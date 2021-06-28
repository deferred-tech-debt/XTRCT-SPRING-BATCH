package com.chromedata.incentives.extract.dao.lookup.model;

/**
 * Represents the different possible link types.  This is synonymous to the rel or relation attribute in the html link
 * element.
 */
public enum LinkType {
    SELF("self");

    private final String linkTypeName;

    LinkType(String linkTypeName) {
        this.linkTypeName = linkTypeName;
    }

    public String getLinkTypeName() {
        return linkTypeName;
    }
}
