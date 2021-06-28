package com.chromedata.incentives.extract.dao.model.lookup;

import java.io.Serializable;

/**
 * POJO holding information representing an incentive's market type in the database
 */
public class Market implements Serializable {

    private int id;
    private String description;

    public Market(Integer id, String description) {
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
