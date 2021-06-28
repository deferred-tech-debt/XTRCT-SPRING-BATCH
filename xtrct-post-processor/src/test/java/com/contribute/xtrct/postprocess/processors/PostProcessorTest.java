package com.contribute.xtrct.postprocess.processors;

import com.chromedata.commons.dsvio.core.DSVReader;
import com.contribute.xtrct.postprocess.ExtractVersion;
import com.contribute.xtrct.postprocess.rules.engine.CSVFormatter;
import com.contribute.xtrct.postprocess.rules.engine.CSVMappers;
import com.contribute.xtrct.postprocess.rules.model.Column;
import com.contribute.xtrct.postprocess.rules.model.Criteria;
import com.contribute.xtrct.postprocess.rules.model.DataValue;
import com.contribute.xtrct.postprocess.rules.model.Definition;
import com.contribute.xtrct.postprocess.rules.model.Rule;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipFile;

public class PostProcessorTest {

    private static final ObjectMapper MAPPER = new ObjectMapper(new YAMLFactory());
    private static final String TEMP_EXTRACT = "_temp_extract";
    private static final String CSV_EXTENSION = ".csv";

    @org.junit.Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();
    private File outputZipLocation = null;
    private ZipFile inZip = null;

    @Before
    public void init() throws IOException, URISyntaxException {
        this.outputZipLocation = temporaryFolder.getRoot();
        this.inZip = new ZipFile(Paths.get(this.getClass().getClassLoader().getResource("csv/ConsumerExtract_3.0_en_US.zip").toURI()).toFile());
    }

    @After
    public void testTempDirectoryDeleted() {
        final Path    path            = Paths.get(outputZipLocation.getPath(), TEMP_EXTRACT);
        final boolean isTempDirExists = Files.exists(path);
        Assert.assertFalse(String.format("Temp Directory with path %s still exist !!!", path), isTempDirExists);
    }

    @Test
    public void testProcess() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final List<ExtractVersion> requestedExtractVersions = Collections.singletonList(ExtractVersion.TWO_DOT_ONE);
        try (PostProcessor postProcessor = new PostProcessor(inZip, outputZipLocation, requestedExtractVersions)) {
            for (ExtractVersion extractVersion : ExtractVersion.getAllInReverseOrder()) {
                postProcessor.process(extractVersion);
                validatePostProcessedExtract(extractVersion);

                final String generatedZipName     = getGeneratedZipName(postProcessor, extractVersion);
                final Path   generatedZipFilePath = Paths.get(outputZipLocation.getPath(), generatedZipName);
                if (requestedExtractVersions.contains(extractVersion)) {
                    Assert.assertTrue(String.format("ZipFile %s not generated in post processor", generatedZipFilePath),
                                      Files.exists(generatedZipFilePath));
                }
            }
        }
    }

    private String getGeneratedZipName(final PostProcessor postProcessor,
                                       final ExtractVersion extractVersion) throws NoSuchMethodException, InvocationTargetException,
                                                                                   IllegalAccessException {
        Method createZipFileNameMethod = PostProcessor.class.getDeclaredMethod("createZipFileName", ExtractVersion.class);
        createZipFileNameMethod.setAccessible(true);
        return (String) createZipFileNameMethod.invoke(postProcessor, extractVersion);
    }

    private List<Rule> collectRules(final ExtractVersion extractVersion) throws IOException {
        final List<Rule> rules = PostProcessorTest.parseRules(extractVersion);
        rules.stream()
             .map(Rule::getDefinition)
             .map(Definition::getCriteria)
             .filter(Objects::nonNull)
             .flatMap(Collection::stream)
             .forEach(Criteria::init);
        return rules;
    }

    private void validatePostProcessedExtract(final ExtractVersion extractVersion) throws IOException {
        final List<Rule> rules = collectRules(extractVersion);
        for (Rule rule : rules) {
            CSVMappers.updateMapping(rule);
            CSVFormatter.updateMapping(rule);
            validateRule(rule);
        }
    }

    private static List<Rule> parseRules(ExtractVersion extractVersion) throws IOException {
        try (InputStream ymlResourceStream = PostProcessorTest.class.getClassLoader().getResourceAsStream("rules/" + extractVersion.getRuleYML())) {
            return Arrays.asList(MAPPER.readValue(ymlResourceStream, Rule[].class));
        }
    }

    private void validateRule(final Rule rule) throws IOException {
        switch (rule.getDefinition().getOperation()) {
            case REMOVE_FILE:
                validateCSVExistence(rule.getCsv(), false);
                break;
            case RENAME_FILE:
                validateCSVExistence(rule.getCsv(), false);
                validateCSVExistence(rule.getDefinition().getNewFileName(), true);
                break;
            case REMOVE_COLUMNS:
                validateCSVColumnsExistence(rule.getCsv(), rule.getDefinition().getDeleteColumns(), false);
                break;
            case RENAME_COLUMNS:
                final List<String> oldColumns = rule.getDefinition().getNewColumns().stream().map(Column::getName).collect(Collectors.toList());
                final List<String> newColumns = rule.getDefinition().getNewColumns().stream().map(Column::getNewName).collect(Collectors.toList());
                validateCSVColumnsExistence(rule.getCsv(), oldColumns, false);
                validateCSVColumnsExistence(rule.getCsv(), newColumns, true);
                break;
            case REMOVE_ROWS:
                validateDeletedCSVRecords(rule);
                break;
            case UPDATE_RECORDS:
                validateUpdatedCSVRecords(rule);
        }
    }

    private void validateCSVExistence(final String csv, final boolean shouldExist) {
        final boolean exists = Files.exists(Paths.get(outputZipLocation.getPath(), TEMP_EXTRACT, csv + CSV_EXTENSION));

        if (shouldExist) {
            Assert.assertTrue(String.format("%s.csv not exist !!!", csv), exists);
        } else {
            Assert.assertFalse(String.format("%s.csv still exist !!!", csv), exists);
        }
    }

    private void validateCSVColumnsExistence(final String csv, final List<String> columns, final boolean shouldExist) throws IOException {
        try (Reader reader = Files.newBufferedReader(Paths.get(outputZipLocation.getPath(), TEMP_EXTRACT, csv + CSV_EXTENSION));
             DSVReader dsvReader = new DSVReader(reader)) {

            final List<String> headers = Arrays.asList(dsvReader.getHeader());
            if (shouldExist) {
                columns.forEach(column -> Assert.assertTrue(String.format("%s.csv does not contain header %s !!!", csv, column),
                                                            headers.contains(column)));
            } else {
                columns.forEach(column -> Assert.assertFalse(String.format("%s.csv still contain header %s !!!", csv, column),
                                                             headers.contains(column)));
            }
        }
    }

    private void validateDeletedCSVRecords(final Rule rule) throws IOException {
        try (Reader reader = Files.newBufferedReader(Paths.get(outputZipLocation.getPath(), TEMP_EXTRACT, rule.getCsv() + CSV_EXTENSION));
             DSVReader dsvReader = new DSVReader(reader)) {

            final List<String>         headers   = Arrays.asList(dsvReader.getHeader());
            final Map<String, Integer> headerMap = headers.stream().collect(Collectors.toMap(Function.identity(), headers::indexOf));
            final Predicate<List<String>> predicate = rule.getDefinition().getCriteria().stream()
                                                          .map(criteria -> criteria.toPredicate(headerMap))
                                                          .reduce(Predicate::or)
                                                          .get();

            boolean  isAllExpectedRecordsDeleted = true;
            String[] line;
            while ((line = dsvReader.readLine()) != null) {
                final List<String> record = Stream.of(line).collect(Collectors.toList());
                if (predicate.test(record)) {
                    isAllExpectedRecordsDeleted = false;
                    break;
                }
            }

            Assert.assertTrue(String.format("All records from %s.csv meeting criteria's %s not deleted !!!",
                                            rule.getCsv(),
                                            rule.getDefinition().getCriteria()), isAllExpectedRecordsDeleted);

        }
    }

    private void validateUpdatedCSVRecords(final Rule rule) throws IOException {

        try (Reader reader = Files.newBufferedReader(Paths.get(outputZipLocation.getPath(), TEMP_EXTRACT, rule.getCsv() + CSV_EXTENSION));
             DSVReader csvReader = new DSVReader(reader)) {

            final List<String>         headers   = Arrays.asList(csvReader.getHeader());
            final Map<String, Integer> headerMap = headers.stream().collect(Collectors.toMap(Function.identity(), headers::indexOf));
            final Predicate<List<String>> predicate = rule.getDefinition().getCriteria().stream()
                                                          .map(criteria -> criteria.toPredicate(headerMap))
                                                          .reduce(Predicate::or)
                                                          .get();
            final List<DataValue> updatedValues = rule.getDefinition().getUpdatedValues();

            String[] line;
            while ((line = csvReader.readLine()) != null) {
                final List<String> record = Stream.of(line).collect(Collectors.toList());
                if (predicate.test(record)) {
                    for (DataValue dataValue : updatedValues) {
                        boolean isDataUpdated = StringUtils.equals(csvReader.getString(dataValue.getColumn()), dataValue.getNewValue());
                        Assert.assertTrue(String.format("Record in %s.csv at line no. %s meeting criteria's %s is not updated !!!",
                                                        rule.getCsv(),
                                                        csvReader.getLinesRead(),
                                                        rule.getDefinition().getCriteria()), isDataUpdated);
                    }
                }
            }
        }
    }
}
