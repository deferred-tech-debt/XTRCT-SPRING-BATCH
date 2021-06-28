package com.chromedata.incentives.extract.dao.model;

public class BatchGeographyDetail {

    private int geographyId;
    private String zipPostalPattern;

    public BatchGeographyDetail(Integer geographyId, String zipPostalPattern) {
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
