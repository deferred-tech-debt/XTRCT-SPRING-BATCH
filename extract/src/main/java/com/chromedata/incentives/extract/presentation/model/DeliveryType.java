package com.chromedata.incentives.extract.presentation.model;

import com.chromedata.commons.dsvio.DSVColumn;
import com.chromedata.commons.identity.IdentityGenerator;
import com.chromedata.commons.identity.utils.IdentityGenerators;

/**
 * Created with IntelliJ IDEA.
 * User: abhishek.mundewal
 * Date: 1/29/14
 * Time: 2:52 AM
 * To change this template use File | Settings | File Templates.
 */
public class DeliveryType implements Comparable<DeliveryType> {

    private static final IdentityGenerator IDENTITY = IdentityGenerators.createIdentityGenerator(DeliveryType.class);

    @DSVColumn(header = "IncentiveID", qualified = false)
    private Integer incentiveID;
    @DSVColumn(header = "Type")
    private String type;
    @DSVColumn(header = "DeliveryTypeID", qualified = false)
    private Integer deliveryTypeId;

    public Integer getIncentiveID() {
        return incentiveID;
    }

    public void setIncentiveID(Integer incentiveID) {
        this.incentiveID = incentiveID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getDeliveryTypeId() {
        return deliveryTypeId;
    }

    public void setDeliveryTypeId(final Integer deliveryTypeId) {
        this.deliveryTypeId = deliveryTypeId;
    }

    @Override
    public boolean equals(Object o) {
        return IDENTITY.areEqual(this, o);
    }

    @Override
    public int hashCode() {
        return IDENTITY.getHashCode(this);
    }

    @Override
    public int compareTo(DeliveryType o) {
        if(o == this) return 0;

        int comparison = this.getIncentiveID().compareTo(o.getIncentiveID());
        if(comparison != 0) return comparison;
        comparison = this.getType().compareTo(o.getType());
        if(comparison != 0) return comparison;
        comparison = this.getDeliveryTypeId().compareTo(o.getDeliveryTypeId());
        return comparison;
    }

    @Override
    public String toString() {
        return IDENTITY.toString(this);
    }
}
