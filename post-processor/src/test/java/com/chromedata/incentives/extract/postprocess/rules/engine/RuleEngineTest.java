package com.chromedata.incentives.extract.postprocess.rules.engine;

import com.chromedata.incentives.extract.postprocess.ExtractVersion;
import com.chromedata.incentives.extract.postprocess.rules.model.Condition;
import com.chromedata.incentives.extract.postprocess.rules.model.Criteria;
import com.chromedata.incentives.extract.postprocess.rules.model.DataValue;
import com.chromedata.incentives.extract.postprocess.rules.model.Rule;
import com.chromedata.incentives.extract.postprocess.utils.PostProcessorTestUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import static com.chromedata.incentives.extract.postprocess.rules.model.Operation.REMOVE_ROWS;
import static com.chromedata.incentives.extract.postprocess.rules.model.Operation.UPDATE_RECORDS;

public class RuleEngineTest {

    @org.junit.Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void ruleEngineTest() throws IOException, NoSuchFieldException, IllegalAccessException {
        RuleEngine ruleEngine = new RuleEngine(ExtractVersion.TWO_DOT_TWO, temporaryFolder.newFile());
        Field      field      = ruleEngine.getClass().getDeclaredField("rules");
        field.setAccessible(true);
        List<Rule> rules = (List<Rule>) field.get(ruleEngine);
        Assert.assertEquals(rules.size(), 5);

        validateFirstRule(rules.get(0));
        validateSecondRule(rules.get(1));
        validateThirdRule(rules.get(2));
        validateFourthRule(rules.get(3));
        validateFifthRule(rules.get(4));
    }

    @Test(expected = IOException.class)
    public void ruleEngineProcessTest() throws IOException {
        RuleEngine ruleEngine = new RuleEngine(ExtractVersion.TWO_DOT_TWO, temporaryFolder.newFile());
        ruleEngine.execute();
    }

    private void validateFifthRule(final Rule rule) {
        Assert.assertEquals(rule.getCsv(), "Version");
        Assert.assertEquals(rule.getDefinition().getOperation(), UPDATE_RECORDS);
        Criteria criteria = new Criteria();
        criteria.setColumn("Schema Version");
        criteria.setCondition(Condition.LIKE);
        criteria.setValues(Collections.singleton("3.0.0[.0-9]*"));
        criteria.init();
        Assert.assertTrue(PostProcessorTestUtils.isEqualCollections(rule.getDefinition().getCriteria(),
                                                                    Collections.singletonList(criteria),
                                                                    "patternPredicate"));
        final DataValue updateValue = new DataValue();
        updateValue.setColumn("Schema Version");
        updateValue.setNewValue("2.2.0");
        Assert.assertEquals(rule.getDefinition().getUpdatedValues(), Collections.singletonList(updateValue));
        Assert.assertNull(rule.getDefinition().getDeleteColumns());
        Assert.assertNull(rule.getDefinition().getNewColumns());
        Assert.assertNull(rule.getDefinition().getNewFileName());
    }

    private void validateFourthRule(final Rule rule) {
        Assert.assertEquals(rule.getCsv(), "ValueVariation");
        Assert.assertEquals(rule.getDefinition().getOperation(), REMOVE_ROWS);
        Criteria criteria = new Criteria();
        criteria.setColumn("ValueVariationID");
        criteria.setCondition(Condition.NOT_LIKE);
        criteria.setValues(Collections.singleton("[0-9]*-0"));
        criteria.init();
        Assert.assertTrue(PostProcessorTestUtils.isEqualCollections(rule.getDefinition().getCriteria(),
                                                                    Collections.singletonList(criteria),
                                                                    "patternPredicate"));
        Assert.assertNull(rule.getDefinition().getDeleteColumns());
        Assert.assertNull(rule.getDefinition().getNewColumns());
        Assert.assertNull(rule.getDefinition().getNewFileName());
        Assert.assertNull(rule.getDefinition().getUpdatedValues());
    }

    private void validateThirdRule(final Rule rule) {
        Assert.assertEquals(rule.getCsv(), "LUVehicleStatus");
        Assert.assertEquals(rule.getDefinition().getOperation(), REMOVE_ROWS);
        Criteria criteria = new Criteria();
        criteria.setColumn("VehicleStatusID");
        criteria.setCondition(Condition.EQ);
        criteria.setValues(Collections.singleton("49"));
        criteria.init();
        Assert.assertEquals(rule.getDefinition().getCriteria(), Collections.singletonList(criteria));
        Assert.assertNull(rule.getDefinition().getDeleteColumns());
        Assert.assertNull(rule.getDefinition().getNewColumns());
        Assert.assertNull(rule.getDefinition().getNewFileName());
        Assert.assertNull(rule.getDefinition().getUpdatedValues());
    }

    private void validateSecondRule(final Rule rule) {
        Assert.assertEquals(rule.getCsv(), "VehicleStatus");
        Assert.assertEquals(rule.getDefinition().getOperation(), REMOVE_ROWS);
        Criteria criteria = new Criteria();
        criteria.setColumn("VehicleStatusID");
        criteria.setCondition(Condition.EQ);
        criteria.setValues(Collections.singleton("49"));
        criteria.init();
        Assert.assertEquals(rule.getDefinition().getCriteria(), Collections.singletonList(criteria));
        Assert.assertNull(rule.getDefinition().getDeleteColumns());
        Assert.assertNull(rule.getDefinition().getNewColumns());
        Assert.assertNull(rule.getDefinition().getNewFileName());
        Assert.assertNull(rule.getDefinition().getUpdatedValues());
    }

    private static void validateFirstRule(final Rule rule) {
        Assert.assertEquals(rule.getCsv(), "Incentive");
        Assert.assertEquals(rule.getDefinition().getOperation(), REMOVE_ROWS);
        Criteria criteria = new Criteria();
        criteria.setColumn("Source");
        criteria.setCondition(Condition.EQ);
        criteria.setValues(Collections.singleton("Financial Institution"));
        criteria.init();
        Assert.assertEquals(rule.getDefinition().getCriteria(), Collections.singletonList(criteria));
        Assert.assertNull(rule.getDefinition().getDeleteColumns());
        Assert.assertNull(rule.getDefinition().getNewColumns());
        Assert.assertNull(rule.getDefinition().getNewFileName());
        Assert.assertNull(rule.getDefinition().getUpdatedValues());
    }
}
