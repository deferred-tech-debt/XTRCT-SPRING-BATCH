package com.chromedata.incentives.extract.presentation.model;

import com.chromedata.commons.dsvio.DSVColumn;
import com.chromedata.commons.identity.IdentityGenerator;
import com.chromedata.commons.identity.utils.IdentityGenerators;

/**
 * Created with IntelliJ IDEA.
 * User: abhishek.mundewal
 * Date: 1/29/14
 * Time: 2:48 AM
 * To change this template use File | Settings | File Templates.
 */
public class ProgramRule implements Comparable<ProgramRule> {

    private static final IdentityGenerator IDENTITY = IdentityGenerators.createIdentityGenerator(ProgramRule.class);

    @DSVColumn(header = "IncentiveID", qualified = false)
    private Integer incentiveID;
    @DSVColumn(header = "Type")
    private String type;
    @DSVColumn(header = "Description")
    private String description;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object obj) {
        return IDENTITY.areEqual(this, obj);
    }

    @Override
    public int hashCode() {
        return IDENTITY.getHashCode(this);
    }

    @Override
    public String toString() {
        return IDENTITY.toString(this);
    }

    @Override
    public int compareTo(ProgramRule o) {
        if(o == this) return 0;

        int comparison = this.getIncentiveID().compareTo(o.getIncentiveID());
        if(comparison != 0) return comparison;
        comparison = this.getDescription().compareTo(o.getDescription());
        if(comparison != 0) return comparison;

        return (this.getType().compareTo(o.getType()));
    }
}
