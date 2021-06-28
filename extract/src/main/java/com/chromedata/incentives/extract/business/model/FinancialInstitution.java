package com.chromedata.incentives.extract.business.model;

/**
 * Represents a financial institution which incentives are associated with
 */
public class FinancialInstitution {

    private int id;
    private String description;

    public FinancialInstitution(Integer id, String description) {
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
