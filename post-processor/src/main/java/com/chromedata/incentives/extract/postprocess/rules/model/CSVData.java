package com.chromedata.incentives.extract.postprocess.rules.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@AllArgsConstructor
public class CSVData {

    private Map<String, Integer> headerMap;
    private List<List<String>> records;

    /**
     * Method to query the records for given projected column, columns to query for given parameters (OR Clause)
     * @param projectedColumn projection clause column
     * @param queriedColumns where clause column
     * @param parameters value of where clause column
     * @return Distinct list of values
     */
    public List<String> getResults(final String projectedColumn, final String queriedColumns, final Set<String> parameters) {
        final Integer projectedColumnIndex = headerMap.get(projectedColumn);
        final Integer queriedColumnsIndex = headerMap.get(queriedColumns);
        return records.stream()
                      .filter(record -> parameters.contains(record.get(queriedColumnsIndex)))
                      .map(record -> record.get(projectedColumnIndex))
                      .distinct()
                      .collect(Collectors.toList());
    }

    /**
     * Get the distinct value of given column
     * @param column projected columns
     * @return Set of values of given columns
     */
    public Set<String> getDistinctValuesForColumn(final String column) {
        final Integer columnIndex = headerMap.get(column);
        return records.stream()
                      .parallel()
                      .map(record -> record.get(columnIndex))
                      .collect(Collectors.toSet());
    }

    /**
     * Update the records for given criteria as row predicate with provided list of values (mapped to columns)
     * @param rowPredicate criteria as row predicate
     * @param updatedValues list of values (mapped to columns)
     */
    public void update(final Predicate<List<String>> rowPredicate, final List<DataValue> updatedValues) {
        this.records.stream()
                    .filter(rowPredicate)
                    .forEach(record ->
                                 updatedValues.forEach(dataValue ->
                                                           record.set(headerMap.get(dataValue.getColumn()), dataValue.getNewValue())
                                 )
                    );
    }

    public void retainProjectedColumn(final List<String> projectedColumns) {
        final List<Integer> deletedColumnsIndex = this.headerMap.entrySet()
                                                                .stream()
                                                                .filter(columnEntry -> !projectedColumns.contains(columnEntry.getKey()))
                                                                .map(Map.Entry::getValue)
                                                                .filter(Objects::nonNull)
                                                                .sorted()
                                                                .collect(Collectors.toList());

        for(int i=0; i<deletedColumnsIndex.size(); i++) {
            for(List<String> record : this.records) {
                record.remove(deletedColumnsIndex.get(i) - i);
            }
        }
        reIndexHeaders(projectedColumns);
    }

    private void reIndexHeaders(final List<String> projectedColumns) {
        final List<String> headers = getOrderedHeaderStream().collect(Collectors.toList());
        headers.retainAll(projectedColumns);
        this.headerMap.clear();
        this.headerMap.putAll(headers.stream().collect(Collectors.toMap(Function.identity(), headers::indexOf)));
    }

    private Stream<String> getOrderedHeaderStream() {
        return this.headerMap.entrySet()
                             .stream()
                             .sorted(Comparator.comparing(Map.Entry::getValue))
                             .map(Map.Entry::getKey);
    }

    /**
     * Get the headers arrays in order
     * @return header array
     */
    public String[] getHeadersArray() {
        return getOrderedHeaderStream().toArray(String[]::new);
    }
}