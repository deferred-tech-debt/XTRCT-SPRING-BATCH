package com.contribute.xtrct.postprocess.rules.engine;

import com.contribute.xtrct.postprocess.rules.model.CSVData;
import com.contribute.xtrct.postprocess.rules.model.Column;
import com.contribute.xtrct.postprocess.rules.model.Rule;
import com.contribute.xtrct.postprocess.rules.model.Operation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * CSV Rule Processor to process the rules. This concrete class handles the operation like REMOVE_FILE, RENAME_FILE, REMOVE_COLUMNS, RENAME_COLUMNS
 */
class CSVRuleProcessor extends CSVDataProcessor {

    private static final Logger LOG = LogManager.getLogger(CSVRuleProcessor.class);

    CSVRuleProcessor(final File extractDir, final Rule rule) {
        super(extractDir, rule);
        init();
    }

    private void init() {
        CSVMappers.updateMapping(rule);
        CSVFormatter.updateMapping(rule);
    }

    void process() throws IOException {
        switch (this.rule.getDefinition().getOperation()) {
            case Operation.REMOVE_FILE:
                this.deleteCSVFIle();
                break;
            case Operation.RENAME_FILE:
                this.renameCSVFIle();
                break;
            case Operation.REMOVE_COLUMNS:
                this.deleteCSVColumn();
                break;
            case Operation.RENAME_COLUMNS:
                this.renameCSVColumn();
                break;
            case Operation.REMOVE_ROWS:
                super.deleteCSVRecords();
                break;
            case Operation.UPDATE_RECORDS:
                super.updateCSVRecords();
        }
    }

    /**
     * Implementation of REMOVE_FILE operation
     * @throws IOException IOException
     */
    private void deleteCSVFIle() throws IOException {
        boolean deleted = Files.deleteIfExists(extractDir.toPath().resolve(rule.getCsv() + CSV_EXTENSION));
        if(deleted) {
            LOG.debug(rule.getCsv()+".csv deleted");
        } else {
            LOG.warn(rule.getCsv()+".csv not deleted");
        }
    }

    /**
     * Implementation of RENAME_FILE operation
     */
    private void renameCSVFIle() {
        File newFileName = new File(extractDir, this.rule.getDefinition().getNewFileName() + CSV_EXTENSION);
        boolean isRenamed = extractDir.toPath().resolve(rule.getCsv() + CSV_EXTENSION).toFile().renameTo(newFileName);
        if(isRenamed) {
            LOG.debug(rule.getCsv() + ".csv has been renamed to " + this.rule.getDefinition().getNewFileName() + CSV_EXTENSION);
        } else {
            LOG.warn(rule.getCsv() + ".csv could not be renamed to " + this.rule.getDefinition().getNewFileName() + CSV_EXTENSION);
        }
    }

    /**
     * Implementation of REMOVE_COLUMNS operation
     * @throws IOException IOException
     */
    private void deleteCSVColumn() throws IOException {

        final CSVData csvData = getCSVData(rule.getCsv());

        final Map<String, Integer> headerMap = csvData.getHeaderMap();
        final List<Integer> deletedColumnIndex = new ArrayList<>(headerMap.size());
        // deleting the columns and collecting them in list
        this.rule.getDefinition().getDeleteColumns().forEach(column -> deletedColumnIndex.add(headerMap.remove(column)));

        final List<List<String>> newRecords = csvData.getRecords().stream().map(record -> {
                                                    final List<String> newRecord = new ArrayList<>();
                                                    for(int i=0; i<record.size(); i++) {
                                                        if(! deletedColumnIndex.contains(i)) {
                                                            newRecord.add(record.get(i));
                                                        }
                                                    }
                                                    return newRecord;
                                                }).collect(Collectors.toList());
        // collecting the headers in correct order
        final String[] newHeaders = headerMap.entrySet()
                                             .stream()
                                             .sorted(Comparator.comparing(Map.Entry::getValue))
                                             .map(Map.Entry::getKey)
                                             .toArray(String[]::new);
        this.writeTempCSV(rule.getCsv(), newHeaders, newRecords);
        this.finalizeTempCSV(rule.getCsv());
    }

    /**
     * Implementation of RENAME_COLUMNS operation
     * @throws IOException IOException
     */
    private void renameCSVColumn() throws IOException {
        final CSVData csvData = getCSVData(rule.getCsv());
        final Map<String, String> newColumnsMap = this.rule.getDefinition()
                                                           .getNewColumns()
                                                           .stream()
                                                           .collect(Collectors.toMap(Column::getName, Column::getNewName));
        final String[] newHeaders = Stream.of(csvData.getHeadersArray()).map(header -> newColumnsMap.getOrDefault(header, header)).toArray(String[]::new);
        final List<List<String>> newRecords = csvData.getRecords();
        this.writeTempCSV(rule.getCsv(), newHeaders, newRecords);
        this.finalizeTempCSV(rule.getCsv());
    }
}
