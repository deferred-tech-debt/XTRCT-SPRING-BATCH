package com.contribute.xtrct.dao.model.lookup;

import java.io.Serializable;

/**
 * POJO holding information representing an incentive's Tax Rule in the database
 */
public class TaxRule implements Serializable {

    private int id;
    private String description;

    public TaxRule(Integer id, String description) {
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
