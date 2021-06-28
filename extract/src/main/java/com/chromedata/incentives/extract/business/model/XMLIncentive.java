package com.chromedata.incentives.extract.business.model;

import com.chromedata.incentives.extract.dao.model.AugmentedIncentiveData;

import java.util.Date;
import java.util.List;

public class XMLIncentive implements Incentive {

    private IncentiveXML xml = null;
    private XMLVariationList variations = null;
    private AugmentedIncentiveData augmentedIncentiveData;

    public XMLIncentive(IncentiveXML xml, XMLVariationList variations, AugmentedIncentiveData augmentedIncentiveData) {
        this.xml = xml;
        this.variations = variations;
        this.augmentedIncentiveData = augmentedIncentiveData;
    }

    public String getId() {
        return xml.getID().toString();
    }

    public String getIncentiveType() {
        return xml.getIncentiveType();
    }

    public List<FinancialInstitution> getFinancialInstitutionList() {
        return xml.getInstitutions();
    }

    public List<Variation> getVariationList() {
        return variations;
    }

    public String getCategoryId() {
        return xml.getCategoryID().toString();
    }

    public String getCategoryDescription() {
        return xml.getCategoryDescription();
    }

    /**
     * Returns the consumer friendly name of this incentive.
     * <br/><br/>
     * By default, it will attempt to use the "ProgramName" field.  If that field is not populated, it will default
     * to the "ProgramDescription" field.
     *
     * @return String representing the name of this incentive
     */
    public String getName() {
        String name = xml.getProgramName();

        if (name == null || name.isEmpty()) {
            name = xml.getProgramDescription();
        }

        return name;
    }

    public String getMarket() {
        return xml.getMarket();
    }

    public String getGroupAffiliation() {
        return xml.getGroupAffiliation();
    }

    public Date getEffectiveDate() {
        return xml.getStartDate();
    }

    public Date getExpiryDate() {
        return xml.getEndDate();
    }

    public String getPreviousOwnership() {
        return xml.getPreviousOwnership();
    }

    @Override
    public List<VehicleStatus> getVehicleStatuses() {
        return xml.getVehicleStatusList();
    }

    public String getOfferType() {
        return xml.getOfferType();
    }

    @Override
    public List<DeliveryType> getDeliveryTypes() {
        return xml.getDeliveryTypeList();
    }

    public Integer getSignatureId() {
        return xml.getSignatureID();
    }

    public Integer getSignatureHistoryId() {
        return xml.getSignatureHistoryID();
    }

    public String getEndRecipient() {
        return xml.getEndRecipient();
    }

    public String getEligibility() {
        return xml.getEligibility();
    }

    public String getQualification() {
        return xml.getQualification();
    }

    public String getSource() {
        return xml.getSource();
    }

    public String getOrderingCode() {
        return xml.getOrderingCode();
    }

    public Integer getGeographyId() {
        return xml.getGeographyID();
    }

    public String getProgramNumber() {
        return xml.getProgramNumber();
    }

    public String getProgramText() {
        return xml.getProgramDetail();
    }

    public String getVehicleAttribute() {
        return xml.getVehicleAttribute();
    }

    @Override
    public Integer getTaxRuleId() {
        return xml.getTaxRuleId();
    }

    @Override
    public String getCategoryGroup() {
        return xml.getCategoryGroup();
    }

    @Override
    public Integer getMarketId() {
        return xml.getMarketId();
    }

    @Override
    public Integer getGroupAffiliationId() {
        return xml.getGroupAffiliationId();
    }

    @Override
    public Integer getPreviousOwnershipId() {
        return xml.getPreviousOwnershipId();
    }

    @Override
    public String getRegion() {
        return xml.getRegionDesc();
    }

    @Override
    public Integer getEndRecipientId() {
        return xml.getEndRecipientId();
    }

    @Override
    public String getTaxRule() {
        return xml.getTaxRule();
    }

    @Override
    public String getGroupAffiliationDetail() {
        return xml.getGroupAffiliationDetail();
    }

    @Override
    public String getPreviousOwnershipDetail() {
        return xml.getPreviousOwnershipDetail();
    }

    @Override
    public boolean isStackableWith(Incentive incentive) {
        throw new UnsupportedOperationException("This operation is not supported for the " + this.getClass().getName()
                + " incentive type.  Please wrap this incentive in a " + StackableIncentive.class.getName());
    }
}
