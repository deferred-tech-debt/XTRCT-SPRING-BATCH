package com.chromedata.incentives.extract.postprocess.processors;

import com.chromedata.incentives.extract.postprocess.ExtractVersion;
import com.chromedata.incentives.extract.postprocess.rules.engine.RuleEngine;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * Responsible for unZip the extract sip file to temp dir, execute the rule engine to process the CSV files, Zip them back to required to zip file.
 * The name of new zip file is created from given zip file in input
 */
public class PostProcessor implements Closeable {

    private static final Logger LOG = LogManager.getLogger(PostProcessor.class);
    private static final String TEMP_EXTRACT = "_temp_extract";

    private ZipFile inZip;
    private File outputZipLocation;
    private File unZipExtractDir;
    private List<ExtractVersion> requestedExtractVersions;

    /**
     * Constructor
     *
     * @param inZip             Extract zip file to process
     * @param outputZipLocation Network location to store the newly processed extract file
     * @param requestedExtractVersions List of Extract versions required to generate on disk
     */
    public PostProcessor(ZipFile inZip, File outputZipLocation, List<ExtractVersion> requestedExtractVersions) throws IOException {
        this.inZip = inZip;
        this.outputZipLocation = outputZipLocation;
        this.requestedExtractVersions = requestedExtractVersions;
        init();
    }

    private void init() throws IOException {
        this.unZipExtractDir = unZipExtract();
    }

    /**
     * Processes the input extract version and creates a new extract zip (if present in requested extract versions) of processed files.
     * @param extractVersion Extract version to process
     *
     * @throws IOException If an error occurs while reading from/writing to the extract files
     */
    public void process(ExtractVersion extractVersion) throws IOException {
        RuleEngine ruleEngine = new RuleEngine(extractVersion, unZipExtractDir);
        ruleEngine.execute();

        if(requestedExtractVersions.contains(extractVersion)) {
            Path zipPath = Paths.get(outputZipLocation.getPath(), createZipFileName(extractVersion));
            PostProcessor.deleteIfExist(zipPath);
            zipPostProcessedExtract(zipPath);
            LOG.info("Generated {} extract. Output located at {}", extractVersion.getVersion(), zipPath.toAbsolutePath());
        }
    }

    private static void deleteIfExist(final Path zipPath) throws IOException {
        boolean isDeletedExistZip = Files.deleteIfExists(zipPath);
        if(isDeletedExistZip) {
            LOG.info("Deleted existing zip on path {}", zipPath.toAbsolutePath());
        }
    }

    /**
     * Create the zip file name for given extract version from provided ZIP file name
     * @param extractVersion Extract version to process
     * @return zip file name for given extract version
     */
    private String createZipFileName(final ExtractVersion extractVersion) {
        String zipFileName = new File(this.inZip.getName()).getName();
        String[] splitZipFileName = zipFileName.split("_",3);
        splitZipFileName[1] = extractVersion.getVersion();
        return String.join("_", splitZipFileName);
    }

    /**
        Close the Zip stream and deletes the temp dir. This must be called after all processing
     */
    @Override
    public void close() throws IOException {
        FileUtils.deleteDirectory(this.unZipExtractDir);
        if (inZip != null) {
            inZip.close();
        }
    }

    // Extract each entry in the directory
    private File unZipExtract() throws IOException {
        File unZipExtractDir = new File(outputZipLocation, TEMP_EXTRACT);
        if(unZipExtractDir.exists()) {
            FileUtils.deleteDirectory(unZipExtractDir);
        }
        Files.createDirectory(unZipExtractDir.toPath());

        inZip.stream().forEach(zipEntry -> {
            try (InputStream zipInputStream = inZip.getInputStream(zipEntry)) {
                File extractedFile = new File(unZipExtractDir, zipEntry.getName());
                Files.copy(zipInputStream, extractedFile.toPath());
            } catch (IOException e) {
                LOG.error("Failed to extract the {} extract file", zipEntry.getName(), e);
            }
        });

        return unZipExtractDir;
    }

    private void zipPostProcessedExtract(final Path zipPath) throws IOException {
        Path sourcePath = this.unZipExtractDir.toPath();
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(zipPath));
             Stream<Path> paths = Files.walk(sourcePath)) {

            paths.filter(path -> !Files.isDirectory(path))
                 .forEach(path -> {
                     final ZipEntry zipEntry = new ZipEntry(sourcePath.relativize(path).toString());
                     try {
                         zipOutputStream.putNextEntry(zipEntry);
                         Files.copy(path, zipOutputStream);
                         zipOutputStream.closeEntry();
                     } catch (IOException e) {
                         LOG.error("Unable to zip {}", zipEntry.getName(), e);
                     }
                 });
        }
    }
}
