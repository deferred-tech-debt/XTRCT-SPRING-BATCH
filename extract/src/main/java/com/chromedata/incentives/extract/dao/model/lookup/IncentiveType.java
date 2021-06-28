package com.chromedata.incentives.extract.dao.model.lookup;

import java.io.Serializable;

/**
 * POJO holding information representing an incentive's type in the database
 */
public class IncentiveType implements Serializable {

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
