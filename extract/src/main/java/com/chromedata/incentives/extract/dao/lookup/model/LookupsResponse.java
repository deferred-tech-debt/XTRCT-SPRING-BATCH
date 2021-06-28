package com.chromedata.incentives.extract.dao.lookup.model;

import com.chromedata.incentives.response.AbstractCopyRightResponse;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Display class modeling a root lookup index container response object
 */
@XmlRootElement(name = "LookupsResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class LookupsResponse extends AbstractCopyRightResponse {

    @XmlElement(name = "Lookup")
    private List<Lookup> lookups;

    // Private no-arg constructor required by jax-b
    private LookupsResponse() {
    }

    public LookupsResponse(List<Lookup> lookups) {
        this.lookups = lookups;
    }

    public List<Lookup> getLookups() {
        return lookups;
    }
}
