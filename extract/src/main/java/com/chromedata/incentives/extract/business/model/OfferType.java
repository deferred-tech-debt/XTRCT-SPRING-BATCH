package com.chromedata.incentives.extract.business.model;

/**
 * Enum used for filtering incentive offer types
 */
public enum OfferType {
    PUBLIC(22);

    private int id;

    OfferType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
