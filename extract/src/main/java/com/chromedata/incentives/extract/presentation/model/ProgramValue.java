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
 * Time: 2:58 AM
 * To change this template use File | Settings | File Templates.
 */
public class ProgramValue implements Comparable<ProgramValue> {

    private static final IdentityGenerator IDENTITY = IdentityGenerators.createIdentityGenerator(ProgramValue.class);

    @DSVColumn(header = "ProgramValueID", qualified = false)
    private String ProgramValueID;
    @DSVColumn(header = "ValueVariationID", qualified = false)
    private String ValueVariationID;
    @DSVColumn(header = "Cash", qualified = false)
    private Integer Cash;
    @DSVColumn(header = "UpTo", qualified = false)
    private Boolean UpTo;
    @DSVColumn(header = "MaxNumberOfPaymentsWaived", qualified = false)
    private Integer MaxNumberOfPaymentsWaived;
    @DSVColumn(header = "MaxValueOfPaymentsWaived", qualified = false)
    private Integer MaxValueOfPaymentsWaived;
    @DSVColumn(header = "MaxValueOfPaymentsWaivedPerPayment", qualified = false)
    private Integer MaxValueOfPaymentsWaivedPerPayment;
    @DSVColumn(header = "ExitingGroup")
    private String ExitingGroup;
    @DSVColumn(header = "LeasesExpiringFrom")
    private String LeasesExpiringFrom;
    @DSVColumn(header = "LeasesExpiringTo")
    private String LeasesExpiringTo;
    @DSVColumn(header = "Description")
    private String Description;
    @DSVColumn(header = "Note")
    private String Note;
    @DSVColumn(header = "NumberOfDays", qualified = false)
    private Integer NumberOfDays;
    @DSVColumn(header = "MRM", qualified = false)
    private String mrm;
    @DSVColumn(header = "Percent", qualified = false)
    private Double percent;
    @DSVColumn(header = "BasePriceOnly", qualified = false)
    private Boolean basePriceOnly;
    @DSVColumn(header = "DollarValueEach", qualified = false)
    private String dollarValueEach;
    @DSVColumn(header = "UpToMaxNumber", qualified = false)
    private Integer upToMaxNumber;
    @DSVColumn(header = "EarnedPeriodFrom", qualified = false)
    private String earnedPeriodFrom;
    @DSVColumn(header = "EarnedPeriodTo", qualified = false)
    private String earnedPeriodTo;

    public String getProgramValueID() {
        return ProgramValueID;
    }

    public void setProgramValueID(String programValueID) {
        this.ProgramValueID = programValueID;
    }

    public Integer getNumberOfDays() {
        return NumberOfDays;
    }

    public void setNumberOfDays(Integer numberOfDays) {
        NumberOfDays = numberOfDays;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }

    public String getLeasesExpiringTo() {
        return LeasesExpiringTo;
    }

    public void setLeasesExpiringTo(String leasesExpiringTo) {
        LeasesExpiringTo = leasesExpiringTo;
    }

    public String getLeasesExpiringFrom() {
        return LeasesExpiringFrom;
    }

    public void setLeasesExpiringFrom(String leasesExpiringFrom) {
        LeasesExpiringFrom = leasesExpiringFrom;
    }

    public String getExitingGroup() {
        return ExitingGroup;
    }

    public void setExitingGroup(String exitingGroup) {
        ExitingGroup = exitingGroup;
    }

    public Integer getMaxValueOfPaymentsWaivedPerPayment() {
        return MaxValueOfPaymentsWaivedPerPayment;
    }

    public void setMaxValueOfPaymentsWaivedPerPayment(Integer maxValueOfPaymentsWaivedPerPayment) {
        MaxValueOfPaymentsWaivedPerPayment = maxValueOfPaymentsWaivedPerPayment;
    }

    public Integer getMaxValueOfPaymentsWaived() {
        return MaxValueOfPaymentsWaived;
    }

    public void setMaxValueOfPaymentsWaived(Integer maxValueOfPaymentsWaived) {
        MaxValueOfPaymentsWaived = maxValueOfPaymentsWaived;
    }

    public Integer getMaxNumberOfPaymentsWaived() {
        return MaxNumberOfPaymentsWaived;
    }

    public void setMaxNumberOfPaymentsWaived(Integer maxNumberOfPaymentsWaived) {
        MaxNumberOfPaymentsWaived = maxNumberOfPaymentsWaived;
    }

    public Boolean getUpTo() {
        return UpTo;
    }

    public void setUpTo(Boolean upTo) {
        UpTo = upTo;
    }

    public Integer getCash() {
        return Cash;
    }

    public void setCash(Integer cash) {
        Cash = cash;
    }

    public String getValueVariationID() {
        return ValueVariationID;
    }

    public void setValueVariationID(String valueVariationID) {
        this.ValueVariationID = valueVariationID;
    }

    public String getMrm() {
        return mrm;
    }

    public void setMrm(BigDecimal mrm) {
        this.mrm = Optional.ofNullable(mrm).map(BigDecimal::toPlainString).orElse(null);
    }

    public void setMrm(String mrm) {
        this.mrm = mrm;
    }

    public void setPercent(Double percent) {
        this.percent = percent;
    }

    public void setBasePriceOnly(Boolean basePriceOnly) {
        this.basePriceOnly = basePriceOnly;
    }

    public String getDollarValueEach() {
        return dollarValueEach;
    }

    public void setDollarValueEach(BigDecimal dollarValueEach) {
        this.dollarValueEach = Optional.ofNullable(dollarValueEach).map(BigDecimal::toPlainString).orElse(null);
    }

    public void setDollarValueEach(String dollarValueEach) {
        this.dollarValueEach = dollarValueEach;
    }

    public Integer getUpToMaxNumber() {
        return upToMaxNumber;
    }

    public void setUpToMaxNumber(Integer upToMaxNumber) {
        this.upToMaxNumber = upToMaxNumber;
    }

    public String getEarnedPeriodFrom() {
        return earnedPeriodFrom;
    }

    public void setEarnedPeriodFrom(String earnedPeriodFrom) {
        this.earnedPeriodFrom = earnedPeriodFrom;
    }

    public String getEarnedPeriodTo() {
        return earnedPeriodTo;
    }

    public void setEarnedPeriodTo(String earnedPeriodTo) {
        this.earnedPeriodTo = earnedPeriodTo;
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
    public int compareTo(ProgramValue programValue) {
        if (programValue == this) return 0;

        int comparison = this.getProgramValueID().compareTo(programValue.getProgramValueID());
        if (comparison != 0) return comparison;

        comparison = this.getValueVariationID().compareTo(programValue.getValueVariationID());
        if (comparison != 0) return comparison;

        comparison = this.getCash().compareTo(programValue.getCash());
        if (comparison != 0) return comparison;

        comparison = this.getUpTo().compareTo(programValue.getUpTo());
        if (comparison != 0) return comparison;

        comparison = this.getMaxNumberOfPaymentsWaived().compareTo(programValue.getMaxNumberOfPaymentsWaived());
        if (comparison != 0) return comparison;

        comparison = this.getMaxValueOfPaymentsWaived().compareTo(programValue.getMaxValueOfPaymentsWaived());
        if (comparison != 0) return comparison;

        comparison = this.getMaxValueOfPaymentsWaivedPerPayment().compareTo(programValue.getMaxValueOfPaymentsWaivedPerPayment());
        if (comparison != 0) return comparison;

        comparison = this.getExitingGroup().compareTo(programValue.getExitingGroup());
        if (comparison != 0) return comparison;

        comparison = this.getLeasesExpiringFrom().compareTo(programValue.getLeasesExpiringFrom());
        if (comparison != 0) return comparison;

        comparison = this.getLeasesExpiringTo().compareTo(programValue.getLeasesExpiringTo());
        if (comparison != 0) return comparison;

        comparison = this.getDescription().compareTo(programValue.getDescription());
        if (comparison != 0) return comparison;

        comparison = this.getNumberOfDays().compareTo(programValue.getNumberOfDays());
        if (comparison != 0) return comparison;

        return this.getNote().compareTo(programValue.getNote());
    }
}
