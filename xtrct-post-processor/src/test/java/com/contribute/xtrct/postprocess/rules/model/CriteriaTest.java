package com.contribute.xtrct.postprocess.rules.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.contribute.xtrct.postprocess.utils.PostProcessorTestUtils.buildCSVData;

public class CriteriaTest {

    @Test
    public void intiGEnGTTest() {
        Criteria criteria = new Criteria();
        criteria.setCondition(Condition.GE);
        criteria.setValues(Stream.of("45", "90.6").collect(Collectors.toSet()));
        criteria.init();
        Assert.assertEquals(criteria.getMaxValue(), 90.6, 0);
        Assert.assertNull(criteria.getMinValue());
        Assert.assertNull(criteria.getPatternPredicate());

        criteria.setValues(Stream.of("90.60", "90.6").collect(Collectors.toSet()));
        criteria.init();
        Assert.assertEquals(criteria.getMaxValue(), 90.6, 0);
        Assert.assertNull(criteria.getMinValue());
        Assert.assertNull(criteria.getPatternPredicate());

        criteria.setValues(Stream.of("90.60", "90.6", "  ").collect(Collectors.toSet()));
        criteria.init();
        Assert.assertEquals(criteria.getMaxValue(), Double.MAX_VALUE, 0);
        Assert.assertNull(criteria.getMinValue());
        Assert.assertNull(criteria.getPatternPredicate());

        criteria.setCondition(Condition.GT);
        criteria.setValues(Stream.of("45", "90.6").collect(Collectors.toSet()));
        criteria.init();
        Assert.assertEquals(criteria.getMaxValue(), 90.6, 0);
        Assert.assertNull(criteria.getMinValue());
        Assert.assertNull(criteria.getPatternPredicate());

        criteria.setValues(Stream.of("90.60", "90.6").collect(Collectors.toSet()));
        criteria.init();
        Assert.assertEquals(criteria.getMaxValue(), 90.6, 0);
        Assert.assertNull(criteria.getMinValue());
        Assert.assertNull(criteria.getPatternPredicate());

        criteria.setValues(Stream.of("90.60", "90.6", "  ").collect(Collectors.toSet()));
        criteria.init();
        Assert.assertEquals(criteria.getMaxValue(), Double.MAX_VALUE, 0);
        Assert.assertNull(criteria.getMinValue());
        Assert.assertNull(criteria.getPatternPredicate());
    }

    @Test
    public void intiLEnLTTest() {
        Criteria criteria = new Criteria();
        criteria.setCondition(Condition.LT);
        criteria.setValues(Stream.of("45", "90.6").collect(Collectors.toSet()));
        criteria.init();
        Assert.assertEquals(criteria.getMinValue(), 45, 0);
        Assert.assertNull(criteria.getMaxValue());
        Assert.assertNull(criteria.getPatternPredicate());

        criteria.setValues(Stream.of("90.60", "90.6").collect(Collectors.toSet()));
        criteria.init();
        Assert.assertEquals(criteria.getMinValue(), 90.6, 0);
        Assert.assertNull(criteria.getMaxValue());
        Assert.assertNull(criteria.getPatternPredicate());

        criteria.setValues(Stream.of("90.60", "90.6", "  ").collect(Collectors.toSet()));
        criteria.init();
        Assert.assertEquals(criteria.getMinValue(), Double.MIN_VALUE, 0);
        Assert.assertNull(criteria.getMaxValue());
        Assert.assertNull(criteria.getPatternPredicate());

        criteria.setCondition(Condition.LE);
        criteria.setValues(Stream.of("45", "90.6").collect(Collectors.toSet()));
        criteria.init();
        Assert.assertEquals(criteria.getMinValue(), 45, 0);
        Assert.assertNull(criteria.getMaxValue());
        Assert.assertNull(criteria.getPatternPredicate());

        criteria.setValues(Stream.of("90.60", "90.6").collect(Collectors.toSet()));
        criteria.init();
        Assert.assertEquals(criteria.getMinValue(), 90.6, 0);
        Assert.assertNull(criteria.getMaxValue());
        Assert.assertNull(criteria.getPatternPredicate());

        criteria.setValues(Stream.of("90.60", "90.6", "  ").collect(Collectors.toSet()));
        criteria.init();
        Assert.assertEquals(criteria.getMinValue(), Double.MIN_VALUE, 0);
        Assert.assertNull(criteria.getMaxValue());
        Assert.assertNull(criteria.getPatternPredicate());
    }

    @Test
    public void intiLIKEnNOT_LIKETest() {
        Criteria criteria = new Criteria();
        criteria.setCondition(Condition.LIKE);
        criteria.setValues(Stream.of("[0123456789]*-0").collect(Collectors.toSet()));
        criteria.init();
        Assert.assertTrue(criteria.getPatternPredicate().test("956234-0"));
        Assert.assertFalse(criteria.getPatternPredicate().test("56556-1-0-1"));
        Assert.assertFalse(criteria.getPatternPredicate().test("abcd-0"));
        Assert.assertNull(criteria.getMaxValue());
        Assert.assertNull(criteria.getMinValue());

        criteria.setValues(Stream.of("[0123456789]*-0", "[0-9]*-0-4").collect(Collectors.toSet()));
        criteria.init();
        Assert.assertTrue(criteria.getPatternPredicate().test("956234-0"));
        Assert.assertTrue(criteria.getPatternPredicate().test("79879787-0-4"));
        Assert.assertFalse(criteria.getPatternPredicate().test("56556-1-0-1"));
        Assert.assertFalse(criteria.getPatternPredicate().test("abcd-0"));
        Assert.assertNull(criteria.getMaxValue());
        Assert.assertNull(criteria.getMinValue());

        criteria.setCondition(Condition.NOT_LIKE);
        criteria.setValues(Stream.of("[0123456789]*-0").collect(Collectors.toSet()));
        criteria.init();
        Assert.assertTrue(criteria.getPatternPredicate().test("956234-0"));
        Assert.assertFalse(criteria.getPatternPredicate().test("56556-1-0-1"));
        Assert.assertFalse(criteria.getPatternPredicate().test("abcd-0"));
        Assert.assertNull(criteria.getMaxValue());
        Assert.assertNull(criteria.getMinValue());

        criteria.setValues(Stream.of("[0123456789]*-0", "[0-9]*-0-4").collect(Collectors.toSet()));
        criteria.init();
        Assert.assertTrue(criteria.getPatternPredicate().test("956234-0"));
        Assert.assertTrue(criteria.getPatternPredicate().test("79879787-0-4"));
        Assert.assertFalse(criteria.getPatternPredicate().test("56556-1-0-1"));
        Assert.assertFalse(criteria.getPatternPredicate().test("abcd-0"));
        Assert.assertNull(criteria.getMaxValue());
        Assert.assertNull(criteria.getMinValue());
    }

    @Test
    public void toPredicateEQTest() {
        CSVData  csvData  = buildCSVData();
        Criteria criteria = new Criteria();
        criteria.setColumn("Institution");
        criteria.setCondition(Condition.EQ);
        criteria.setValues(Stream.of("OEM", "Honda Financial Services").collect(Collectors.toSet()));
        Predicate<List<String>> predicate = criteria.toPredicate(csvData.getHeaderMap());
        Assert.assertTrue(csvData.getRecords().stream().anyMatch(predicate));
        Assert.assertFalse(csvData.getRecords().stream().allMatch(predicate));

        criteria.setValues(Stream.of("OEM123", "Honda Financial").collect(Collectors.toSet()));
        predicate = criteria.toPredicate(csvData.getHeaderMap());
        Assert.assertTrue(csvData.getRecords().stream().noneMatch(predicate));
    }

    @Test
    public void toPredicateNEQTest() {
        CSVData  csvData  = buildCSVData();
        Criteria criteria = new Criteria();
        criteria.setColumn("Institution");
        criteria.setCondition(Condition.NEQ);
        criteria.setValues(Stream.of("OEM", "Honda Financial Services").collect(Collectors.toSet()));
        Predicate<List<String>> predicate = criteria.toPredicate(csvData.getHeaderMap());
        Assert.assertTrue(csvData.getRecords().stream().anyMatch(predicate));
        Assert.assertFalse(csvData.getRecords().stream().allMatch(predicate));

        criteria.setValues(Stream.of("OEM123", "Honda Financial").collect(Collectors.toSet()));
        predicate = criteria.toPredicate(csvData.getHeaderMap());
        Assert.assertTrue(csvData.getRecords().stream().allMatch(predicate));
    }

    @Test
    public void toPredicateGETest() {
        CSVData  csvData  = buildCSVData();
        Criteria criteria = new Criteria();
        criteria.setColumn("InstitutionID");
        criteria.setCondition(Condition.GE);
        criteria.setValues(Stream.of("14", "109").collect(Collectors.toSet()));
        criteria.init();
        Predicate<List<String>> predicate = criteria.toPredicate(csvData.getHeaderMap());
        Assert.assertTrue(csvData.getRecords().stream().anyMatch(predicate));
        Assert.assertFalse(csvData.getRecords().stream().allMatch(predicate));

        criteria.setValues(Stream.of("1000", "109").collect(Collectors.toSet()));
        criteria.init();
        predicate = criteria.toPredicate(csvData.getHeaderMap());
        Assert.assertTrue(csvData.getRecords().stream().noneMatch(predicate));

        criteria.setColumn("Institution");
        criteria.setValues(Stream.of("1000", "109").collect(Collectors.toSet()));
        criteria.init();
        predicate = criteria.toPredicate(csvData.getHeaderMap());
        Assert.assertTrue(csvData.getRecords().stream().noneMatch(predicate));
    }

    @Test
    public void toPredicateGTTest() {
        CSVData  csvData  = buildCSVData();
        Criteria criteria = new Criteria();
        criteria.setColumn("InstitutionID");
        criteria.setCondition(Condition.GT);
        criteria.setValues(Stream.of("14", "109").collect(Collectors.toSet()));
        criteria.init();
        Predicate<List<String>> predicate = criteria.toPredicate(csvData.getHeaderMap());
        Assert.assertTrue(csvData.getRecords().stream().noneMatch(predicate));

        criteria.setValues(Stream.of("14", "89").collect(Collectors.toSet()));
        criteria.init();
        predicate = criteria.toPredicate(csvData.getHeaderMap());
        Assert.assertTrue(csvData.getRecords().stream().anyMatch(predicate));
        Assert.assertFalse(csvData.getRecords().stream().allMatch(predicate));

        criteria.setColumn("Institution");
        criteria.setValues(Stream.of("1000", "109").collect(Collectors.toSet()));
        criteria.init();
        predicate = criteria.toPredicate(csvData.getHeaderMap());
        Assert.assertTrue(csvData.getRecords().stream().noneMatch(predicate));

        criteria.setColumn("Institution");
        criteria.setValues(Stream.of("1000", "109").collect(Collectors.toSet()));
        criteria.init();
        predicate = criteria.toPredicate(csvData.getHeaderMap());
        Assert.assertTrue(csvData.getRecords().stream().noneMatch(predicate));
    }

    @Test
    public void toPredicateLETest() {
        CSVData  csvData  = buildCSVData();
        Criteria criteria = new Criteria();
        criteria.setColumn("InstitutionID");
        criteria.setCondition(Condition.LE);
        criteria.setValues(Stream.of("14", "109").collect(Collectors.toSet()));
        criteria.init();
        Predicate<List<String>> predicate = criteria.toPredicate(csvData.getHeaderMap());
        Assert.assertTrue(csvData.getRecords().stream().anyMatch(predicate));
        Assert.assertFalse(csvData.getRecords().stream().allMatch(predicate));

        criteria.setValues(Stream.of("1000", "109").collect(Collectors.toSet()));
        criteria.init();
        predicate = criteria.toPredicate(csvData.getHeaderMap());
        Assert.assertTrue(csvData.getRecords().stream().allMatch(predicate));

        criteria.setValues(Stream.of("1").collect(Collectors.toSet()));
        criteria.init();
        predicate = criteria.toPredicate(csvData.getHeaderMap());
        Assert.assertTrue(csvData.getRecords().stream().noneMatch(predicate));

        criteria.setColumn("Institution");
        criteria.setValues(Stream.of("1000", "109").collect(Collectors.toSet()));
        criteria.init();
        predicate = criteria.toPredicate(csvData.getHeaderMap());
        Assert.assertTrue(csvData.getRecords().stream().noneMatch(predicate));
    }

    @Test
    public void toPredicateLTTest() {
        CSVData  csvData  = buildCSVData();
        Criteria criteria = new Criteria();
        criteria.setColumn("InstitutionID");
        criteria.setCondition(Condition.LT);
        criteria.setValues(Stream.of("14", "109").collect(Collectors.toSet()));
        criteria.init();
        Predicate<List<String>> predicate = criteria.toPredicate(csvData.getHeaderMap());
        Assert.assertTrue(csvData.getRecords().stream().noneMatch(predicate));

        criteria.setValues(Stream.of("46", "89").collect(Collectors.toSet()));
        criteria.init();
        predicate = criteria.toPredicate(csvData.getHeaderMap());
        Assert.assertTrue(csvData.getRecords().stream().anyMatch(predicate));
        Assert.assertFalse(csvData.getRecords().stream().allMatch(predicate));

        criteria.setColumn("Institution");
        criteria.setValues(Stream.of("1000", "109").collect(Collectors.toSet()));
        criteria.init();
        predicate = criteria.toPredicate(csvData.getHeaderMap());
        Assert.assertTrue(csvData.getRecords().stream().noneMatch(predicate));
    }

    @Test
    public void toPredicateLIKETest() {
        CSVData  csvData  = buildCSVData();
        Criteria criteria = new Criteria();
        criteria.setColumn("Institution");
        criteria.setCondition(Condition.LIKE);
        criteria.setValues(Stream.of("OEM", "109").collect(Collectors.toSet()));
        criteria.init();
        Predicate<List<String>> predicate = criteria.toPredicate(csvData.getHeaderMap());
        Assert.assertTrue(csvData.getRecords().stream().anyMatch(predicate));
        Assert.assertFalse(csvData.getRecords().stream().allMatch(predicate));
    }

    @Test
    public void toPredicateNotLIKETest() {
        CSVData  csvData  = buildCSVData();
        Criteria criteria = new Criteria();
        criteria.setColumn("Institution");
        criteria.setCondition(Condition.NOT_LIKE);
        criteria.setValues(Stream.of("OEM", "109").collect(Collectors.toSet()));
        criteria.init();
        Predicate<List<String>> predicate = criteria.toPredicate(csvData.getHeaderMap());
        Assert.assertTrue(csvData.getRecords().stream().anyMatch(predicate));
        Assert.assertFalse(csvData.getRecords().stream().allMatch(predicate));

        criteria.setValues(Stream.of("[a-zA-Z() ]*").collect(Collectors.toSet()));
        criteria.init();
        predicate = criteria.toPredicate(csvData.getHeaderMap());
        Assert.assertTrue(csvData.getRecords().stream().noneMatch(predicate));
    }
}
