package com.contribute.xtrct.postprocess.rules.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.contribute.xtrct.postprocess.utils.PostProcessorTestUtils.buildCSVData;

public class CSVDataTest {

    @Test
    public void getResultsTest() {
        CSVData      csvData = buildCSVData();
        List<String> results = csvData.getResults("Institution", "InstitutionID", Stream.of("109", "14").collect(Collectors.toSet()));
        Assert.assertEquals(results, Arrays.asList("OEM", "Honda Financial Services"));

        results = csvData.getResults("Institution", "InstitutionID", Stream.of("1009").collect(Collectors.toSet()));
        Assert.assertEquals(results, Collections.emptyList());
    }

    @Test
    public void getDistinctValuesForColumnTest() {
        CSVData     csvData        = buildCSVData();
        Set<String> distinctValues = csvData.getDistinctValuesForColumn("Institution");
        final Set<String> expected = Stream.of("OEM", "INFINITI Financial Services (IFS)", "Honda Financial Services")
                                           .collect(Collectors.toSet());
        Assert.assertEquals(distinctValues, expected);
    }

    @Test
    public void updateTest() {
        final Set<String> criteriaParameters = Stream.of("109", "14").collect(Collectors.toSet());
        final String      newValue           = "Test update";
        final CSVData     csvData            = buildCSVData();

        Criteria criteria = new Criteria();
        criteria.setColumn("InstitutionID");
        criteria.setCondition(Condition.EQ);
        criteria.setValues(criteriaParameters);

        final Predicate<List<String>> predicate = criteria.toPredicate(csvData.getHeaderMap());
        final DataValue               dataValue = new DataValue();
        dataValue.setColumn("Institution");
        dataValue.setNewValue(newValue);
        csvData.update(predicate, Collections.singletonList(dataValue));
        List<String> results = csvData.getResults("Institution", "InstitutionID", criteriaParameters);
        Assert.assertEquals(results, Collections.singletonList(newValue));
    }

    @Test
    public void retainProjectedColumnTest() {
        final CSVData      csvData          = buildCSVData();
        final List<String> projectedColumns = Arrays.asList("IncentiveID", "Institution");
        final List<List<String>> expectedRecords = csvData.getRecords()
                                                          .stream()
                                                          .map(record -> Arrays.asList(record.get(0), record.get(2)))
                                                          .collect(Collectors.toList());
        csvData.retainProjectedColumn(projectedColumns);
        Assert.assertEquals(csvData.getHeaderMap(),
                            projectedColumns.stream().collect(Collectors.toMap(Function.identity(), projectedColumns::indexOf)));

        final List<List<String>> records = csvData.getRecords();
        Assert.assertTrue(records.stream().allMatch(record -> record.size() == projectedColumns.size()));
        Assert.assertEquals(records, expectedRecords);
    }

    @Test
    public void getHeadersArrayTest() {
        final CSVData  csvData   = buildCSVData();
        final String[] headerArr = csvData.getHeadersArray();
        Assert.assertEquals(headerArr.length, csvData.getHeaderMap().keySet().size());
        for (int i = 0; i < headerArr.length; i++) {
            Assert.assertEquals(csvData.getHeaderMap().get(headerArr[i]), Integer.valueOf(i));
        }
    }
}
