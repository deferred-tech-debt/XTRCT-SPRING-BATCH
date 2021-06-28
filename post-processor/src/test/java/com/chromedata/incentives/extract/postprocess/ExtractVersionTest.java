package com.chromedata.incentives.extract.postprocess;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class ExtractVersionTest {

    @Test
    public void shouldGetExtractVersionByNumber() throws Exception {
        ExtractVersion version = ExtractVersion.getVersion("2.0");

        assertThat(version).isEqualTo(ExtractVersion.TWO_DOT_ZERO);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForInvalidVersion() throws Exception {
        ExtractVersion.getVersion("fail");
    }

    @Test
    public void testGetAllInReverseOrder() {
        boolean errorFlag = false;
        ExtractVersion[] versions = ExtractVersion.getAllInReverseOrder();
        Double maxVersion = Double.valueOf(versions[0].getVersion());
        for(int i=1; i<versions.length; i++) {
            Double version = Double.valueOf(versions[i].getVersion());
            if(maxVersion <= version) {
                errorFlag = true;
                break;
            }
            maxVersion = version;
        }
        assertThat(errorFlag).isEqualTo(false);
    }
}
