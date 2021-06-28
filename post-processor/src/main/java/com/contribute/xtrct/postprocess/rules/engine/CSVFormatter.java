package com.contribute.xtrct.postprocess.rules.engine;

import com.contribute.xtrct.postprocess.rules.model.CSVFormat;
import com.contribute.xtrct.postprocess.rules.model.Column;
import com.contribute.xtrct.postprocess.rules.model.Format;
import com.contribute.xtrct.postprocess.rules.model.Rule;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Responsible to format the CSV files as per rules configured in rules/csv-formats.yml
 */
public class CSVFormatter {

    private static final Logger LOG = LogManager.getLogger(CSVFormatter.class);
    private static final ObjectMapper MAPPER = new ObjectMapper(new YAMLFactory());
    private static final List<CSVFormat> CSV_FORMATS = CSVFormatter.loadCSVFormats();
    private static final String CSV_FORMATS_YML = "rules/csv-formats.yml";

    private static List<CSVFormat> loadCSVFormats() {
        try (InputStream csvFormatResourceStream = CSVFormatter.class.getClassLoader().getResourceAsStream(CSV_FORMATS_YML)) {
            return Stream.of(MAPPER.readValue(csvFormatResourceStream, CSVFormat[].class)).collect(Collectors.toList());
        } catch (IOException e) {
            LOG.error("Failed to load CSV Formats from classpath resource {}", CSV_FORMATS_YML);
            throw new RuntimeException(e);
        }
    }

    /**
     * Get the CSV formats for given CSV
     * @param csv CSV file
     * @return Optional CSV Formats
     */
    private static Optional<CSVFormat> getCSVFormat(final String csv) {
        return CSV_FORMATS.parallelStream().filter(csvFormat -> csvFormat.getCsv().equalsIgnoreCase(csv)).findFirst();
    }

    /**
     * Format the CSV records
     * @param csv CSV file
     * @param headers headers
     * @param records csv records
     */
    static void formatCSV(final String csv, final List<String> headers, final List<List<String>> records) {
        final Optional<CSVFormat> csvFormat = getCSVFormat(csv);
        if(csvFormat.isPresent()) {
            final Map<String, Integer> headerMap = headers.stream().collect(Collectors.toMap(Function.identity(), headers::indexOf));
            for(Format format : csvFormat.get().getFormats()) {
                final Set<Integer> headerIndex = getHeaderIndexes(headerMap, format);
                formatRecords(headerIndex, records, format);
            }
        }
    }

    /**
     * Get Header's index for which CSV needs to be formatted
     * @param headerMap header map
     * @param format format details
     * @return Set of header's index
     */
    private static Set<Integer> getHeaderIndexes(final Map<String, Integer> headerMap, final Format format) {
        if(format.getColumns() == null || format.getColumns().isEmpty()) {
            return format.getType().isElseFormat() ? new HashSet<>(headerMap.values()) : Collections.emptySet();
        }

        if(format.getType().isElseFormat()) {
            return  headerMap.entrySet().stream().filter(headerMapEntry -> ! format.getColumns().contains(headerMapEntry.getKey())).map(Map.Entry::getValue).collect(
                Collectors.toSet());
        } else {
            return headerMap.entrySet().stream().filter(headerMapEntry -> format.getColumns().contains(headerMapEntry.getKey())).map(Map.Entry::getValue).collect(
                Collectors.toSet());
        }
    }

    // format the records on provided header's index
    private static void formatRecords(final Set<Integer> headerIndex, final List<List<String>> records, final Format format) {
        switch (format.getType()) {
            case ENCAPSULATE:
            case ENCAPSULATE_ELSE:
                encapsulateRecords(headerIndex, records, format.getValue());
                break;
            case FORMAT_STRING:
            case FORMAT_STRING_ELSE:
                formatStringRecords(headerIndex, records, format.getValue());
                break;
            case FORMAT_DATE:
            case FORMAT_DATE_ELSE:
                formatDateRecords(headerIndex, records, format.getValue());
        }
    }

    // Encapsulate the columns on given records
    private static void encapsulateRecords(final Set<Integer> headerIndex, final List<List<String>> records, final String value) {
        records.parallelStream().forEach(record ->
                                             headerIndex.parallelStream().forEach(index ->
                                                                                      record.set(index, encapsulateString(value, record.get(index)))
                                             )
        );
    }

    /**
     * encapsulate the text with given encapsulater, if text contain's the encapsulter then it appends one more encapsulater in text
     * e.g. text = Sample Text's
     * encapsulater = "'"
     * result = 'Sample Text''s'
     */
    private static String encapsulateString(final String encapsulater, final String text) {
        return Objects.isNull(text)
               ? text
               : Stream.of(text.split(encapsulater, -1)).collect(Collectors.joining(encapsulater + encapsulater, encapsulater, encapsulater));
    }

    /**
     * format the text with given string formatter
     * e.g. text = Sample Text's
     * encapsulater = "[%s]"
     * result = [Sample Text's]
     */
    private static void formatStringRecords(final Set<Integer> headerIndex, final List<List<String>> records, final String value) {
        records.parallelStream().forEach(record ->
                                             headerIndex.parallelStream().forEach(index ->
                                                                                      record.set(index, String.format(value, record.get(index)))
                                             )
        );
    }

    // format date string in provided format
    private static void formatDateRecords(final Set<Integer> headerIndex, final List<List<String>> records, final String value) {
        records.parallelStream().forEach(record ->
                                             headerIndex.parallelStream().forEach(index ->
                                                                                      record.set(index, formatDateString(value, record.get(index)))
                                             )
        );
    }

    private static String formatDateString(final String dateFormat, final String text) {
        return LocalDateTime.parse(text).format(DateTimeFormatter.ofPattern(dateFormat));
    }

    /**
     * Updates thr csv format mapping for provided rule.<br>
     * 1 - REMOVE_FILE : Removes the CSV Format mapping from list<br>
     * 2 - RENAME_FILE : Rename the CSV in mapping list<br>
     * 3 - REMOVE_COLUMNS : Remove the column on given CSV file in it's mapping if exist<br>
     * 4 - RENAME_COLUMNS : Rename the column on given CSV file in it's mapping if exist
     * @param rule rule
     */
    public static void updateMapping(final Rule rule) {
        switch (rule.getDefinition().getOperation()) {
            case REMOVE_FILE:
                deleteCSVFormatting(rule.getCsv());
                break;
            case RENAME_FILE:
                renameCSVFormatting(rule.getCsv(), rule.getDefinition().getNewFileName());
                break;
            case REMOVE_COLUMNS:
                deleteCSVColumnFormatting(rule.getCsv(), rule.getDefinition().getDeleteColumns());
                break;
            case RENAME_COLUMNS:
                renameCSVColumnFormatting(rule.getCsv(), rule.getDefinition().getNewColumns());
        }
    }

    private static void renameCSVColumnFormatting(final String csv, final List<Column> columns) {
        final Map<String, String> columnMap = columns.stream().collect(Collectors.toMap(Column::getName, Column::getNewName));
        CSV_FORMATS.parallelStream()
                   .filter(csvFormat -> csvFormat.getCsv().equalsIgnoreCase(csv))
                   .flatMap(csvFormat -> csvFormat.getFormats().stream())
                   .forEach(format ->
                       columnMap.forEach((oldCol, newCol) -> {
                           if(Objects.nonNull(format.getColumns()) && format.getColumns().contains(oldCol)) {
                               format.getColumns().set(format.getColumns().indexOf(oldCol), newCol);
                           }
                       })
                   );
    }

    private static void deleteCSVColumnFormatting(final String csv, final List<String> deleteColumns) {
        // check for else type formatting, in case all columns deleted
        CSV_FORMATS.parallelStream()
                   .filter(csvFormat -> csvFormat.getCsv().equalsIgnoreCase(csv))
                   .forEach(csvFormat -> csvFormat.getFormats()
                                                  .removeIf(format -> format.getType().isElseFormat() &&
                                                                      Objects.nonNull(format.getColumns()) &&
                                                                      format.getColumns().size() == deleteColumns.size() &&
                                                                      format.getColumns().containsAll(deleteColumns)));

        CSV_FORMATS.parallelStream()
                   .filter(csvFormat -> csvFormat.getCsv().equalsIgnoreCase(csv))
                   .flatMap(csvFormat -> csvFormat.getFormats().stream())
                   .filter(format -> Objects.nonNull(format.getColumns()))
                   .forEach(format -> format.getColumns().removeAll(deleteColumns));
    }

    private static void renameCSVFormatting(final String csv, final String newCSVName) {
        CSV_FORMATS.parallelStream().filter(csvFormat -> csvFormat.getCsv().equalsIgnoreCase(csv)).forEach(csvFormat -> csvFormat.setCsv(newCSVName));
    }

    private static void deleteCSVFormatting(final String csv) {
        CSV_FORMATS.removeIf(csvFormat -> csvFormat.getCsv().equalsIgnoreCase(csv));
    }
}
