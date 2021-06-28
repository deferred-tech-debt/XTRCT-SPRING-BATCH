package com.contribute.xtrct.dao.model;

import java.io.Serializable;

/**
 * Represents a geography id and it's postal codes tied to it
 */
public class BatchGeographiesDetail implements Serializable {

    private int geographyId;
    private String zipPostalPattern;

    public BatchGeographiesDetail(Integer geographyId, String zipPostalPattern) {
        this.geographyId = geographyId;
        this.zipPostalPattern = zipPostalPattern;
    }

    public int getGeographyId() {
        return geographyId;
    }

    public String getZipPostalPattern() {
        return zipPostalPattern;
    }
}
