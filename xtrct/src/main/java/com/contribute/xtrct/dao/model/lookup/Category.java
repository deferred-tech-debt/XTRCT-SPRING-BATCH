package com.contribute.xtrct.dao.model.lookup;

import java.io.Serializable;

/**
 * POJO holding information representing an incentive's category in the database
 */
public class Category implements Serializable {

    private int id;
    private String group;
    private String description;

    public Category(Integer id, String group, String description) {
        this.id = id;
        this.group = group;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getGroup() {
        return group;
    }

    public String getDescription() {
        return description;
    }
}
