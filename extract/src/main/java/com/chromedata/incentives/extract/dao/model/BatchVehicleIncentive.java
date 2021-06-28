package com.chromedata.incentives.extract.dao.model;

public class BatchVehicleIncentive {

    private int incentiveID;
    private String acode;

    public BatchVehicleIncentive(Integer incentiveID, String acode) {
        this.incentiveID = incentiveID;
        this.acode = acode;
    }

    public int getIncentiveID() {
        return incentiveID;
    }

    public String getAcode() {
        return acode;
    }
}
