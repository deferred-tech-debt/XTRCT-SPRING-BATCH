package com.chromedata.incentives.extract.dao.lookup.model;

import com.chromedata.incentives.response.AbstractCopyRightResponse;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Display class modeling a end recipient response object
 */
@XmlRootElement(name = "EndRecipientResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class EndRecipientResponse extends AbstractCopyRightResponse {

    @XmlElement(name = "EndRecipient")
    private List<EndRecipient> endRecipient;

    // Private no-arg constructor required by jax-b
    private EndRecipientResponse() {
    }

    public EndRecipientResponse(List<EndRecipient> endRecipient) {
        this.endRecipient = endRecipient;
    }

    public List<EndRecipient> getEndRecipients() {
        return endRecipient;
    }
}
