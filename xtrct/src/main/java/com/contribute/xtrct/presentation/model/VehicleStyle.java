package com.contribute.xtrct.presentation.model;

import com.chromedata.commons.dsvio.DSVColumn;
import com.chromedata.commons.identity.IdentityGenerator;
import com.chromedata.commons.identity.utils.IdentityGenerators;

public class VehicleStyle implements Comparable<VehicleStyle> {

    private static final IdentityGenerator IDENTITY = IdentityGenerators.createIdentityGenerator(VehicleStyle.class);

    @DSVColumn(header = "Acode", qualified = false)
    private String acode;
    @DSVColumn(header = "StyleID", qualified = false)
    private Integer styleID;


    public VehicleStyle() {
    }

    /**
     * Getter method to return the acode.
     * @return acode
     */

    public String getAcode() {
        return acode;
    }

    /**
     * Setter method to set teh acode.
     * @param acode
     */
    public void setAcode(String acode) {
        this.acode = acode;
    }

    /**
     * Getter method to return the styleID.
     * @return styleID
     */
    public Integer getStyleID() {
        return styleID;
    }

    /**
     * Setter method to set the styleID.
     * @param styleID
     */

    public void setStyleID(Integer styleID) {
        this.styleID = styleID;
    }


    @Override
    public int compareTo(VehicleStyle vehicleDetail) {
        if (this == vehicleDetail) return 0;

        int comparison = (this.getAcode().compareTo(vehicleDetail.getAcode()));
        if (comparison != 0) return comparison;

        return (this.styleID.compareTo(vehicleDetail.getStyleID()));
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
