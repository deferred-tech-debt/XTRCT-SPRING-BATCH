package com.chromedata.incentives.extract.dao.model.lookup;

import java.io.Serializable;

/**
 * POJO holding information representing an incentive's previous ownership in the database
 */
public class PreviousOwnership implements Serializable {

    private int id;
    private String description;

    public PreviousOwnership(Integer id, String description) {
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
