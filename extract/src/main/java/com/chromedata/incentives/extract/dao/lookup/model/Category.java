package com.chromedata.incentives.extract.dao.lookup.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Display class modeling a category response object
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Category {

    @XmlAttribute
    private int id;
    @XmlElement(name = "Group")
    private String group;
    @XmlElement(name = "Description")
    private String description;

    // Private no-arg constructor required by jax-b
    private Category() {
    }

    public Category(int id, String group, String description) {
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
