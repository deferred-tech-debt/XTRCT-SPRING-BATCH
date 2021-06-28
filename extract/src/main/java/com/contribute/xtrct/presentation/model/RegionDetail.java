package com.contribute.xtrct.presentation.model;

/**
 * Created with IntelliJ IDEA.
 * User: abhishek.mundewal
 * Date: 11/27/13
 * Time: 5:58 AM
 * To change this template use File | Settings | File Templates.
 */

import com.chromedata.commons.dsvio.DSVColumn;
import com.chromedata.commons.identity.IdentityGenerator;
import com.chromedata.commons.identity.utils.IdentityGenerators;

/**
 * Model class RegionDetail.
 */
public class RegionDetail implements Comparable<RegionDetail> {

    private static final IdentityGenerator IDENTITY = IdentityGenerators.createIdentityGenerator(RegionDetail.class);

    @DSVColumn(header = "RegionID", qualified = false)
    private Integer geographyID;
    @DSVColumn(header = "ZipPostalPattern")
    private String zipPostalPattern;



    public RegionDetail() {
    }

    /**
     * Getter method to return the geographyID.
     * @return geographyID
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
     * Getter method to return the zipPostalPattern.
     * @return
     */

    public String getZipPostalPattern() {
        return zipPostalPattern;
    }

    /**
     * Setter method to set the zipPostalPattern.
     * @param zipPostalPattern
     */

    public void setZipPostalPattern(String zipPostalPattern) {
        this.zipPostalPattern = zipPostalPattern;
    }


    @Override
    public int compareTo(RegionDetail RegionDetail) {

        if (RegionDetail == this) return 0;

        int comparison = RegionDetail.getGeographyID().compareTo(this.getGeographyID());
        return ((comparison != 0) ? comparison : RegionDetail.getZipPostalPattern().compareTo(this.getZipPostalPattern()));
    }

    @Override
    public boolean equals(Object obj) {
        return IDENTITY.areEqual(this, obj);
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
