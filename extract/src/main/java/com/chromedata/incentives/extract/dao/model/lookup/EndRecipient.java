package com.chromedata.incentives.extract.dao.model.lookup;

import java.io.Serializable;

/**
 * POJO holding information representing an incentive's End Recipient in the database
 */
public class EndRecipient implements Serializable {

    private int id;
    private String description;

    public EndRecipient(Integer id, String description) {
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
