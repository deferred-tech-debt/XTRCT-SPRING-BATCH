package com.chromedata.incentives.extract.business.model;

/**
 * Represents a DeliveryType which incentives are associated with
 */
public class DeliveryType {

    private int id;
    private String description;

    public DeliveryType(final int id, final String description) {
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
