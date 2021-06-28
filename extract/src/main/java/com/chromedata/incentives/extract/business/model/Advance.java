package com.chromedata.incentives.extract.business.model;

import java.math.BigDecimal;

public class Advance {

    private String ltvRange;
    private BigDecimal ltvValue;
    private String maxAdvance;
    private boolean isDefault;

    public Advance(final String ltvRange, final BigDecimal ltvValue, final String maxAdvance, final boolean isDefault) {
        this.ltvRange = ltvRange;
        this.ltvValue = ltvValue;
        this.maxAdvance = maxAdvance;
        this.isDefault = isDefault;
    }

    public String getLtvRange() {
        return ltvRange;
    }

    public void setLtvRange(final String ltvRange) {
        this.ltvRange = ltvRange;
    }

    public BigDecimal getLtvValue() {
        return ltvValue;
    }

    public void setLtvValue(final BigDecimal ltvValue) {
        this.ltvValue = ltvValue;
    }

    public String getMaxAdvance() {
        return maxAdvance;
    }

    public void setMaxAdvance(final String maxAdvance) {
        this.maxAdvance = maxAdvance;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(final boolean aDefault) {
        isDefault = aDefault;
    }
}
