package com.chromedata.incentives.extract.dao.lookup.model;

import com.chromedata.incentives.response.AbstractCopyRightResponse;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Display class modeling a previous ownership container response object
 */
@XmlRootElement(name = "PreviousOwnershipResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class PreviousOwnershipResponse extends AbstractCopyRightResponse {

    @XmlElement(name = "PreviousOwnership")
    private List<PreviousOwnership> previousOwnerships;

    // Private no-arg constructor required by jax-b
    private PreviousOwnershipResponse() {
    }

    public PreviousOwnershipResponse(List<PreviousOwnership> previousOwnerships) {
        this.previousOwnerships = previousOwnerships;
    }

    public List<PreviousOwnership> getPreviousOwnerships() {
        return previousOwnerships;
    }
}
