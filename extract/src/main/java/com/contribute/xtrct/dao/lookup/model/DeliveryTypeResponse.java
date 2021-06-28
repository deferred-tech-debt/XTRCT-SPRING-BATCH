package com.contribute.xtrct.dao.lookup.model;

import com.chromedata.incentives.response.AbstractCopyRightResponse;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Display class modeling a delivery type container response object
 */
@XmlRootElement(name = "DeliveryTypeResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class DeliveryTypeResponse extends AbstractCopyRightResponse {

    @XmlElement(name = "DeliveryType")
    private List<DeliveryType> deliveryTypes;

    // Private no-arg constructor required by jax-b
    private DeliveryTypeResponse() {
    }

    public DeliveryTypeResponse(List<DeliveryType> deliveryTypes) {
        this.deliveryTypes = deliveryTypes;
    }

    public List<DeliveryType> getDeliveryTypes() {
        return deliveryTypes;
    }
}
