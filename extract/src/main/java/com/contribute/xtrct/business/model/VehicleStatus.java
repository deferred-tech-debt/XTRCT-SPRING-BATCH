package com.contribute.xtrct.business.model;

public class VehicleStatus {

    private int id;
    private String description;

    public VehicleStatus(final int id, final String description) {
        this.id = id;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
}
