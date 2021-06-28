package com.contribute.xtrct.business.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a version of the Extract/Batch
 */
public class ApplicationVersion {

    public static final String VERSION_DELIMITER = ".";
    private static final String ESCAPE_REGULAR_EXPRESSION = "\\";

    private String majorVersion;
    private String minorVersion;
    private String patchVersion;
    private String buildNumber;

    private ApplicationVersion(String versionString) {
        parseVersionString(versionString);
    }

    /**
     * Attempts to parse the provided string into a new ApplicationVersion instance.
     * <p>
     * A valid version string must:
     * 1) use "." to separate different version designators
     * 2) contain at least 3 components
     * 3) contain no more than 4 components
     * <p>
     * Example: <major>.<minor>.<patch>.<build number> -> 1.3.2.100
     *
     * @param versionString String to parse
     * @return new instance of an ApplicationVersion
     */
    public static ApplicationVersion parse(String versionString) {
        return new ApplicationVersion(versionString);
    }

    public String getMajorVersion() {
        return majorVersion;
    }

    public String getMinorVersion() {
        return minorVersion;
    }

    public String getPatchVersion() {
        return patchVersion;
    }

    public String getBuildNumber() {
        return buildNumber;
    }

    private void parseVersionString(String versionString) {
        String[] versionParts = versionString.split(ESCAPE_REGULAR_EXPRESSION + VERSION_DELIMITER);

        switch (versionParts.length) {
            case 3:
                majorVersion = versionParts[0];
                minorVersion = versionParts[1];
                buildNumber = versionParts[2];
                break;
            case 4:
                majorVersion = versionParts[0];
                minorVersion = versionParts[1];
                patchVersion = versionParts[2];
                buildNumber = versionParts[3];
                break;
            default:
                throw new IllegalArgumentException("Cannot parse version string: " + versionString + ".  Invalid number of parts: " + versionParts.length);
        }
    }

    @Override
    public String toString() {
        List<String> versionList = new ArrayList<>(4);
        versionList.add(majorVersion);
        versionList.add(minorVersion);
        versionList.add(patchVersion);
        versionList.add(buildNumber);

        versionList.removeIf(Objects::isNull);

        return String.join(VERSION_DELIMITER, versionList);
    }
}
