package com.contribute.xtrct.presentation.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: abhishek.mundewal
 * Date: 11/28/13
 * Time: 1:39 AM
 * To change this template use File | Settings | File Templates.
 */

public class CSVDataSet {

    private List<Incentive> incentiveList = new ArrayList<Incentive>();
    private List<VehicleStatus> vehicleStatusList = new ArrayList<VehicleStatus>();
    private List<ProgramRule> programRuleList = new ArrayList<ProgramRule>();
    private List<Institution> institutionList = new ArrayList<Institution>();
    private List<DeliveryType> deliveryTypeList = new ArrayList<DeliveryType>();
    private List<LURegion> luRegionList = new ArrayList<LURegion>();
    private List<RegionDetail> geoDetailList = new ArrayList<RegionDetail>();
    private List<VehicleIncentive> vehicleIncentiveList = new ArrayList<VehicleIncentive>();
    private List<Vehicle> vehicleList = new ArrayList<Vehicle>();
    private List<VehicleStyle> vehicleStyleList = new ArrayList<VehicleStyle>();
    private List<VehicleVIN> vehicleVINList = new ArrayList<VehicleVIN>();
    private List<Stackability> stackabilityList = new ArrayList<Stackability>();
    private List<ValueVariation> valueVariationList = new ArrayList<ValueVariation>();
    private List<ProgramValue> programValueList = new ArrayList<ProgramValue>();
    private List<Term> termList = new ArrayList<Term>();
    private List<LoanToValue> loanToValues = new ArrayList<>();
    private List<MileageRestriction> mileageRestrictionList = new ArrayList<>();
    private List<TaxRule> taxRuleList = new ArrayList<>();
    private List<LUCategory> luCategoryList = new ArrayList<>();
    private List<LUDeliveryType> luDeliveryTypeList = new ArrayList<>();
    private List<LUEndRecipient> luEndRecipientList = new ArrayList<>();
    private List<LUGroupAffiliation> luGroupAffiliationList = new ArrayList<>();
    private List<LUInstitution> luInstitutionList = new ArrayList<>();
    private List<LUPreviousOwnership> luPreviousOwnershipList = new ArrayList<>();
    private List<LUVehicleStatus> luVehicleStatusList = new ArrayList<>();
    private List<LUMarket> luMarketList= new ArrayList<>();
    private List<LUIncentiveType> luIncentiveTypeList= new ArrayList<>();
    private List<LUDivision> luDivisionList= new ArrayList<>();




    public List<Incentive> getIncentiveList() {
        return incentiveList;
    }

    public List<VehicleStatus> getVehicleStatusList() {
        return vehicleStatusList;
    }

    public List<ProgramRule> getProgramRuleList() {
        return programRuleList;
    }

    public List<Institution> getInstitutionList() {
        return institutionList;
    }

    public List<DeliveryType> getDeliveryTypeList() {
        return deliveryTypeList;
    }

    public List<LURegion> getLuRegionList() {
        return luRegionList;
    }

    public List<RegionDetail> getGeoDetailList() {
        return geoDetailList;
    }

    public List<VehicleIncentive> getVehicleIncentiveList() {
        return vehicleIncentiveList;
    }

    public List<Vehicle> getVehicleList() {
        return vehicleList;
    }

    public List<VehicleStyle> getVehicleStyleList() {
        return vehicleStyleList;
    }

    public List<VehicleVIN> getVehicleVINList() {
        return vehicleVINList;
    }

    public List<Stackability> getStackabilityList() {
        return stackabilityList;
    }

    public List<ValueVariation> getValueVariationList() {
        return valueVariationList;
    }

    public List<ProgramValue> getProgramValueList() {
        return programValueList;
    }

    public List<Term> getTermList() {
        return termList;
    }

    public List<LoanToValue> getLoanToValues() {
        return loanToValues;
    }

    public List<MileageRestriction> getMileageRestrictionList() {
        return mileageRestrictionList;
    }

    public List<TaxRule> getTaxRuleList() {
        return taxRuleList;
    }

    public List<LUCategory> getLuCategoryList() {
        return luCategoryList;
    }

    public List<LUDeliveryType> getLuDeliveryTypeList() {
        return luDeliveryTypeList;
    }

    public List<LUEndRecipient> getLuEndRecipientList() {
        return luEndRecipientList;
    }

    public List<LUGroupAffiliation> getLuGroupAffiliationList() {
        return luGroupAffiliationList;
    }

    public List<LUInstitution> getLuInstitutionList() {
        return luInstitutionList;
    }

    public List<LUPreviousOwnership> getLuPreviousOwnershipList() {
        return luPreviousOwnershipList;
    }

    public List<LUVehicleStatus> getLuVehicleStatusList() {
        return luVehicleStatusList;
    }

    public List<LUMarket> getLuMarketList() {
        return luMarketList;
    }

    public List<LUIncentiveType> getLuIncentiveTypeList() {
        return luIncentiveTypeList;
    }

    public List<LUDivision> getLuDivisionList() {
        return luDivisionList;
    }
}
