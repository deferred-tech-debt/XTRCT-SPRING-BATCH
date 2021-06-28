package com.chromedata.incentives.extract.dao.lookup.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * This class is used to define a link.  Objects may be composed of only one link (self) or multiple links to support
 * pagination.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Link {

    private String rel;
    private String href;

    // Default JAXB constructor
    private Link() {
    }

    /**
     * Constructor
     *
     * @param linkType The type of link this will be.  e.g. self, first, last, etc.
     * @param href     The link to the resource specified by the link type
     */
    public Link(LinkType linkType, String href) {
        this.rel = linkType.getLinkTypeName();
        this.href = href;
    }

    public String getRel() {
        return rel;
    }

    public String getHref() {
        return href;
    }
}
