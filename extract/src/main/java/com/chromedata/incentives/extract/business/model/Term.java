package com.chromedata.incentives.extract.business.model;


import java.math.BigDecimal;
import java.util.List;


public class Term {

    private int from;
    private int to;
    private BigDecimal value;
    private Character valueType;
    private Double variance;
    private String futureValue;
    private Integer programValueId;
    private BigDecimal amountFinancedFrom;
    private BigDecimal amountFinancedTo;
    private String financialDisclosure;
    private List<MileageRestriction> mileageRestrictions;
    private List<Advance> advances;

    public int getFrom() {
        return from;
    }

    public void setFrom(final int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(final int to) {
        this.to = to;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(final BigDecimal value) {
        this.value = value;
    }

    public Character getValueType() {
        return valueType;
    }

    public void setValueType(Character valueType) {
        this.valueType = valueType;
    }

    public Double getVariance() {
        return variance;
    }

    public void setVariance(Double variance) {
        this.variance = variance;
    }

    public String getFutureValue() {
        return futureValue;
    }

    public void setFutureValue(String futureValue) {
        this.futureValue = futureValue;
    }

    public Integer getProgramValueId() {
        return programValueId;
    }

    public void setProgramValueId(Integer programValueId) {
        this.programValueId = programValueId;
    }

    public BigDecimal getAmountFinancedFrom() {
        return amountFinancedFrom;
    }

    public void setAmountFinancedFrom(final BigDecimal amountFinancedFrom) {
        this.amountFinancedFrom = amountFinancedFrom;
    }

    public BigDecimal getAmountFinancedTo() {
        return amountFinancedTo;
    }

    public void setAmountFinancedTo(final BigDecimal amountFinancedTo) {
        this.amountFinancedTo = amountFinancedTo;
    }

    public String getFinancialDisclosure() {
        return financialDisclosure;
    }

    public void setFinancialDisclosure(final String financialDisclosure) {
        this.financialDisclosure = financialDisclosure;
    }

    public List<MileageRestriction> getMileageRestrictions() {
        return mileageRestrictions;
    }

    public void setMileageRestrictions(List<MileageRestriction> mileageRestrictions) {
        this.mileageRestrictions = mileageRestrictions;
    }


    public List<Advance> getAdvances() {
        return advances;
    }

    public void setAdvances(final List<Advance> advances) {
        this.advances = advances;
    }

    /**
     * Returns true if this term has an associated value.  The term is considered having a value if there is a dollar
     * or percentage value amount, or if the term has mileage restrictions associated with it.
     *
     * @return True if this term has a value
     */
    public boolean hasValue() {
        return this.value != null
                || (this.getMileageRestrictions() != null && !this.getMileageRestrictions().isEmpty());
    }

    @Override
    public String toString() {
        return "BasicTerm [from=" + from + ", to=" + to + ", value=" + value + "]";
    }
}
