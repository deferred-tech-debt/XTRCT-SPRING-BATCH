package com.contribute.xtrct.presentation.model;

import com.chromedata.commons.dsvio.DSVColumn;
import com.chromedata.commons.identity.IdentityGenerator;
import com.chromedata.commons.identity.utils.IdentityGenerators;

/**
 * Created with IntelliJ IDEA.
 * User: abhishek.mundewal
 * Date: 1/29/14
 * Time: 2:53 AM
 * To change this template use File | Settings | File Templates.
 */
public class ValueVariation implements Comparable<ValueVariation> {

    private static final IdentityGenerator IDENTITY = IdentityGenerators.createIdentityGenerator(ValueVariation.class);

    @DSVColumn(header = "ValueVariationID", qualified = false)
    private String ValueVariationID;
    @DSVColumn(header = "IncentiveID", qualified = false)
    private Integer IncentiveID;
    @DSVColumn(header = "Requirements")
    private String Requirements;
    @DSVColumn(header = "TierStart")
    private String TierStart;
    @DSVColumn(header = "TierEnd")
    private String TierEnd;
    @DSVColumn(header = "Tiers")
    private String Tiers;

    public String getValueVariationID() {
        return ValueVariationID;
    }

    public void setValueVariationID(String valueVariationID) {
        this.ValueVariationID = valueVariationID;
    }

    public String getTierEnd() {
        return TierEnd;
    }

    public void setTierEnd(String tierEnd) {
        TierEnd = tierEnd;
    }

    public String getTierStart() {
        return TierStart;
    }

    public void setTierStart(String tierStart) {
        TierStart = tierStart;
    }

    public String getTiers() {
        return Tiers;
    }

    public void setTiers(String tiers) {
        Tiers = tiers;
    }

    public String getRequirements() {
        return Requirements;
    }

    public void setRequirements(String requirements) {
        this.Requirements = requirements;
    }

    public Integer getIncentiveID() {
        return IncentiveID;
    }

    public void setIncentiveID(Integer incentiveID) {
        this.IncentiveID = incentiveID;
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
    public int compareTo(ValueVariation valueVariation) {
        if (valueVariation == this) return 0;

        int comparison = this.getIncentiveID().compareTo(valueVariation.getIncentiveID());
        if (comparison != 0) return comparison;

        comparison = this.getValueVariationID().compareTo(valueVariation.getValueVariationID());
        if (comparison != 0) return comparison;

        comparison = this.getRequirements().compareTo(valueVariation.getRequirements());
        if (comparison != 0) return comparison;

        comparison = this.getTierStart().compareTo(valueVariation.getTierStart());
        if (comparison != 0) return comparison;

        return this.getTierEnd().compareTo(valueVariation.getTierEnd());
    }
}
