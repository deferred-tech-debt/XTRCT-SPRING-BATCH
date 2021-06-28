package com.chromedata.incentives.extract.postprocess.rules.engine;

import com.chromedata.incentives.extract.postprocess.rules.model.Column;
import com.chromedata.incentives.extract.postprocess.rules.model.Relation;
import com.chromedata.incentives.extract.postprocess.rules.model.Rule;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Responsible to provide mapping b/w the CSV files as per rules configured in rules/csv-mapping.yml
 */
public class CSVMappers {

    private static final Logger LOG = LogManager.getLogger(CSVMappers.class);
    private static final ObjectMapper MAPPER = new ObjectMapper(new YAMLFactory());
    private static final List<Relation> CSV_RELATIONS = CSVMappers.loadCSVRelations();
    private static final String CSV_MAPPING_YML = "rules/csv-mapping.yml";

    private static List<Relation> loadCSVRelations() {
        try (InputStream csvMappingResourceStream = CSVMappers.class.getClassLoader().getResourceAsStream(CSV_MAPPING_YML)) {
            return Stream.of(MAPPER.readValue(csvMappingResourceStream, Relation[].class)).collect(Collectors.toList());
        } catch (IOException e) {
            LOG.error("Failed to load CSV Mappings from classpath resource {}", CSV_MAPPING_YML);
            throw new RuntimeException(e);
        }
    }

    /**
     * Get the CSV mappings for given CSV
     * @param csv CSV file
     * @return Optional CSV Formats
     */
    static Optional<Relation> getRelation(final String csv) {
        return CSV_RELATIONS.parallelStream().filter(relation -> relation.getCsv().equalsIgnoreCase(csv)).findFirst();
    }

    /**
     * Updates thr csv mapping for provided rule.<br>
     * 1 - REMOVE_FILE : Removes the CSV mapping from list<br>
     * 2 - RENAME_FILE : Rename the CSV in mapping list<br>
     * 3 - REMOVE_COLUMNS : Remove the column on given CSV file in it's mapping if exist<br>
     * 4 - RENAME_COLUMNS : Rename the column on given CSV file in it's mapping if exist
     * @param rule rule
     */
    public static void updateMapping(final Rule rule) {
        switch (rule.getDefinition().getOperation()) {
            case REMOVE_FILE:
                deleteCSVMapping(rule.getCsv());
                break;
            case RENAME_FILE:
                renameCSVMapping(rule.getCsv(), rule.getDefinition().getNewFileName());
                break;
            case REMOVE_COLUMNS:
                deleteCSVColumnMapping(rule.getCsv(), rule.getDefinition().getDeleteColumns());
                break;
            case RENAME_COLUMNS:
                renameCSVColumnMapping(rule.getCsv(), rule.getDefinition().getNewColumns());
        }
    }

    private static void renameCSVColumnMapping(final String csv, final List<Column> columns) {
        final Map<String, String> columnMap = columns.stream().collect(Collectors.toMap(Column::getName, Column::getNewName));
        // updating parentColumn for given CSV
        CSV_RELATIONS.parallelStream()
                     .filter(relation -> relation.getCsv().equalsIgnoreCase(csv))
                     .forEach(relation -> relation.getMapping()
                                                  .stream()
                                                  .filter(mapping -> columnMap.containsKey(mapping.getParentColumn()))
                                                  .forEach(mapping -> mapping.setParentColumn(columnMap.get(mapping.getParentColumn()))));

        // updating mappingColumn for given CSV
        CSV_RELATIONS.parallelStream()
                     .forEach(relation -> relation.getMapping()
                                                  .stream()
                                                  .filter(mapping -> mapping.getCsv().equalsIgnoreCase(csv))
                                                  .filter(mapping -> columnMap.containsKey(mapping.getMappingColumn()))
                                                  .forEach(mapping -> mapping.setMappingColumn(columnMap.get(mapping.getMappingColumn()))));
    }

    private static void deleteCSVColumnMapping(final String csv, final List<String> deleteColumns) {
        CSV_RELATIONS.parallelStream()
                     .filter(relation -> relation.getCsv().equalsIgnoreCase(csv))
                     .forEach(relation -> relation.getMapping()
                                                  .removeIf(mapping -> deleteColumns.stream()
                                                                                    .anyMatch(deleteColumn -> deleteColumn.equalsIgnoreCase(mapping.getParentColumn()))));
    }

    private static void renameCSVMapping(final String csv, final String newCSVName) {
        CSV_RELATIONS.parallelStream().filter(relation -> relation.getCsv().equalsIgnoreCase(csv)).forEach(relation -> relation.setCsv(newCSVName));
        CSV_RELATIONS.parallelStream()
                     .forEach(relation -> relation.getMapping()
                                                  .parallelStream()
                                                  .filter(mapping -> mapping.getCsv().equalsIgnoreCase(csv))
                                                  .forEach(mapping -> mapping.setCsv(newCSVName)));
    }

    private static void deleteCSVMapping(final String csv) {
        CSV_RELATIONS.removeIf(relation -> relation.getCsv().equalsIgnoreCase(csv));
        CSV_RELATIONS.parallelStream().forEach(relation -> relation.getMapping().removeIf(mapping -> mapping.getCsv().equalsIgnoreCase(csv)));
    }
}
