package com.contribute.xtrct.postprocess;

import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.FileConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.ZipFile;

/**
 * JCommander annotated class used for binding command line arguments into a transfer object
 */
public class CommandLineArgs {
    private static final Logger LOG = LogManager.getLogger(CommandLineArgs.class);

    @Parameter(names = "-extractPath",
               description = "The path to the extract zip.",
               required = true,
               converter = ZipFileConverter.class)
    private ZipFile extractZip;
    @Parameter(names = "-outPath",
               description = "The path to the outputed extract zip.",
               required = true,
               converter = FileConverter.class)
    private File outPath;
    @Parameter(names = "-versions",
               description = "The comma separated versions to generate from the main extract output.",
               required = true,
               converter = ExtractVersionConverter.class)
    private List<ExtractVersion> extractVersions;
    @Parameter(names = "-help", help = true)
    private boolean help;

    public ZipFile getExtractZip() {
        return extractZip;
    }

    public File getOutPath() {
        return outPath;
    }

    public List<ExtractVersion> getExtractVersions() {
        return extractVersions;
    }

    public boolean isHelp() {
        return help;
    }

    public static class ExtractVersionConverter implements IStringConverter<ExtractVersion> {

        public ExtractVersion convert(String versionsStr) {
            return ExtractVersion.getVersion(versionsStr.trim());
        }
    }

    public static class ZipFileConverter implements IStringConverter<ZipFile> {

        public ZipFile convert(String extractZipPath) {

            try {
                return new ZipFile(new File(extractZipPath), StandardCharsets.UTF_8);
            } catch (IOException e) {
                LOG.fatal("Failed to create extract input zip located at {}", extractZipPath, e);
                throw new IllegalStateException(e);
            }
        }
    }
}
