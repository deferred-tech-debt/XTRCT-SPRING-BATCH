package com.chromedata.incentives.extract.business.model;


import java.math.BigDecimal;
import java.util.List;


public class Variation {

    private String requirement;
    private String tierStart;
    private String tierEnd;
    private String tiers;
    private String description;
    private String note;
    private BigDecimal amountFinancedFrom;
    private BigDecimal amountFinancedTo;
    private Integer mileageFrom;
    private Integer mileageTo;
    private int paymentsWaived;
    private BigDecimal value;
    private List<Term> termList;
    private Boolean upTo;
    private Integer maxNumberOfPaymentsWaived;
    private Integer maxValueOfPaymentsWaived;
    private Integer maxValueOfPaymentsWaivedPerPayment;
    private String exitingGroup;
    private String leasesExpiringFrom;
    private String leasesExpiringTo;
    private Integer numberOfDays;
    private Integer DFUID;
    private Integer valueVariationId;
    private Integer incentiveId;
    private BigDecimal maximumResidualizableMsrp;
    private Double percent;
    private Boolean basePriceOnly;
    private String earnedPeriodTo;
    private String earnedPeriodFrom;
    private Integer upToMaxNumber;
    private BigDecimal dollarValueEach;
    private int order;

    public String getRequirement() {
        return requirement;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }

    public String getTierStart() {
        return tierStart;
    }

    public void setTierStart(String tierStart) {
        this.tierStart = tierStart;
    }

    public String getTierEnd() {
        return tierEnd;
    }

    public void setTierEnd(String tierEnd) {
        this.tierEnd = tierEnd;
    }

    public String getTiers() {
        return tiers;
    }

    public void setTiers(String tiers) {
        this.tiers = tiers;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public BigDecimal getAmountFinancedFrom() {
        return amountFinancedFrom;
    }

    public void setAmountFinancedFrom(BigDecimal amountFinancedFrom) {
        this.amountFinancedFrom = amountFinancedFrom;
    }

    public BigDecimal getAmountFinancedTo() {
        return amountFinancedTo;
    }

    public void setAmountFinancedTo(BigDecimal amountFinancedTo) {
        this.amountFinancedTo = amountFinancedTo;
    }

    public Integer getMileageFrom() {
        return mileageFrom;
    }

    public void setMileageFrom(Integer mileageFrom) {
        this.mileageFrom = mileageFrom;
    }

    public Integer getMileageTo() {
        return mileageTo;
    }

    public void setMileageTo(Integer mileageTo) {
        this.mileageTo = mileageTo;
    }

    public int getPaymentsWaived() {
        return paymentsWaived;
    }

    public void setPaymentsWaived(int paymentsWaived) {
        this.paymentsWaived = paymentsWaived;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public List<Term> getTermList() {
        return termList;
    }

    public void setTermList(List<Term> termList) {
        this.termList = termList;
    }

    public Boolean getUpTo() {
        return upTo;
    }

    public void setUpTo(Boolean upTo) {
        this.upTo = upTo;
    }

    public Integer getMaxNumberOfPaymentsWaived() {
        return maxNumberOfPaymentsWaived;
    }

    public void setMaxNumberOfPaymentsWaived(Integer maxNumberOfPaymentsWaived) {
        this.maxNumberOfPaymentsWaived = maxNumberOfPaymentsWaived;
    }

    public Integer getMaxValueOfPaymentsWaived() {
        return maxValueOfPaymentsWaived;
    }

    public void setMaxValueOfPaymentsWaived(Integer maxValueOfPaymentsWaived) {
        this.maxValueOfPaymentsWaived = maxValueOfPaymentsWaived;
    }

    public Integer getMaxValueOfPaymentsWaivedPerPayment() {
        return maxValueOfPaymentsWaivedPerPayment;
    }

    public void setMaxValueOfPaymentsWaivedPerPayment(Integer maxValueOfPaymentsWaivedPerPayment) {
        this.maxValueOfPaymentsWaivedPerPayment = maxValueOfPaymentsWaivedPerPayment;
    }

    public String getExitingGroup() {
        return exitingGroup;
    }

    public void setExitingGroup(String exitingGroup) {
        this.exitingGroup = exitingGroup;
    }

    public String getLeasesExpiringFrom() {
        return leasesExpiringFrom;
    }

    public void setLeasesExpiringFrom(String leasesExpiringFrom) {
        this.leasesExpiringFrom = leasesExpiringFrom;
    }

    public String getLeasesExpiringTo() {
        return leasesExpiringTo;
    }

    public void setLeasesExpiringTo(String leasesExpiringTo) {
        this.leasesExpiringTo = leasesExpiringTo;
    }

    public Integer getNumberOfDays() {
        return numberOfDays;
    }

    public void setNumberOfDays(Integer numberOfDays) {
        this.numberOfDays = numberOfDays;
    }

    public Integer getDFUID() {
        return DFUID;
    }

    public void setDFUID(Integer dFUID) {
        this.DFUID = dFUID;
    }

    public Integer getValueVariationId() {
        return valueVariationId;
    }

    public void setValueVariationId(Integer valueVariationId) {
        this.valueVariationId = valueVariationId;
    }

    public Integer getIncentiveId() {
        return incentiveId;
    }

    public void setIncentiveId(Integer incentiveId) {
        this.incentiveId = incentiveId;
    }

    public BigDecimal getMaximumResidualizableMsrp() {
        return maximumResidualizableMsrp;
    }

    public void setMaximumResidualizableMsrp(BigDecimal maximumResidualizableMsrp) {
        this.maximumResidualizableMsrp = maximumResidualizableMsrp;
    }

    public void setPercent(Double percent) {
        this.percent = percent;
    }

    public void setBasePriceOnly(Boolean basePriceOnly) {
        this.basePriceOnly = basePriceOnly;
    }

    public Double getPercent() {
        return percent;
    }

    public Boolean getBasePriceOnly() {
        return basePriceOnly;
    }

    public String getEarnedPeriodTo() {
        return earnedPeriodTo;
    }

    public void setEarnedPeriodTo(String earnedPeriodTo) {
        this.earnedPeriodTo = earnedPeriodTo;
    }

    public String getEarnedPeriodFrom() {
        return earnedPeriodFrom;
    }

    public void setEarnedPeriodFrom(String earnedPeriodFrom) {
        this.earnedPeriodFrom = earnedPeriodFrom;
    }

    public Integer getUpToMaxNumber() {
        return upToMaxNumber;
    }

    public void setUpToMaxNumber(Integer upToMaxNumber) {
        this.upToMaxNumber = upToMaxNumber;
    }

    public BigDecimal getDollarValueEach() {
        return dollarValueEach;
    }

    public void setDollarValueEach(BigDecimal dollarValueEach) {
        this.dollarValueEach = dollarValueEach;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "Variation [requirement=" + requirement + ", tierStart=" + tierStart + ", tierEnd=" + tierEnd
                + ", amountFinancedFrom=" + amountFinancedFrom + ", amountFinancedTo=" + amountFinancedTo
                + ", mileageFrom=" + mileageFrom + ", mileageTo=" + mileageTo + ", value=" + value + ", termList="
                + termList + "]";
    }
}
