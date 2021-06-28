package com.contribute.xtrct.dao.lookup.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Display class modeling a lookup response object in our lookups index
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"name", "links"})
public class Lookup extends Linkable {

    @XmlElement(name = "Name")
    private String name;

    // Default JAXB constructor
    private Lookup() {
    }

    public Lookup(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
