package com.contribute.xtrct.presentation.model;

import com.chromedata.commons.dsvio.DSVColumn;
import com.chromedata.commons.identity.IdentityGenerator;
import com.chromedata.commons.identity.utils.IdentityGenerators;

import java.math.BigDecimal;
import java.util.Optional;

public class LoanToValue implements Comparable<LoanToValue>{

    private static final IdentityGenerator IDENTITY = IdentityGenerators.createIdentityGenerator(LoanToValue.class);

    @DSVColumn(header = "TermID", qualified = false)
    private String termId;
    @DSVColumn(header = "LTVRange", qualified = false)
    private String ltvRange;
    @DSVColumn(header = "LTVValue", qualified = false)
    private String ltvValue;
    @DSVColumn(header = "MaxAdvance", qualified = false)
    private String maxAdvance;
    @DSVColumn(header = "isDefault", qualified = false)
    private Boolean isDefault;

    public LoanToValue(final String termId, final String ltvRange, final BigDecimal ltvValue, final String maxAdvance, final Boolean isDefault) {
        this.termId = termId;
        this.ltvRange = ltvRange;
        this.ltvValue = Optional.ofNullable(ltvValue).map(BigDecimal::toPlainString).orElse(null);
        this.maxAdvance = maxAdvance;
        this.isDefault = isDefault;
    }

    public String getTermId() {
        return termId;
    }

    public void setTermId(final String termId) {
        this.termId = termId;
    }

    public String getLtvRange() {
        return ltvRange;
    }

    public void setLtvRange(final String ltvRange) {
        this.ltvRange = ltvRange;
    }

    public String getLtvValue() {
        return ltvValue;
    }

    public void setLtvValue(final String ltvValue) {
        this.ltvValue = ltvValue;
    }

    public void setLtvValue(final BigDecimal ltvValue) {
        this.ltvValue = Optional.ofNullable(ltvValue).map(BigDecimal::toPlainString).orElse(null);
    }

    public String getMaxAdvance() {
        return maxAdvance;
    }

    public void setMaxAdvance(final String maxAdvance) {
        this.maxAdvance = maxAdvance;
    }

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(final Boolean aDefault) {
        isDefault = aDefault;
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
    public int compareTo(LoanToValue loanToValue) {
        if (loanToValue == this) return 0;

        int comparison = this.getTermId().compareTo(loanToValue.getTermId());
        if (comparison != 0) return comparison;

        comparison = this.getLtvRange().compareTo(loanToValue.getLtvRange());
        if (comparison != 0) return comparison;

        comparison = this.getLtvValue().compareTo(loanToValue.getLtvValue());
        if (comparison != 0) return comparison;

        return this.getMaxAdvance().compareTo(loanToValue.getMaxAdvance());
    }
}
