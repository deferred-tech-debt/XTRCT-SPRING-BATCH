package com.chromedata.incentives.extract.presentation.model;

import com.chromedata.commons.dsvio.DSVColumn;
import com.chromedata.commons.identity.IdentityGenerator;
import com.chromedata.commons.identity.utils.IdentityGenerators;

import java.time.LocalDate;

/**
 * Created with IntelliJ IDEA.
 * User: abhishek.mundewal
 * Date: 11/27/13
 * Time: 8:40 PM
 * To change this template use File | Settings | File Templates.
 */

/**
 *
 * Model class for the Incentive.
 */
public class Incentive implements Comparable<Incentive> {

    private static final IdentityGenerator IDENTITY = IdentityGenerators.createIdentityGenerator(Incentive.class);

    @DSVColumn(header = "IncentiveID", qualified = false)
    private Integer incentiveID;
    @DSVColumn(header = "RegionID", qualified = false)
    private Integer geographyID;
    @DSVColumn(header = "SignatureHistoryID", qualified = false)
    private Integer signatureHistoryID;
    @DSVColumn(header = "SignatureID", qualified = false)
    private Integer signatureID;
    @DSVColumn(header = "CategoryID", qualified = false)
    private Integer categoryID;
    @DSVColumn(header = "CategoryDescription")
    private String categoryDescription;
    @DSVColumn(header = "ProgramDescription")
    private String programDescription;
    @DSVColumn(header = "ProgramNumber")
    private String programNumber;
    @DSVColumn(header = "Market")
    private String market;
    @DSVColumn(header = "Type")
    private String type;
    @DSVColumn(header = "GroupAffiliation")
    private String groupAffiliation;
    @DSVColumn(header = "EffectiveDate", format = "yyyy-MM-dd")
    private LocalDate effectiveDate;
    @DSVColumn(header = "ExpiryDate", format = "yyyy-MM-dd")
    private LocalDate expiryDate;
    @DSVColumn(header = "PreviousOwnership")
    private String previousOwnership;
    @DSVColumn(header = "VehicleAttribute")
    private String vehicleAttribute;
    @DSVColumn(header = "ProgramText")
    private String programText;
    @DSVColumn(header = "EndRecipient")
    private String endRecipient;
    @DSVColumn(header = "Source")
    private String source;
    @DSVColumn(header = "OrderingCode")
    private String orderingCode;
    @DSVColumn(header = "TaxRuleID", qualified = false)
    private Integer taxRuleId;

    @DSVColumn(header = "CategoryGroup")
    private String categoryGroup;
    @DSVColumn(header = "MarketID", qualified = false)
    private Integer marketId;
    @DSVColumn(header = "GroupAffiliationID", qualified = false)
    private Integer groupAffiliationId;
    @DSVColumn(header = "PreviousOwnershipID", qualified = false)
    private Integer previousOwnershipId;
    @DSVColumn(header = "EndRecipientID", qualified = false)
    private Integer endRecipientId;

    @DSVColumn(header = "GroupAffiliationDetail")
    private String groupAffiliationDetail;
    @DSVColumn(header = "PreviousOwnershipDetail")
    private String previousOwnershipDetail;

    public Incentive() {
    }

    public Integer getIncentiveID() {
        return incentiveID;
    }

    public void setIncentiveID(Integer incentiveID) {
        this.incentiveID = incentiveID;
    }

    public Integer getGeographyID() {
        return geographyID;
    }

    public void setGeographyID(Integer geographyID) {
        this.geographyID = geographyID;
    }

    public Integer getSignatureID() {
        return signatureID;
    }

    public void setSignatureID(Integer signatureID) {
        this.signatureID = signatureID;
    }

    public Integer getSignatureHistoryID() {
        return signatureHistoryID;
    }

    public void setSignatureHistoryID(Integer signatureHistoryID) {
        this.signatureHistoryID = signatureHistoryID;
    }

    public Integer getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(Integer categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    public String getProgramDescription() {
        return programDescription;
    }

    public void setProgramDescription(String programDescription) {
        this.programDescription = programDescription;
    }

    public String getProgramNumber() {
        return programNumber;
    }

    public void setProgramNumber(String programNumber) {
        this.programNumber = programNumber;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGroupAffiliation() {
        return groupAffiliation;
    }

    public void setGroupAffiliation(String groupAffiliation) {
        this.groupAffiliation = groupAffiliation;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getPreviousOwnership() {
        return previousOwnership;
    }

    public void setPreviousOwnership(String previousOwnership) {
        this.previousOwnership = previousOwnership;
    }

    public String getVehicleAttribute() {
        return vehicleAttribute;
    }

    public void setVehicleAttribute(String vehicleAttribute) {
        this.vehicleAttribute = vehicleAttribute;
    }

    public String getProgramText() {
        return programText;
    }

    public void setProgramText(String programText) {
        this.programText = programText;
    }

    public String getEndRecipient() {
        return endRecipient;
    }

    public void setEndRecipient(String endRecipient) {
        this.endRecipient = endRecipient;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getOrderingCode() {
        return orderingCode;
    }

    public void setOrderingCode(String orderingCode) {
        this.orderingCode = orderingCode;
    }

    public Integer getTaxRuleId() {
        return taxRuleId;
    }

    public void setTaxRuleId(Integer taxRuleId) {
        this.taxRuleId = taxRuleId;
    }

    public String getCategoryGroup() {
        return categoryGroup;
    }

    public void setCategoryGroup(final String categoryGroup) {
        this.categoryGroup = categoryGroup;
    }

    public Integer getMarketId() {
        return marketId;
    }

    public void setMarketId(final Integer marketId) {
        this.marketId = marketId;
    }

    public Integer getGroupAffiliationId() {
        return groupAffiliationId;
    }

    public void setGroupAffiliationId(final Integer groupAffiliationId) {
        this.groupAffiliationId = groupAffiliationId;
    }

    public Integer getPreviousOwnershipId() {
        return previousOwnershipId;
    }

    public void setPreviousOwnershipId(final Integer previousOwnershipId) {
        this.previousOwnershipId = previousOwnershipId;
    }

    public Integer getEndRecipientId() {
        return endRecipientId;
    }

    public void setEndRecipientId(final Integer endRecipientId) {
        this.endRecipientId = endRecipientId;
    }

    public String getGroupAffiliationDetail() {
        return groupAffiliationDetail;
    }

    public void setGroupAffiliationDetail(final String groupAffiliationDetail) {
        this.groupAffiliationDetail = groupAffiliationDetail;
    }

    public String getPreviousOwnershipDetail() {
        return previousOwnershipDetail;
    }

    public void setPreviousOwnershipDetail(final String previousOwnershipDetail) {
        this.previousOwnershipDetail = previousOwnershipDetail;
    }

    @Override
    public int compareTo(Incentive incentive) {
        if (incentive == this) return 0;

        int comparison = (this.getIncentiveID().compareTo(incentive.getIncentiveID()));
        if (comparison != 0) return comparison;

        comparison = (this.getGeographyID().compareTo(incentive.getGeographyID()));
        if (comparison != 0) return comparison;

        comparison = (this.getCategoryID().compareTo(incentive.getCategoryID()));
        if (comparison != 0) return comparison;

        comparison = (this.getSignatureID().compareTo(incentive.getSignatureID()));
        if (comparison != 0) return comparison;

        comparison = (this.getSignatureHistoryID().compareTo(incentive.getSignatureHistoryID()));
        if (comparison != 0) return comparison;

        comparison = (this.getEffectiveDate().compareTo(incentive.getEffectiveDate()));
        if(comparison != 0) return comparison;

        comparison = (this.getCategoryDescription().compareTo(incentive.getCategoryDescription()));
        if(comparison != 0) return comparison;

        comparison = (this.getEndRecipient().compareTo(incentive.getEndRecipient()));
        if(comparison != 0) return comparison;

        comparison = (this.getMarket().compareTo(incentive.getMarket()));
        if(comparison != 0) return comparison;

        comparison = (this.getExpiryDate().compareTo(incentive.getExpiryDate()));
        if(comparison != 0) return comparison;

        comparison = (this.getGroupAffiliation().compareTo(incentive.getGroupAffiliation()));
        if(comparison != 0) return comparison;

        comparison = (this.getPreviousOwnership().compareTo(incentive.getPreviousOwnership()));
        if(comparison != 0) return comparison;

        comparison = (this.getProgramDescription().compareTo(incentive.getProgramDescription()));
        if(comparison != 0) return comparison;

        comparison = (this.getProgramNumber().compareTo(incentive.getProgramNumber()));
        if(comparison != 0) return comparison;

        comparison = (this.getProgramText().compareTo(incentive.getProgramText()));
        if(comparison != 0) return comparison;

        comparison = (this.getSource().compareTo(incentive.getSource()));
        if(comparison != 0) return comparison;

        comparison = (this.getVehicleAttribute().compareTo(incentive.getVehicleAttribute()));
        if(comparison != 0) return comparison;

        comparison = (this.getType().compareTo(incentive.getType()));
        if(comparison != 0) return comparison;

        comparison = (this.getProgramNumber().compareTo(incentive.getProgramNumber()));
        if(comparison != 0) return comparison;

        comparison = (this.getMarketId().compareTo(incentive.getMarketId()));
        if(comparison != 0) return comparison;

        comparison = (this.getGroupAffiliationId().compareTo(incentive.getGroupAffiliationId()));
        if(comparison != 0) return comparison;

        comparison = (this.getPreviousOwnershipId().compareTo(incentive.getPreviousOwnershipId()));
        if(comparison != 0) return comparison;

        comparison = (this.getCategoryGroup().compareTo(incentive.getCategoryGroup()));
        if(comparison != 0) return comparison;

        comparison = (this.getEndRecipientId().compareTo(incentive.getEndRecipientId()));
        if(comparison != 0) return comparison;

        return (this.getTaxRuleId().compareTo(incentive.getTaxRuleId()));
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
}
