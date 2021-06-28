package com.contribute.xtrct.dao.model;

/**
 * Represents a market in the database.  This representation is designed to carry a single translation.
 */
public class Market {

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
