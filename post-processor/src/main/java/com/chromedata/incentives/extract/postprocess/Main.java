package com.chromedata.incentives.extract.postprocess;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.chromedata.incentives.extract.postprocess.processors.PostProcessor;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;

/**
 * Main entry point to application
 */
public class Main {

    private static final Logger LOG = LogManager.getLogger(Main.class);

    /**
     * Kicks off the post processing of the extract files
     * @param args Arguments defined by CommandLineArgs class using JCommander
     */
    public static void main(String... args) {
        final Instant mainStartTime = Instant.now();
        final CommandLineArgs commandLine = Main.parseCommandLine(args);
        final List<ExtractVersion> requestedExtractVersions = commandLine.getExtractVersions();
        final ExtractVersion leastRequestedVersion = requestedExtractVersions.stream().min(Comparator.comparing(ExtractVersion::ordinal)).get();

        try(PostProcessor postProcessor = new PostProcessor(commandLine.getExtractZip(), commandLine.getOutPath(), requestedExtractVersions)) {
            for(ExtractVersion extractVersion : ExtractVersion.getAllInReverseOrder()) {

                // condition to validate if this version is required to process
                if(extractVersion.ordinal() < leastRequestedVersion.ordinal()) {
                    break;
                }

                LOG.info("Executing post processor {}", extractVersion.getVersion());

                final Instant startTime = Instant.now();
                postProcessor.process(extractVersion);
                final Instant endTime = Instant.now();
                final Duration executionTime = Duration.between(startTime, endTime);

                LOG.info("Post processor {} completed with execution time: {}",
                         extractVersion.getVersion(),
                         DurationFormatUtils.formatDuration(executionTime.toMillis(), "H:mm:ss", true));
            }

        } catch (Exception e) {
            LOG.fatal("Failed to process extract: " + e.getMessage(), e);
            System.exit(1);
        }

        final Instant mainEndTime = Instant.now();
        final Duration mainExecutionTime = Duration.between(mainStartTime, mainEndTime);

        LOG.info("All post processors completed with execution time: {}",
                 DurationFormatUtils.formatDuration(mainExecutionTime.toMillis(), "H:mm:ss", true));
    }

    private static CommandLineArgs parseCommandLine(String... args) {
        CommandLineArgs commandLine = new CommandLineArgs();
        JCommander commandLineParser = new JCommander(commandLine);
        commandLineParser.setProgramName("incentives-extract-post-processor");
        try {
            commandLineParser.parse(args);
        } catch (ParameterException e) {
            LOG.fatal("Failed to parse command line arguments", e);
            commandLineParser.usage();
            System.exit(1);
        }

        if (commandLine.isHelp()) {
            commandLineParser.usage();
            System.exit(0);
        }

        return commandLine;
    }
}
