package com.chromedata.incentives.extract.presentation.model;

import com.chromedata.commons.dsvio.DSVColumn;
import com.chromedata.commons.identity.IdentityGenerator;
import com.chromedata.commons.identity.utils.IdentityGenerators;

public class VehicleVIN implements Comparable<VehicleVIN> {

    private static final IdentityGenerator IDENTITY = IdentityGenerators.createIdentityGenerator(VehicleVIN.class);

    @DSVColumn(header = "Acode", qualified = false)
    private String acode;
    @DSVColumn(header = "VIN", qualified = false)
    private String vin;


    public VehicleVIN() {
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
     * Getter method to return the VIN.
     * @return vin
     */
    public String getVIN() {
        return vin;
    }

    /**
     * Setter method to set the VIN.
     * @param vin
     */

    public void setVIN(String vin) {
        this.vin = vin;
    }


    @Override
    public int compareTo(VehicleVIN vehicleDetail) {
        if (this == vehicleDetail) return 0;

        int comparison = (this.getAcode().compareTo(vehicleDetail.getAcode()));
        if (comparison != 0) return comparison;

        return (this.vin.compareTo(vehicleDetail.getVIN()));
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
