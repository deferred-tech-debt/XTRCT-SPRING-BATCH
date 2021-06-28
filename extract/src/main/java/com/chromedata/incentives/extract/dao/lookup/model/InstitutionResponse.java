package com.chromedata.incentives.extract.dao.lookup.model;

import com.chromedata.incentives.response.AbstractCopyRightResponse;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Display class modeling a financial institution container response object
 */
@XmlRootElement(name = "InstitutionResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class InstitutionResponse extends AbstractCopyRightResponse {

    @XmlElement(name = "Institution")
    private List<Institution> institutions;

    // Private no-arg constructor required by jax-b
    private InstitutionResponse() {
    }

    public InstitutionResponse(List<Institution> institutions) {
        this.institutions = institutions;
    }

    public List<Institution> getInstitutions() {
        return institutions;
    }
}
