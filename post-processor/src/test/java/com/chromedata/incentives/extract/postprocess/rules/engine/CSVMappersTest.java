package com.chromedata.incentives.extract.postprocess.rules.engine;

import com.chromedata.incentives.extract.postprocess.rules.model.Mapping;
import com.chromedata.incentives.extract.postprocess.rules.model.Relation;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CSVMappersTest {

    @Test
    @SuppressWarnings("unchecked")
    public void loadCSVRelationsTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method loadCSVRelationsMethod = CSVMappers.class.getDeclaredMethod("loadCSVRelations");
        loadCSVRelationsMethod.setAccessible(true);
        List<Relation> csvRelations = (List<Relation>) loadCSVRelationsMethod.invoke(null);
        Assert.assertEquals(csvRelations.size(), 8);
        for (Relation relation : csvRelations) {
            switch (relation.getCsv()) {
                case "Incentive":
                    validateIncentiveMapping(relation.getMapping());
                    break;
                case "ValueVariation":
                    validateValueVariationMapping(relation.getMapping());
                    break;
                case "ProgramValue":
                    validateProgramValueMapping(relation.getMapping());
                    break;
                case "Term":
                    validateTermMapping(relation.getMapping());
                    break;
                case "VehicleIncentive":
                    validateVehicleIncentiveMapping(relation.getMapping());
                    break;
                case "Vehicle":
                    validateVehicleMapping(relation.getMapping());
                    break;
                case "LURegion":
                    validateLURegionMapping(relation.getMapping());
                    break;
                case "VehicleStatus":
                    validateVehicleStatusMapping(relation.getMapping());
                    break;
            }
        }
    }

    @Test
    public void getRelationTest() {
        Optional<Relation> optionalRelation = CSVMappers.getRelation("Incentive");
        Assert.assertTrue(optionalRelation.isPresent());
        validateIncentiveMapping(optionalRelation.get().getMapping());
    }

    private void validateVehicleStatusMapping(final List<Mapping> mappings) {
        Assert.assertEquals(1, mappings.size());
        Assert.assertEquals("Incentive", mappings.get(0).getCsv());
        Assert.assertEquals("IncentiveID", mappings.get(0).getMappingColumn());
        Assert.assertEquals("IncentiveID", mappings.get(0).getParentColumn());
    }

    private void validateLURegionMapping(final List<Mapping> mappings) {
        Assert.assertEquals(1, mappings.size());
        Assert.assertEquals("RegionDetail", mappings.get(0).getCsv());
        Assert.assertEquals("RegionID", mappings.get(0).getMappingColumn());
        Assert.assertEquals("RegionID", mappings.get(0).getParentColumn());
    }

    private void validateVehicleMapping(final List<Mapping> mappings) {
        Assert.assertEquals(1, mappings.size());
        Assert.assertEquals("VehicleStyle", mappings.get(0).getCsv());
        Assert.assertEquals("Acode", mappings.get(0).getMappingColumn());
        Assert.assertEquals("Acode", mappings.get(0).getParentColumn());
    }

    private void validateValueVariationMapping(final List<Mapping> mappings) {
        Assert.assertEquals(1, mappings.size());
        Assert.assertEquals("ProgramValue", mappings.get(0).getCsv());
        Assert.assertEquals("ValueVariationID", mappings.get(0).getMappingColumn());
        Assert.assertEquals("ValueVariationID", mappings.get(0).getParentColumn());
    }

    private void validateIncentiveMapping(final List<Mapping> mappings) {
        Assert.assertEquals(9, mappings.size());
        final List<String> incentiveIDCSVMapping = Stream.of("VehicleIncentive", "DeliveryType",
                                                             "Institution", "ProgramRule",
                                                             "ValueVariation", "VehicleStatus")
                                                         .collect(Collectors.toList());
        Assert.assertTrue(
            mappings.stream().filter(mapping -> incentiveIDCSVMapping.contains(mapping.getCsv()))
                    .allMatch(mapping ->
                                  "IncentiveID".equals(mapping.getParentColumn()) &&
                                  "IncentiveID".equals(mapping.getMappingColumn())
                    )
        );

        Assert.assertTrue(
            mappings.stream().filter(mapping -> "Stackability".equals(mapping.getCsv()))
                    .allMatch(mapping ->
                                  Arrays.asList("SignatureHistoryID", "SignatureID").contains(mapping.getParentColumn()) &&
                                  Arrays.asList("SignatureHistoryID", "ToSignatureID").contains(mapping.getMappingColumn())
                    )
        );

        Assert.assertTrue(
            mappings.stream().filter(mapping -> "LURegion".equals(mapping.getCsv()))
                    .allMatch(mapping ->
                                  "RegionID".equals(mapping.getParentColumn()) &&
                                  "RegionID".equals(mapping.getMappingColumn())
                    )
        );
    }

    private void validateVehicleIncentiveMapping(final List<Mapping> mappings) {
        Assert.assertEquals(1, mappings.size());
        Assert.assertEquals("Vehicle", mappings.get(0).getCsv());
        Assert.assertEquals("Acode", mappings.get(0).getMappingColumn());
        Assert.assertEquals("Acode", mappings.get(0).getParentColumn());
    }

    private void validateTermMapping(final List<Mapping> mappings) {
        Assert.assertEquals(1, mappings.size());
        Assert.assertEquals("MileageRestriction", mappings.get(0).getCsv());
        Assert.assertEquals("TermID", mappings.get(0).getMappingColumn());
        Assert.assertEquals("TermID", mappings.get(0).getParentColumn());
    }

    private void validateProgramValueMapping(final List<Mapping> mappings) {
        Assert.assertEquals(1, mappings.size());
        Assert.assertEquals("Term", mappings.get(0).getCsv());
        Assert.assertEquals("ProgramValueID", mappings.get(0).getMappingColumn());
        Assert.assertEquals("ProgramValueID", mappings.get(0).getParentColumn());
    }
}
