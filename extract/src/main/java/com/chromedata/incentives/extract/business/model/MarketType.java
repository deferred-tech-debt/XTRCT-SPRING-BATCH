package com.chromedata.incentives.extract.business.model;

/**
 * Enum used for filtering incentive market types
 */
public enum MarketType {
    RETAIL(1),
    DEALER(2);

    private int id;

    MarketType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
