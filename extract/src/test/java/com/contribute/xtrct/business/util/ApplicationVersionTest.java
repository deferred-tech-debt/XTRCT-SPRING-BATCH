package com.contribute.xtrct.business.util;


import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class ApplicationVersionTest {

    @Test
    public void shouldParse4PartVersionStrings() throws Exception {
        ApplicationVersion version = ApplicationVersion.parse("1.2.3.100");

        assertThat(version.getMajorVersion()).isEqualTo("1");
        assertThat(version.getMinorVersion()).isEqualTo("2");
        assertThat(version.getPatchVersion()).isEqualTo("3");
        assertThat(version.getBuildNumber()).isEqualTo("100");
    }

    @Test
    public void shouldParse3PartVersionStrings() throws Exception {
        ApplicationVersion version = ApplicationVersion.parse("1.2.100");

        assertThat(version.getMajorVersion()).isEqualTo("1");
        assertThat(version.getMinorVersion()).isEqualTo("2");
        assertThat(version.getBuildNumber()).isEqualTo("100");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForInvalidVersionString() throws Exception {
        ApplicationVersion.parse("1.2.3.2.100");
    }
}
