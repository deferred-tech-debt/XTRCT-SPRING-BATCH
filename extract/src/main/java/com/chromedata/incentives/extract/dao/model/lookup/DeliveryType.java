package com.chromedata.incentives.extract.dao.model.lookup;

import java.io.Serializable;

/**
 * POJO holding information representing an incentive's delivery type in the database
 */
public class DeliveryType implements Serializable {

    private int id;
    private String code;
    private String description;

    public DeliveryType(Integer id, String code, String description) {
        this.id = id;
        this.code = code;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
