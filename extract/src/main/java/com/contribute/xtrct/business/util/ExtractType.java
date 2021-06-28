package com.contribute.xtrct.business.util;

import java.io.File;
import java.util.Locale;

/**
 * Represents the different types of extract outputs and facilitates the naming convention of the extract files
 */
public enum ExtractType {
    CONSUMER("ConsumerExtract"),
    DEALER("DealerExtract");

    private String baseFileName;

    ExtractType(String baseFileName) {
        this.baseFileName = baseFileName;
    }

    /**
     * Gets the extract file for the provided parameters
     * <p>
     * Both extract and batch should utilize this mechanism for retrieving the extract files to ensure the logic for
     * enforcing the extract naming convention remains in a single place.
     *
     * @param extractBaseDirectory The parent directory that houses all of the extract files
     * @param version              The version of this application
     * @param locale               The locale of the extract file
     * @return File representing the requested extract
     */
    public File getFile(File extractBaseDirectory, ApplicationVersion version, Locale locale) {
        String name = baseFileName + "_"
                + version.getMajorVersion() + ApplicationVersion.VERSION_DELIMITER + version.getMinorVersion() + "_"
                + locale.toString();

        return new File(extractBaseDirectory, name);
    }
}
