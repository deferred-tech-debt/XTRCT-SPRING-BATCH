package com.chromedata.incentives.extract.dao.model.lookup;

import java.io.Serializable;

/**
 * POJO holding information representing an incentive's group affiliation in the database
 */
public class GroupAffiliation implements Serializable {

    private int id;
    private String description;

    public GroupAffiliation(Integer id, String description) {
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
