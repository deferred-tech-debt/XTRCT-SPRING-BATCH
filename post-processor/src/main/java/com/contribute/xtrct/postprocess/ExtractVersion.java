package com.contribute.xtrct.postprocess;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Constant representing the available extract versions we can generate off the latest extract version
 */
public enum ExtractVersion {

    // The order of versions must always be lower to higher, otherwise it will affect the output
    TWO_DOT_ZERO("2.0", "post_processor_2.0"),
    TWO_DOT_ONE("2.1", "post_processor_2.1"),
    TWO_DOT_TWO("2.2", "post_processor_2.2"),
    THREE_DOT_ZERO("3.0", "post_processor_3.0");

    private String version;
    private String ruleYML;

    ExtractVersion(String version, String ruleYML) {
        this.version = version;
        this.ruleYML = ruleYML;
    }

    public String getVersion() {
        return version;
    }

    public String getRuleYML() {
        return ruleYML+".yml";
    }

    /**
     * Lookup the ExtractVersion for the given string representation.  Lookup is performed by the corresponding numerical version
     *
     * @param version Numeric or string representation of the extract version
     * @return ExtractVersion representing the string version passed in
     */
    public static ExtractVersion getVersion(String version) {
        return Arrays.stream(ExtractVersion.values())
                .filter(extractVersion -> extractVersion.version.equals(version))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Unrecognized version " + version
                        + ".  Available versions are "
                        + Arrays.stream(values())
                            .map(extractVersion -> extractVersion.version)
                            .collect(Collectors.joining(", "))));
    }

    /**
     * Get All Extract Version in reverse order i.e. higher to lower
     * @return array of Extract Version
     */
    public static ExtractVersion [] getAllInReverseOrder() {
        final ExtractVersion[] values = values();
        ArrayUtils.reverse(values);
        return values;
    }
}
