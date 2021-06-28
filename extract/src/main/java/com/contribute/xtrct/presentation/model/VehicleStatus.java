package com.contribute.xtrct.presentation.model;

import com.chromedata.commons.dsvio.DSVColumn;
import com.chromedata.commons.identity.IdentityGenerator;
import com.chromedata.commons.identity.utils.IdentityGenerators;

/**
 * Created with IntelliJ IDEA.
 * User: abhishek.mundewal
 * Date: 1/29/14
 * Time: 2:37 AM
 * To change this template use File | Settings | File Templates.
 */
public class VehicleStatus implements Comparable<VehicleStatus> {

    private static final IdentityGenerator IDENTITY = IdentityGenerators.createIdentityGenerator(VehicleStatus.class);

    @DSVColumn(header = "IncentiveID", qualified = false)
    private Integer incentiveID;
    @DSVColumn(header = "Status")
    private String status;
    @DSVColumn(header = "VehicleStatusID", qualified = false)
    private Integer id;

    public Integer getIncentiveID() {
        return incentiveID;
    }

    public void setIncentiveID(Integer incentiveID) {
        this.incentiveID = incentiveID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
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


    @Override
    public int compareTo(VehicleStatus vehicleStatus) {
        if (vehicleStatus == this) return 0;

        int comparison = this.getIncentiveID().compareTo(vehicleStatus.getIncentiveID());
        if (comparison != 0) return comparison;
        comparison = this.getId().compareTo(vehicleStatus.getId());
        if(comparison != 0) return comparison;
        return this.getStatus().compareTo(vehicleStatus.getStatus());
    }
}
