package com.chromedata.incentives.extract.presentation.model;

import com.chromedata.commons.dsvio.DSVColumn;
import com.chromedata.commons.identity.IdentityGenerator;
import com.chromedata.commons.identity.utils.IdentityGenerators;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 * User: abhishek.mundewal
 * Date: 1/29/14
 * Time: 3:00 AM
 * To change this template use File | Settings | File Templates.
 */
public class Term implements Comparable<Term> {

    private static final IdentityGenerator IDENTITY = IdentityGenerators.createIdentityGenerator(Term.class);

    @DSVColumn(header = "ProgramValueID", qualified = false)
    private String programValueId;
    @DSVColumn(header = "TermID", qualified = false)
    private String termId;
    @DSVColumn(header = "From", qualified = false)
    private Integer from;
    @DSVColumn(header = "To", qualified = false)
    private Integer to;
    @DSVColumn(header = "Value", qualified = false)
    private String value;
    @DSVColumn(header = "ValueType")
    private Character valueType;
    @DSVColumn(header = "Variance", qualified = false)
    private Double variance;
    @DSVColumn(header = "FutureValue", qualified = false)
    private String futureValue;
    @DSVColumn(header = "AmountFinancedFrom", qualified = false)
    private String amountFinancedFrom;
    @DSVColumn(header = "AmountFinancedTo", qualified = false)
    private String amountFinancedTo;
    @DSVColumn(header = "FinancialDisclosure")
    private String financialDisclosure;

    public String getProgramValueId() {
        return programValueId;
    }

    public void setProgramValueId(String programValueId) {
        this.programValueId = programValueId;
    }

    public String getTermId() {
        return termId;
    }

    public void setTermId(String termId) {
        this.termId = termId;
    }

    public String getFutureValue() {
        return futureValue;
    }

    public void setFutureValue(String futureValue) {
        this.futureValue = futureValue;
    }

    public Double getVariance() {
        return variance;
    }

    public void setVariance(Double variance) {
        this.variance = variance;
    }

    public Character getValueType() {
        return valueType;
    }

    public void setValueType(Character valueType) {
        this.valueType = valueType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setValue(BigDecimal value) {
        this.value = Optional.ofNullable(value).map(BigDecimal::toPlainString).orElse(null);
    }

    public Integer getTo() {
        return to;
    }

    public void setTo(Integer to) {
        this.to = to;
    }

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public String getAmountFinancedTo() {
        return amountFinancedTo;
    }

    public void setAmountFinancedTo(BigDecimal amountFinancedTo) {
        this.amountFinancedTo = Optional.ofNullable(amountFinancedTo).map(BigDecimal::toPlainString).orElse(null);
    }

    public void setAmountFinancedTo(String amountFinancedTo) {
        this.amountFinancedTo = amountFinancedTo;
    }

    public String getAmountFinancedFrom() {
        return amountFinancedFrom;
    }

    public void setAmountFinancedFrom(BigDecimal amountFinancedFrom) {
        this.amountFinancedFrom = Optional.ofNullable(amountFinancedFrom).map(BigDecimal::toPlainString).orElse(null);
    }

    public void setAmountFinancedFrom(String amountFinancedFrom) {
        this.amountFinancedFrom = amountFinancedFrom;
    }

    public String getFinancialDisclosure() {
        return financialDisclosure;
    }

    public void setFinancialDisclosure(String financialDisclosure) {
        this.financialDisclosure = financialDisclosure;
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
    public int compareTo(Term term) {
        if (term == this) return 0;

        int comparison = this.getProgramValueId().compareTo(term.getProgramValueId());
        if (comparison != 0) return comparison;

        comparison = this.getFrom().compareTo(term.getFrom());
        if (comparison != 0) return comparison;

        comparison = this.getTo().compareTo(term.getTo());
        if (comparison != 0) return comparison;

        comparison = this.getValue().compareTo(term.getValue());
        if (comparison != 0) return comparison;

        comparison = this.getValueType().compareTo(term.getValueType());
        if (comparison != 0) return comparison;

        comparison = this.getVariance().compareTo(term.getVariance());
        if (comparison != 0) return comparison;

        comparison = this.getFutureValue().compareTo(term.getFutureValue());
        if (comparison != 0) return comparison;

        comparison = this.getAmountFinancedFrom().compareTo(term.getAmountFinancedFrom());
        if (comparison != 0) return comparison;

        comparison = this.getAmountFinancedTo().compareTo(term.getAmountFinancedTo());
        if (comparison != 0) return comparison;

        return this.getFinancialDisclosure().compareTo(term.getFinancialDisclosure());
    }
}
