package com.contribute.xtrct.dao.lookup.model;

import com.chromedata.incentives.response.AbstractCopyRightResponse;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Display class modeling a division container response object
 */
@XmlRootElement(name = "DivisionResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class DivisionResponse extends AbstractCopyRightResponse {

    @XmlElement(name = "Division")
    private List<Division> divisions;

    // Private no-arg constructor required by jax-b
    private DivisionResponse() {
    }

    public DivisionResponse(List<Division> divisions) {
        this.divisions = divisions;
    }

    public List<Division> getDivisions() {
        return divisions;
    }
}
