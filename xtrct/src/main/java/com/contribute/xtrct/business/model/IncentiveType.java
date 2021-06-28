package com.contribute.xtrct.business.model;

/**
 * Enum used for filtering incentive types
 */
public enum IncentiveType {
    CASH("CASH"),
    GIFT_AWARD("GA");

    private String code;

    IncentiveType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
