package com.contribute.xtrct.postprocess.rules.engine;

import com.contribute.xtrct.postprocess.ExtractVersion;
import com.contribute.xtrct.postprocess.rules.model.Criteria;
import com.contribute.xtrct.postprocess.rules.model.Definition;
import com.contribute.xtrct.postprocess.rules.model.Rule;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Rule Engine responsible for parsing the rules for given extract version. Initialize the rule's criteria and execute each rule on extracted CSV files present in Temp Dir.
 */
public class RuleEngine {

    private static final Logger LOG = LogManager.getLogger(RuleEngine.class);
    private static final ObjectMapper MAPPER = new ObjectMapper(new YAMLFactory());

    private final ExtractVersion extractVersion;
    private final File unZipExtractDir;
    private List<Rule> rules;

    public RuleEngine(final ExtractVersion extractVersion, final File unZipExtractDir) throws IOException {
        this.extractVersion = extractVersion;
        this.unZipExtractDir = unZipExtractDir;
        this.init();
    }

    private void init() throws IOException {
        this.rules = this.parseRules();
        // Initialize the rule's criteria
        this.rules.stream()
                  .map(Rule::getDefinition)
                  .map(Definition::getCriteria)
                  .filter(Objects::nonNull)
                  .flatMap(Collection::stream)
                  .forEach(Criteria::init);
    }

    /**
     *  Parse the rules extract version
     * @return list of rules parsed
     * @throws IOException IOException
     */
    private List<Rule> parseRules() throws IOException {
        try (InputStream ymlResourceStream = this.getClass().getClassLoader().getResourceAsStream("rules/" + extractVersion.getRuleYML())) {
            return Arrays.asList(MAPPER.readValue(ymlResourceStream, Rule[].class));
        }
    }

    /**
     * Execute the rule one by one
     * @throws IOException IOException
     */
    public void execute() throws IOException {
        for (Rule rule : this.rules) {
            LOG.debug("Executing Rule {}", rule);
            CSVRuleProcessor csvRuleProcessor = new CSVRuleProcessor(unZipExtractDir, rule);
            csvRuleProcessor.process();
        }
    }
}
