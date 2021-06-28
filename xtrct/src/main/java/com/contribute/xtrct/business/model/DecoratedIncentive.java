package com.contribute.xtrct.business.model;

import java.util.Date;
import java.util.List;

/**
 * Wrapper class that allows subclasses to decorate only the methods they are interested in.  All methods default to
 * using the built in functionality of the incentive that this class holds.
 */
abstract class DecoratedIncentive implements Incentive {

    private Incentive baseIncentive;

    DecoratedIncentive(Incentive baseIncentive) {
        this.baseIncentive = baseIncentive;
    }

    @Override
    public String getId() {
        return baseIncentive.getId();
    }

    @Override
    public String getIncentiveType() {
        return baseIncentive.getIncentiveType();
    }

    @Override
    public List<FinancialInstitution> getFinancialInstitutionList() {
        return baseIncentive.getFinancialInstitutionList();
    }

    @Override
    public List<Variation> getVariationList() {
        return baseIncentive.getVariationList();
    }

    @Override
    public String getCategoryId() {
        return baseIncentive.getCategoryId();
    }

    @Override
    public String getCategoryDescription() {
        return baseIncentive.getCategoryDescription();
    }

    @Override
    public String getCategoryGroup() {
        return baseIncentive.getCategoryGroup();
    }

    @Override
    public String getName() {
        return baseIncentive.getName();
    }

    @Override
    public String getMarket() {
        return baseIncentive.getMarket();
    }

    @Override
    public String getGroupAffiliation() {
        return baseIncentive.getGroupAffiliation();
    }

    @Override
    public Date getEffectiveDate() {
        return baseIncentive.getEffectiveDate();
    }

    @Override
    public Date getExpiryDate() {
        return baseIncentive.getExpiryDate();
    }

    @Override
    public String getPreviousOwnership() {
        return baseIncentive.getPreviousOwnership();
    }

    @Override
    public List<VehicleStatus> getVehicleStatuses() {
        return baseIncentive.getVehicleStatuses();
    }

    @Override
    public String getOfferType() {
        return baseIncentive.getOfferType();
    }

    @Override
    public List<DeliveryType> getDeliveryTypes() {
        return baseIncentive.getDeliveryTypes();
    }

    @Override
    public Integer getSignatureId() {
        return baseIncentive.getSignatureId();
    }

    @Override
    public Integer getSignatureHistoryId() {
        return baseIncentive.getSignatureHistoryId();
    }

    @Override
    public String getEndRecipient() {
        return baseIncentive.getEndRecipient();
    }

    @Override
    public String getEligibility() {
        return baseIncentive.getEligibility();
    }

    @Override
    public String getQualification() {
        return baseIncentive.getQualification();
    }

    @Override
    public String getSource() {
        return baseIncentive.getSource();
    }

    @Override
    public String getOrderingCode() {
        return baseIncentive.getOrderingCode();
    }

    @Override
    public Integer getGeographyId() {
        return baseIncentive.getGeographyId();
    }

    @Override
    public String getProgramNumber() {
        return baseIncentive.getProgramNumber();
    }

    @Override
    public String getProgramText() {
        return baseIncentive.getProgramText();
    }

    @Override
    public String getVehicleAttribute() {
        return baseIncentive.getVehicleAttribute();
    }

    @Override
    public Integer getTaxRuleId() {
        return baseIncentive.getTaxRuleId();
    }

    @Override
    public boolean isStackableWith(Incentive incentive) {
        return baseIncentive.isStackableWith(incentive);
    }

    @Override
    public Integer getMarketId() {
        return baseIncentive.getMarketId();
    }

    @Override
    public Integer getGroupAffiliationId() {
        return baseIncentive.getGroupAffiliationId();
    }

    @Override
    public Integer getPreviousOwnershipId() {
        return baseIncentive.getPreviousOwnershipId();
    }

    @Override
    public String getRegion() {
        return baseIncentive.getRegion();
    }

    @Override
    public Integer getEndRecipientId() {
        return baseIncentive.getEndRecipientId();
    }

    @Override
    public String getTaxRule() {
        return baseIncentive.getTaxRule();
    }

    @Override
    public String getPreviousOwnershipDetail() {
        return baseIncentive.getPreviousOwnershipDetail();
    }

    @Override
    public String getGroupAffiliationDetail() {
        return baseIncentive.getGroupAffiliationDetail();
    }
}
