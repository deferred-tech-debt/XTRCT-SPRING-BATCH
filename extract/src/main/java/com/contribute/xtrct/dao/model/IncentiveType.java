package com.contribute.xtrct.dao.model;

/**
 * Represents an incentive type in the database.  This representation is designed to carry a single translation.
 */
public class IncentiveType {

    private String code;
    private String description;

    public IncentiveType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
