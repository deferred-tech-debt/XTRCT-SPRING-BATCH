package com.chromedata.incentives.extract.presentation.model;

import com.chromedata.commons.dsvio.DSVColumn;
import com.chromedata.commons.identity.IdentityGenerator;
import com.chromedata.commons.identity.utils.IdentityGenerators;

/**
 * Created with IntelliJ IDEA.
 * User: abhishek.mundewal
 * Date: 1/29/14
 * Time: 2:50 AM
 * To change this template use File | Settings | File Templates.
 */
public class Institution implements Comparable<Institution> {

    private static final IdentityGenerator IDENTITY = IdentityGenerators.createIdentityGenerator(Institution.class);

    @DSVColumn(header = "IncentiveID", qualified = false)
    private Integer incentiveID;
    @DSVColumn(header = "InstitutionID", qualified = false)
    private Integer institutionID;
    @DSVColumn(header = "Institution")
    private String institution;

    public Integer getIncentiveID() {
        return incentiveID;
    }

    public void setIncentiveID(Integer incentiveID) {
        this.incentiveID = incentiveID;
    }

    public Integer getInstitutionID() {
        return institutionID;
    }

    public void setInstitutionID(Integer institutionID) {
        this.institutionID = institutionID;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
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
    public int compareTo(Institution o) {
        if(o == this) return 0;

        int comparison = this.getIncentiveID().compareTo(o.getIncentiveID());
        if(comparison != 0) return comparison;

        comparison = this.getInstitutionID().compareTo(o.getInstitutionID());
        if (comparison != 0) return comparison;

        comparison = this.getInstitution().compareTo(o.getInstitution());
        return comparison;
    }
}
