package com.chromedata.incentives.extract.dao.model;

import com.chromedata.incentives.extract.business.model.Incentive;
import com.chromedata.incentives.extract.dao.lookup.model.Category;
import com.chromedata.incentives.extract.dao.lookup.model.DeliveryType;
import com.chromedata.incentives.extract.dao.lookup.model.Division;
import com.chromedata.incentives.extract.dao.lookup.model.EndRecipient;
import com.chromedata.incentives.extract.dao.lookup.model.GroupAffiliation;
import com.chromedata.incentives.extract.dao.lookup.model.IncentiveType;
import com.chromedata.incentives.extract.dao.lookup.model.Institution;
import com.chromedata.incentives.extract.dao.lookup.model.Market;
import com.chromedata.incentives.extract.dao.lookup.model.PreviousOwnership;
import com.chromedata.incentives.extract.dao.lookup.model.TaxRule;
import com.chromedata.incentives.extract.dao.lookup.model.VehicleStatus;
import com.chromedata.incentives.extract.dao.model.lookup.LookupDataSet;

import java.util.List;

public class BatchDataSet {

    private List<? extends Incentive> incentiveList;
    private List<? extends BatchVehicleIncentive> vehicleIncentiveList;
    private List<? extends Vehicle> vehicleList;
    private List<? extends BatchVehicleStyle> vehicleStyleList;
    private List<? extends BatchGeography> geographyList;
    private List<BatchGeographyDetail> geographyDetailList;
    private List<? extends SignatureStackability> stackabilityList;
    private List<com.chromedata.incentives.extract.dao.lookup.model.TaxRule> taxRuleList;

    private List<Category> categoryList;
    private List<DeliveryType> deliveryTypeList;
    private List<EndRecipient> endReceiptList;
    private List<GroupAffiliation> groupAffiliationList;
    private List<Institution> institutionList;
    private List<PreviousOwnership> previousOwnershipList;
    private List<VehicleStatus> vehicleStatusList;
    private List<Market> marketList;
    private List<IncentiveType> incentiveTypeList;
    private List<Division> divisionList;

    private LookupDataSet lookupDataSet;


    public List<? extends Incentive> getIncentiveList() {
        return incentiveList;
    }


    public void setIncentiveList(List<? extends Incentive> incentiveList) {
        this.incentiveList = incentiveList;
    }


    public List<? extends BatchGeography> getGeographyList() {
        return geographyList;
    }


    public void setGeographyList(List<? extends BatchGeography> geographyList) {
        this.geographyList = geographyList;
    }


    public List<BatchGeographyDetail> getGeographyDetailList() {
        return geographyDetailList;
    }


    public void setGeographyDetailList(
            List<BatchGeographyDetail> geographyDetailList) {
        this.geographyDetailList = geographyDetailList;
    }


    public List<? extends BatchVehicleIncentive> getVehicleIncentiveList() {
        return vehicleIncentiveList;
    }


    public void setVehicleIncentiveList(List<? extends BatchVehicleIncentive>
                                            vehicleIncentiveList) {
        this.vehicleIncentiveList = vehicleIncentiveList;
    }


    public List<? extends Vehicle> getVehicleList() {
        return vehicleList;
    }


    public void setVehicleList(List<? extends Vehicle> vehicleList) {
        this.vehicleList = vehicleList;
    }


    public List<? extends BatchVehicleStyle> getVehicleStyleList() {
        return vehicleStyleList;
    }


    public void setVehicleStyleList(List<? extends BatchVehicleStyle> vehicleStyleList) {
        this.vehicleStyleList = vehicleStyleList;
    }


    public List<? extends SignatureStackability> getStackabilityList() {
        return stackabilityList;
    }


    public void setStackabilityList(List<? extends SignatureStackability> stackabilityList) {
        this.stackabilityList = stackabilityList;
    }

    public List<TaxRule> getTaxRuleList() {
        return taxRuleList;
    }

    public void setTaxRuleList(List<com.chromedata.incentives.extract.dao.lookup.model.TaxRule> taxRuleList) {
        this.taxRuleList = taxRuleList;
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(final List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    public List<DeliveryType> getDeliveryTypeList() {
        return deliveryTypeList;
    }

    public void setDeliveryTypeList(final List<DeliveryType> deliveryTypeList) {
        this.deliveryTypeList = deliveryTypeList;
    }

    public List<EndRecipient> getEndReceiptList() {
        return endReceiptList;
    }

    public void setEndReceiptList(final List<EndRecipient> endReceiptList) {
        this.endReceiptList = endReceiptList;
    }

    public List<GroupAffiliation> getGroupAffiliationList() {
        return groupAffiliationList;
    }

    public void setGroupAffiliationList(final List<GroupAffiliation> groupAffiliationList) {
        this.groupAffiliationList = groupAffiliationList;
    }

    public List<Institution> getInstitutionList() {
        return institutionList;
    }

    public void setInstitutionList(final List<Institution> institutionList) {
        this.institutionList = institutionList;
    }

    public List<PreviousOwnership> getPreviousOwnershipList() {
        return previousOwnershipList;
    }

    public void setPreviousOwnershipList(final List<PreviousOwnership> previousOwnershipList) {
        this.previousOwnershipList = previousOwnershipList;
    }

    public List<VehicleStatus> getVehicleStatusList() {
        return vehicleStatusList;
    }

    public void setVehicleStatusList(final List<VehicleStatus> vehicleStatusList) {
        this.vehicleStatusList = vehicleStatusList;
    }

    public List<Market> getMarketList() {
        return marketList;
    }

    public void setMarketList(final List<Market> marketList) {
        this.marketList = marketList;
    }

    public List<IncentiveType> getIncentiveTypeList() {
        return incentiveTypeList;
    }

    public void setIncentiveTypeList(final List<IncentiveType> incentiveTypeList) {
        this.incentiveTypeList = incentiveTypeList;
    }

    public List<Division> getDivisionList() {
        return divisionList;
    }

    public void setDivisionList(final List<Division> divisionList) {
        this.divisionList = divisionList;
    }

    public LookupDataSet getLookupDataSet() {
        return lookupDataSet;
    }

    public void setLookupDataSet(LookupDataSet lookupDataSet) {
        this.lookupDataSet = lookupDataSet;
    }
}
