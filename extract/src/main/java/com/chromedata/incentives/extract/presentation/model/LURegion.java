package com.chromedata.incentives.extract.presentation.model;

/**
 * Created with IntelliJ IDEA.
 * User: abhishek.mundewal
 * Date: 11/27/13
 * Time: 6:01 AM
 * To change this template use File | Settings | File Templates.
 */

import com.chromedata.commons.dsvio.DSVColumn;
import com.chromedata.commons.identity.IdentityGenerator;
import com.chromedata.commons.identity.utils.IdentityGenerators;

/**
 * Model class for Region.
 */

public class LURegion implements Comparable<LURegion> {

    private static final IdentityGenerator IDENTITY = IdentityGenerators.createIdentityGenerator(LURegion.class);

    @DSVColumn(header = "RegionID", qualified = false)
    private Integer geographyID;
    @DSVColumn(header = "Country", qualified = false)
    private String country;
    @DSVColumn(header = "Description")
    private String description;



    public LURegion() {
    }

    /**
     * Getter method to return the geographyID.
     * @return
     */
    public Integer getGeographyID() {
        return geographyID;
    }

    /**
     * Setter method to set the geographyID.
     * @param geographyID
     */

    public void setGeographyID(Integer geographyID) {
        this.geographyID = geographyID;
    }

    /**
     * Getter method to return the description.
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter method to set the description.
     * @param description
     */

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter method to return the Country.
     * @return
     */

    public String getCountry() {
        return country;
    }

    /**
     * Setter method to set the Country.
     * @param country
     */

    public void setCountry(String country) {
        this.country = country;
    }


    @Override
    public int compareTo(LURegion region) {
        if (region == this) return 0;

        int comparison = this.getGeographyID().compareTo(region.getGeographyID());
        if (comparison != 0) return comparison;

        comparison = this.getDescription().compareTo(region.getDescription());
        if (comparison != 0) return comparison;

        return (this.getCountry().compareTo(region.getCountry()));
    }

    @Override
    public boolean equals(Object o) {
        return IDENTITY.areEqual(this, o);
    }

    @Override
    public int hashCode() {
        return IDENTITY.getHashCode(this);
    }

    @Override
    public String toString() {
        return IDENTITY.toString(this);
    }
}
