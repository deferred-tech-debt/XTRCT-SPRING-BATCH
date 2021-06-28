package com.contribute.xtrct.dao.lookup.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Display class modeling a geography response object
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class FlexibleGeography {

    @XmlAttribute
    private int id;
    @XmlElement(name = "CountryId")
    private String countryId;
    @XmlElement(name = "Description")
    private String description;

    // Private no-arg constructor required by jax-b
    private FlexibleGeography() {
    }

    public FlexibleGeography(int id, String countryId, String description) {
        this.id = id;
        this.countryId = countryId;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getCountryId() {
        return countryId;
    }

    public String getDescription() {
        return description;
    }
}
