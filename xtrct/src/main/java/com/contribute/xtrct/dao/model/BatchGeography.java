package com.contribute.xtrct.dao.model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class BatchGeography implements Serializable {

    private int geographyId;
    private String description;
    private String country;

    private List<String> zipPostalPatterns;

    public BatchGeography(Integer geographyId, String description, String country) {
        this.geographyId = geographyId;
        this.description = description;
        this.country = country;
    }

    public int getGeographyId() {
        return geographyId;
    }

    public String getDescription() {
        return description;
    }

    public String getCountry() {
        return country;
    }

    public List<String> getZipPostalPatterns() {
        return zipPostalPatterns;
    }

    public void setZipPostalPatterns(final List<String> zipPostalPatterns) {
        this.zipPostalPatterns = zipPostalPatterns;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BatchGeography that = (BatchGeography) o;
        return geographyId == that.geographyId &&
               Objects.equals(description, that.description) &&
               Objects.equals(country, that.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(geographyId, description, country);
    }
}
