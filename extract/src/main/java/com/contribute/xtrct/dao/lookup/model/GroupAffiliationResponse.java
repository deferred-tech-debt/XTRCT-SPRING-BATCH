package com.contribute.xtrct.dao.lookup.model;

import com.chromedata.incentives.response.AbstractCopyRightResponse;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Display class modeling a group affiliation container response object
 */
@XmlRootElement(name = "GroupAffiliationResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class GroupAffiliationResponse extends AbstractCopyRightResponse {

    @XmlElement(name = "GroupAffiliation")
    private List<GroupAffiliation> groupAffiliations;

    // Private no-arg constructor required by jax-b
    private GroupAffiliationResponse() {
    }

    public GroupAffiliationResponse(List<GroupAffiliation> groupAffiliations) {
        this.groupAffiliations = groupAffiliations;
    }

    public List<GroupAffiliation> getGroupAffiliations() {
        return groupAffiliations;
    }
}
