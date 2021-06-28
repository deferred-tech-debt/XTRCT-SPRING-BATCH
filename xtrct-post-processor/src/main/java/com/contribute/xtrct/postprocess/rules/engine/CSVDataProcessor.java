package com.contribute.xtrct.postprocess.rules.engine;

import com.chromedata.commons.dsvio.core.DSVReader;
import com.chromedata.commons.dsvio.core.DSVWriter;
import com.contribute.xtrct.postprocess.rules.model.CSVData;
import com.contribute.xtrct.postprocess.rules.model.Condition;
import com.contribute.xtrct.postprocess.rules.model.Criteria;
import com.contribute.xtrct.postprocess.rules.model.Mapping;
import com.contribute.xtrct.postprocess.rules.model.Relation;
import com.contribute.xtrct.postprocess.rules.model.Rule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * CSV Data Processor to process the rules. This concrete class handles the operation like REMOVE_ROWS, UPDATE_RECORDS.
 * It also handles the common method to read the csv files in CSV Data, writing records back to CSV file.
 */
class CSVDataProcessor {

    private static final Logger LOG = LogManager.getLogger(CSVDataProcessor.class);

    static final String CSV_EXTENSION = ".csv";
    private static final String CSV_TMP = ".csv.tmp";

    File extractDir;
    Rule rule;

    CSVDataProcessor(final File extractDir, final Rule rule) {
        this.extractDir = extractDir;
        this.rule = rule;
    }

    /**
     * Load the CSV records in CSV data instance
     * @param csv csv file
     * @return CSV data instance
     * @throws IOException IOException
     */
    CSVData getCSVData(final String csv) throws IOException {
        try(Reader reader = Files.newBufferedReader(extractDir.toPath().resolve(csv + CSV_EXTENSION));
            DSVReader dsvReader = new DSVReader(reader)) {

            final List<String> header = Arrays.asList(dsvReader.getHeader());
            final Map<String, Integer> headerMap = header.stream().collect(Collectors.toMap(Function.identity(), header::indexOf));

            String[] line;
            List<List<String>> records = new ArrayList<>();
            while((line = dsvReader.readLine()) != null) {
                records.add(Stream.of(line).collect(Collectors.toList()));
            }
            return new CSVData(headerMap, records);
        }
    }

    /**
     * Write back to records to .tmp.csv file
     * @param csv csv file
     * @param header headers
     * @param newRecords new records
     * @throws IOException IOException
     */
    void writeTempCSV(final String csv, final String[] header, final List<List<String>> newRecords) throws IOException {
        try (Writer writer = Files.newBufferedWriter(extractDir.toPath().resolve(csv + CSV_TMP));
             DSVWriter dsvWriter = new DSVWriter(writer)) {

            CSVFormatter.formatCSV(csv, Arrays.asList(header), newRecords);

            dsvWriter.writeHeader(header, false);
            for(List<String> record : newRecords) {
                dsvWriter.writeRow(record.toArray(new String[0]));
            }
        }
    }

    /**
     * Rename the .csv.tmp to .csv file
     * @param csv csv file
     * @throws IOException IOException
     */
    void finalizeTempCSV(final String csv) throws IOException {
        Files.delete(extractDir.toPath().resolve(csv + CSV_EXTENSION));
        File newFileName = new File(extractDir, csv + CSV_EXTENSION);
        extractDir.toPath().resolve(csv + CSV_TMP).toFile().renameTo(newFileName);
    }

    /**
     * Implementation of UPDATE_RECORDS operation
     * @throws IOException IOException
     */
    void updateCSVRecords() throws IOException {
        final CSVData csvData = this.getCSVData(rule.getCsv());
        final Predicate<List<String>> predicate = rule.getDefinition()
                                                      .getCriteria()
                                                      .stream()
                                                      .map(criteria -> criteria.toPredicate(csvData.getHeaderMap()))
                                                      .reduce(Predicate::or)
                                                      .get();

        csvData.update(predicate, this.rule.getDefinition().getUpdatedValues());
        this.writeTempCSV(rule.getCsv(), csvData.getHeadersArray(), csvData.getRecords());
        this.finalizeTempCSV(rule.getCsv());
    }

    /**
     * Implementation of REMOVE_ROWS operation
     * @throws IOException
     */
    void deleteCSVRecords() throws IOException {
        Optional<Relation> optionalRelation = CSVMappers.getRelation(rule.getCsv());
        if(optionalRelation.isPresent()) {
            deleteCSVRecordsWithMapping(rule.getCsv(), rule.getDefinition().getCriteria(), optionalRelation.get().getMapping());
        } else {
            deleteCSVRecordsWithMapping(rule.getCsv(), rule.getDefinition().getCriteria(), null);
        }
    }

    /**
     * Recursively delete the records from mapped CSV files provided in CSV Mapping
     * @param csv csv file
     * @param criteria criteria to delete records
     * @param mappings mapping for given csv file
     * @throws IOException IOException
     */
    private void deleteCSVRecordsWithMapping(final String csv, final List<Criteria> criteria, final List<Mapping> mappings) throws IOException {
        final CSVData deletedCSVData = deleteRecords(csv, criteria);
        LOG.debug("Deleted {} records from {}", deletedCSVData.getRecords().size(), csv);

        if(Objects.isNull(mappings) || Objects.isNull(deletedCSVData.getRecords()) || deletedCSVData.getRecords().isEmpty()) {
            return;
        }

        deletedCSVData.retainProjectedColumn(mappings.stream().map(Mapping::getParentColumn).collect(Collectors.toList()));

        for(Mapping mapping : mappings) {
            final String mappedCSV = mapping.getCsv();
            final List<Criteria> builtCriteria = buildCriteria(mapping, deletedCSVData);
            final Optional<Relation> optionalRelation = CSVMappers.getRelation(mappedCSV);

            // filter out builtCriteria's values that persist in csv (passed in method param) even after records deleted.
            filterCriteriaValuesIfPersists(csv, mapping, builtCriteria);

            if(optionalRelation.isPresent()) {
                deleteCSVRecordsWithMapping(mappedCSV, builtCriteria, optionalRelation.get().getMapping());
            } else {
                deleteCSVRecordsWithMapping(mappedCSV, builtCriteria, null);
            }
        }
    }

    /**
     * Actual method to delete the records from CSV files
     * @param csv csv file
     * @param criterias criteria to delete records
     * @return
     * @throws IOException
     */
    private CSVData deleteRecords(final String csv, final List<Criteria> criterias) throws IOException {
        final CSVData deletedCSVData;
        try(Reader reader = Files.newBufferedReader(extractDir.toPath().resolve(csv + CSV_EXTENSION));
            DSVReader dsvReader = new DSVReader(reader)) {
            final List<String> header = Arrays.asList(dsvReader.getHeader());
            final Map<String, Integer> headerMap = header.stream().collect(Collectors.toMap(Function.identity(), header::indexOf));
            final Predicate<List<String>> predicate = criterias.stream()
                                                               .map(criteria -> criteria.toPredicate(headerMap))
                                                               .reduce(Predicate::or)
                                                               .get();

            final List<List<String>> deletedCSVRecords = new ArrayList<>();
            final List<List<String>> newRecords        = new ArrayList<>();
            String[] line;

            while((line = dsvReader.readLine()) != null) {
                final List<String> record = Stream.of(line).collect(Collectors.toList());
                if (predicate.test(record)) {
                    deletedCSVRecords.add(record);
                } else {
                    newRecords.add(record);
                }
            }
            this.writeTempCSV(csv, dsvReader.getHeader(), newRecords);

            deletedCSVData = new CSVData(headerMap, deletedCSVRecords);
        }
        this.finalizeTempCSV(csv);
        return deletedCSVData;
    }

    // remove the values for criteria if those values are still present in parent mapping CSV files
    private void filterCriteriaValuesIfPersists(final String csv, final Mapping mapping, final List<Criteria> builtCriteria) throws IOException {
        final CSVData csvData = getCSVData(csv);
        for (Criteria criteria : builtCriteria) {
            final List<String> persistValues = csvData.getResults(mapping.getParentColumn(), mapping.getParentColumn(), criteria.getValues());
            criteria.getValues().removeAll(persistValues);
        }
    }

    // build criteria from given deleted csv records
    private List<Criteria> buildCriteria(final Mapping mapping, final CSVData deletedCSVData) {
        final Criteria criteria = new Criteria();
        criteria.setColumn(mapping.getMappingColumn());
        criteria.setCondition(Condition.EQ);
        criteria.setValues(deletedCSVData.getDistinctValuesForColumn(mapping.getParentColumn()));
        criteria.init();
        return Collections.singletonList(criteria);
    }
}