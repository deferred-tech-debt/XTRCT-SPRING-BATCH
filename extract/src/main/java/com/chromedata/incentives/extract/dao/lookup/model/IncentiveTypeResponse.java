package com.chromedata.incentives.extract.dao.lookup.model;

import com.chromedata.incentives.response.AbstractCopyRightResponse;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Display class modeling a incentive type container response object
 */
@XmlRootElement(name = "IncentiveTypeResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class IncentiveTypeResponse extends AbstractCopyRightResponse {

    @XmlElement(name = "IncentiveType")
    private List<IncentiveType> incentiveTypes;

    // Private no-arg constructor required by jax-b
    private IncentiveTypeResponse() {
    }

    public IncentiveTypeResponse(List<IncentiveType> incentiveTypes) {
        this.incentiveTypes = incentiveTypes;
    }

    public List<IncentiveType> getIncentiveTypes() {
        return incentiveTypes;
    }
}
