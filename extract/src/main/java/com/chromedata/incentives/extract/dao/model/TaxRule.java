package com.chromedata.incentives.extract.dao.model;

/**
 * Represents an incentive's tax rule in the database
 */
public class TaxRule {

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
