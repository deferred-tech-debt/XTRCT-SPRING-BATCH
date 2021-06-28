package com.contribute.xtrct.presentation.model;

import com.chromedata.commons.dsvio.DSVColumn;
import com.chromedata.commons.identity.IdentityGenerator;
import com.chromedata.commons.identity.utils.IdentityGenerators;

public class VehicleIncentive {

    private static final IdentityGenerator IDENTITY = IdentityGenerators.createIdentityGenerator(VehicleIncentive.class);

    @DSVColumn(header = "IncentiveID", qualified = false)
    private Integer incentiveID;
    @DSVColumn(header = "Acode", qualified = false)
    private String Acode;



    public Integer getIncentiveID() {
        return incentiveID;
    }


    public void setIncentiveID(Integer incentiveID) {
        this.incentiveID = incentiveID;
    }


    public String getAcode() {
        return Acode;
    }


    public void setAcode(String acode) {
        Acode = acode;
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
