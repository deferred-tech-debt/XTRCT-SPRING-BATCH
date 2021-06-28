package com.contribute.xtrct.dao.model.lookup;

import java.io.Serializable;

/**
 * POJO holding information representing a vehicle's division in the database
 */
public class Division implements Serializable {

    private String code;
    private String description;
    private String manufacturerCode;

    public Division(String code, String description, String manufacturerCode) {
        this.code = code;
        this.description = description;
        this.manufacturerCode = manufacturerCode;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public String getManufacturerCode() {
        return manufacturerCode;
    }
}
