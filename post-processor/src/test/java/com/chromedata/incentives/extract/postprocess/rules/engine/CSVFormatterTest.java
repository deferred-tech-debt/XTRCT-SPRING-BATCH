package com.chromedata.incentives.extract.postprocess.rules.engine;

import com.chromedata.incentives.extract.postprocess.rules.model.CSVData;
import com.chromedata.incentives.extract.postprocess.rules.model.CSVFormat;
import com.chromedata.incentives.extract.postprocess.rules.model.Format;
import com.chromedata.incentives.extract.postprocess.rules.model.FormatTypes;
import com.chromedata.incentives.extract.postprocess.utils.PostProcessorTestUtils;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CSVFormatterTest {

    private static List<FormatTypes> ENCAPSULATED_FORMAT_TYPE = Arrays.asList(FormatTypes.ENCAPSULATE, FormatTypes.ENCAPSULATE_ELSE);
    private static final String ENCAPSULATED_ELSE_CSV = "Incentive,ProgramRule,ValueVariation,Vehicle,LUCategory,LUDeliveryType,LUDivision,LUEndRecipient,LUGroupAffiliation,LUIncentiveType,LUInstitution,LUMarket,LUPreviousOwnership,LUVehicleStatus,Version";
    private static final String ENCAPSULATED_CSV = "DeliveryType,Institution,MileageRestriction,ProgramValue,RegionDetail,Stackability,TaxRule,Term,VehicleStatus,LURegion";

    @Test
    @SuppressWarnings("unchecked")
    public void loadCSVFormatsTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method loadCSVFormatsMethod = CSVFormatter.class.getDeclaredMethod("loadCSVFormats");
        loadCSVFormatsMethod.setAccessible(true);
        List<CSVFormat> csvFormats = (List<CSVFormat>) loadCSVFormatsMethod.invoke(null);
        Assert.assertEquals(25, csvFormats.size());
        Predicate<Format> encapsulatedPredicate = format -> ENCAPSULATED_FORMAT_TYPE.contains(format.getType());
        Assert.assertEquals(ENCAPSULATED_ELSE_CSV,
                            csvFormats.stream()
                                      .filter(csvFormat -> csvFormat.getFormats()
                                                                    .stream()
                                                                    .filter(encapsulatedPredicate)
                                                                    .findFirst()
                                                                    .get()
                                                                    .getType()
                                                                    .isElseFormat())
                                      .map(CSVFormat::getCsv)
                                      .collect(Collectors.joining(",")));
        Assert.assertEquals(ENCAPSULATED_CSV,
                            csvFormats.stream()
                                      .filter(csvFormat -> !csvFormat.getFormats()
                                                                     .stream()
                                                                     .filter(encapsulatedPredicate)
                                                                     .findFirst()
                                                                     .get()
                                                                     .getType()
                                                                     .isElseFormat())
                                      .map(CSVFormat::getCsv)
                                      .collect(Collectors.joining(",")));
    }

    @Test
    public void encapsulateRecordsTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        CSVData csvData = PostProcessorTestUtils.buildCSVData();

        Method method = CSVFormatter.class.getDeclaredMethod("encapsulateRecords", Set.class, List.class, String.class);
        method.setAccessible(true);
        final List<List<String>> records      = csvData.getRecords().stream().distinct().collect(Collectors.toList());
        int                      expectedSize = records.size();
        final List<String> expectedList = records.stream()
                                                 .map(strings -> Optional.ofNullable(strings.get(2)))
                                                 .map(optStr -> optStr.map(s -> "\"" + s + "\"").get())
                                                 .sorted()
                                                 .collect(Collectors.toList());
        method.invoke(null, Collections.singleton(2), records, "\"");
        Assert.assertEquals(expectedSize, records.size());
        Assert.assertEquals(expectedList, records.stream().map(strings -> strings.get(2)).sorted().collect(Collectors.toList()));
    }

    @Test
    public void formatStringRecordsTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        CSVData csvData = PostProcessorTestUtils.buildCSVData();

        Method method = CSVFormatter.class.getDeclaredMethod("formatStringRecords", Set.class, List.class, String.class);
        method.setAccessible(true);
        final List<List<String>> records      = csvData.getRecords().stream().distinct().collect(Collectors.toList());
        int                      expectedSize = records.size();
        final List<String> expectedList = records.stream()
                                                 .map(strings -> Optional.ofNullable(strings.get(2)))
                                                 .map(optStr -> optStr.map(s -> "'" + s + "'").get())
                                                 .sorted()
                                                 .collect(Collectors.toList());
        method.invoke(null, Collections.singleton(2), records, "'%s'");
        Assert.assertEquals(expectedSize, records.size());
        Assert.assertEquals(expectedList, records.stream().map(strings -> strings.get(2)).sorted().collect(Collectors.toList()));
    }
}
