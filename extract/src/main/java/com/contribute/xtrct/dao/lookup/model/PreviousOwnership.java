package com.contribute.xtrct.dao.lookup.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Display class modeling a previous ownership response object
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PreviousOwnership {

    @XmlAttribute
    private int id;
    @XmlElement(name = "Description")
    private String description;

    // Private no-arg constructor required by jax-b
    private PreviousOwnership() {
    }

    public PreviousOwnership(int id, String description) {
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
