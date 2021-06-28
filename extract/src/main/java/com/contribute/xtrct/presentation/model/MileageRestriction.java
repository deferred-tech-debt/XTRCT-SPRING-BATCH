package com.contribute.xtrct.presentation.model;

import com.chromedata.commons.dsvio.DSVColumn;
import com.chromedata.commons.identity.IdentityGenerator;
import com.chromedata.commons.identity.utils.IdentityGenerators;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Represents a row in the MileageRestriction.csv output file.  Mileage restrictions live inside a Term object.
 */
public class MileageRestriction implements Comparable<MileageRestriction> {

    private static final IdentityGenerator IDENTITY = IdentityGenerators.createIdentityGenerator(MileageRestriction.class);

    @DSVColumn(header = "TermID", qualified = false)
    private String termId;
    @DSVColumn(header = "Mileage")
    private String mileage;
    @DSVColumn(header = "Residual", qualified = false)
    private String residual;
    @DSVColumn(header = "IsDefault", qualified = false)
    private Boolean isDefault;

    public MileageRestriction(String termId, String mileage, BigDecimal residual, Boolean isDefault) {
        this.termId = termId;
        this.mileage = mileage;
        this.residual = Optional.ofNullable(residual).map(BigDecimal::toPlainString).orElse(null);
        this.isDefault = isDefault;
    }

    // No-arg constructor required by DSV library
    private MileageRestriction() {
    }

    public String getTermId() {
        return termId;
    }

    public String getMileage() {
        return mileage;
    }

    public String getResidual() {
        return residual;
    }

    public Boolean isDefault() {
        return isDefault;
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
    public int compareTo(MileageRestriction mileageRestriction) {
        if (mileageRestriction == this) {
            return 0;
        }

        int comparison = this.getTermId().compareTo(mileageRestriction.getTermId());
        if (comparison != 0) {
            return comparison;
        }

        comparison = this.getMileage().compareTo(mileageRestriction.getMileage());
        if (comparison != 0) {
            return comparison;
        }

        comparison = this.getResidual().compareTo(mileageRestriction.getResidual());
        if (comparison != 0) {
            return comparison;
        }

        return this.isDefault().compareTo(mileageRestriction.isDefault());
    }
}
