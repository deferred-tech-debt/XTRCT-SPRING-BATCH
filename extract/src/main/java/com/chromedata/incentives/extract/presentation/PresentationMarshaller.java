package com.chromedata.incentives.extract.presentation;

import com.chromedata.incentives.extract.dao.FilterValuesDao;
import com.chromedata.incentives.extract.dao.lookup.model.Category;
import com.chromedata.incentives.extract.dao.lookup.model.Division;
import com.chromedata.incentives.extract.dao.lookup.model.EndRecipient;
import com.chromedata.incentives.extract.dao.lookup.model.GroupAffiliation;
import com.chromedata.incentives.extract.dao.lookup.model.PreviousOwnership;
import com.chromedata.incentives.extract.dao.model.BatchGeographiesDetail;
import com.chromedata.incentives.extract.dao.model.BatchGeography;
import com.chromedata.incentives.extract.dao.model.BatchGeographyDetail;
import com.chromedata.incentives.extract.dao.model.BatchVehicleIncentive;
import com.chromedata.incentives.extract.dao.model.BatchVehicleStyle;
import com.chromedata.incentives.extract.dao.model.IncentiveType;
import com.chromedata.incentives.extract.dao.model.SignatureStackability;
import com.chromedata.incentives.extract.dao.model.lookup.LookupDataSet;
import com.chromedata.incentives.extract.presentation.model.CSVDataSet;
import com.chromedata.incentives.extract.presentation.model.DeliveryType;
import com.chromedata.incentives.extract.presentation.model.Incentive;
import com.chromedata.incentives.extract.presentation.model.Institution;
import com.chromedata.incentives.extract.presentation.model.LUCategory;
import com.chromedata.incentives.extract.presentation.model.LUDeliveryType;
import com.chromedata.incentives.extract.presentation.model.LUDivision;
import com.chromedata.incentives.extract.presentation.model.LUEndRecipient;
import com.chromedata.incentives.extract.presentation.model.LUGroupAffiliation;
import com.chromedata.incentives.extract.presentation.model.LUIncentiveType;
import com.chromedata.incentives.extract.presentation.model.LUInstitution;
import com.chromedata.incentives.extract.presentation.model.LUMarket;
import com.chromedata.incentives.extract.presentation.model.LUPreviousOwnership;
import com.chromedata.incentives.extract.presentation.model.LURegion;
import com.chromedata.incentives.extract.presentation.model.LUVehicleStatus;
import com.chromedata.incentives.extract.presentation.model.LoanToValue;
import com.chromedata.incentives.extract.presentation.model.MileageRestriction;
import com.chromedata.incentives.extract.presentation.model.ProgramRule;
import com.chromedata.incentives.extract.presentation.model.ProgramValue;
import com.chromedata.incentives.extract.presentation.model.RegionDetail;
import com.chromedata.incentives.extract.presentation.model.Stackability;
import com.chromedata.incentives.extract.presentation.model.TaxRule;
import com.chromedata.incentives.extract.presentation.model.Term;
import com.chromedata.incentives.extract.presentation.model.ValueVariation;
import com.chromedata.incentives.extract.presentation.model.Vehicle;
import com.chromedata.incentives.extract.presentation.model.VehicleIncentive;
import com.chromedata.incentives.extract.presentation.model.VehicleStatus;
import com.chromedata.incentives.extract.presentation.model.VehicleStyle;
import org.springframework.context.MessageSource;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import java.util.stream.Collectors;

public class PresentationMarshaller {

    private CSVDataSet data;
    private final Locale locale;
    private final IncentiveType cashIncentiveType;
    private final IncentiveType giftAwardIncentiveType;
    private final MessageSource extractMessages;

    public PresentationMarshaller(Locale locale,
                                  MessageSource extractMessages,
                                  FilterValuesDao filterValuesDao) {
        data = new CSVDataSet();
        this.locale = locale;
        this.extractMessages = extractMessages;

        // cache is done at mybatis query
        cashIncentiveType = filterValuesDao.getIncentiveType(com.chromedata.incentives.extract.business.model.IncentiveType.CASH.getCode(), locale.getLanguage());
        giftAwardIncentiveType = filterValuesDao.getIncentiveType(com.chromedata.incentives.extract.business.model.IncentiveType.GIFT_AWARD.getCode(), locale.getLanguage());
    }

    public void resetData() {
        data = new CSVDataSet();
    }

    public void addIncentive(com.chromedata.incentives.extract.business.model.Incentive from) {
        Incentive to = new Incentive();
        to.setIncentiveID(new Integer(from.getId()));
        to.setGeographyID(from.getGeographyId());
        to.setSignatureHistoryID(from.getSignatureHistoryId());
        to.setSignatureID(from.getSignatureId());
        to.setCategoryID(new Integer(from.getCategoryId()));
        to.setCategoryGroup(from.getCategoryGroup());
        to.setCategoryDescription(from.getCategoryDescription());
        to.setProgramDescription(from.getName());
        to.setProgramNumber(from.getProgramNumber());
        to.setMarket(from.getMarket());
        to.setType(from.getIncentiveType());
        to.setGroupAffiliation(from.getGroupAffiliation());
        to.setEffectiveDate(convertDate(from.getEffectiveDate()));
        to.setExpiryDate(convertDate(from.getExpiryDate()));
        to.setPreviousOwnership(from.getPreviousOwnership());
        to.setVehicleAttribute(from.getVehicleAttribute());
        to.setProgramText(from.getProgramText());
        to.setEndRecipient(from.getEndRecipient());
        to.setSource(from.getSource());
        to.setOrderingCode(from.getOrderingCode());
        to.setTaxRuleId(from.getTaxRuleId());

        to.setCategoryGroup(from.getCategoryGroup());
        to.setMarketId(from.getMarketId());
        to.setGroupAffiliationId(from.getGroupAffiliationId());
        to.setPreviousOwnershipId(from.getPreviousOwnershipId());
        to.setEndRecipientId(from.getEndRecipientId());

        to.setGroupAffiliationDetail(from.getGroupAffiliationDetail());
        to.setPreviousOwnershipDetail(from.getPreviousOwnershipDetail());

        data.getIncentiveList().add(to);
        addVehicleStatusList(from);
        addProgramRuleList(from);
        addInstitutionList(from);
        addDeliveryTypeList(from);
        addVariations(from);
    }

    private LocalDate convertDate(Date date) {
        Instant instant = Instant.ofEpochMilli(date.getTime());
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
    }


    private void addVehicleStatusList(com.chromedata.incentives.extract.business.model.Incentive incentive) {
        for (com.chromedata.incentives.extract.business.model.VehicleStatus from : incentive.getVehicleStatuses()) {
            VehicleStatus to = new VehicleStatus();
            to.setIncentiveID(new Integer(incentive.getId()));
            to.setStatus(from.getDescription());
            to.setId(from.getId());
            data.getVehicleStatusList().add(to);
        }
    }


    private void addProgramRuleList(com.chromedata.incentives.extract.business.model.Incentive incentive) {
        ProgramRule rule;
        if ((incentive.getEligibility() != null) && !incentive.getEligibility().isEmpty()) {
            rule = new ProgramRule();
            rule.setIncentiveID(new Integer(incentive.getId()));
            rule.setType(extractMessages.getMessage("program.rule.eligibility", null, locale));
            rule.setDescription(incentive.getEligibility());
            data.getProgramRuleList().add(rule);
        }
        if ((incentive.getQualification() != null) && !incentive.getQualification().isEmpty()) {
            rule = new ProgramRule();
            rule.setIncentiveID(new Integer(incentive.getId()));
            rule.setType(extractMessages.getMessage("program.rule.qualification", null, locale));
            rule.setDescription(incentive.getQualification());
            data.getProgramRuleList().add(rule);
        }
    }


    private void addInstitutionList(com.chromedata.incentives.extract.business.model.Incentive incentive) {
        for (com.chromedata.incentives.extract.business.model.FinancialInstitution from : incentive.getFinancialInstitutionList()) {
            Institution to = new Institution();
            to.setIncentiveID(new Integer(incentive.getId()));
            to.setInstitutionID(from.getId());
            to.setInstitution(from.getDescription());
            data.getInstitutionList().add(to);
        }
    }


    private void addDeliveryTypeList(com.chromedata.incentives.extract.business.model.Incentive incentive) {
        for (com.chromedata.incentives.extract.business.model.DeliveryType from: incentive.getDeliveryTypes()) {
            DeliveryType to = new DeliveryType();
            to.setIncentiveID(new Integer(incentive.getId()));
            to.setType(from.getDescription());
            to.setDeliveryTypeId(from.getId());
            data.getDeliveryTypeList().add(to);
        }
    }


    public void addVariations(com.chromedata.incentives.extract.business.model.Incentive incentive) {
        int subID = 0;
        for (com.chromedata.incentives.extract.business.model.Variation from : incentive.getVariationList()) {
            String pseudoID = incentive.getId() + "-" + subID++;
            ValueVariation to = new ValueVariation();
            to.setValueVariationID(pseudoID);
            to.setIncentiveID(new Integer(incentive.getId()));
            to.setRequirements(from.getRequirement());
            to.setTierStart(from.getTierStart());
            to.setTierEnd(from.getTierEnd());
            to.setTiers(from.getTiers());
            data.getValueVariationList().add(to);
            addProgramValue(pseudoID, pseudoID, from, incentive.getIncentiveType());
        }
    }


    private void addProgramValue(String parentID, String programValueId, com.chromedata.incentives.extract.business.model.Variation from, String incType) {
        ProgramValue to = new ProgramValue();
        to.setProgramValueID(programValueId);
        to.setValueVariationID(parentID);
        if (from.getTermList() != null) {
            int idCounter = 0;
            if (from.getTermList().isEmpty() && null != from.getValue()) {
                to.setCash(from.getValue().intValue());
            } else {
                for (com.chromedata.incentives.extract.business.model.Term term : from.getTermList()) {
                    String termPseudoId = programValueId + "-" + idCounter++;
                    addTerm(programValueId, termPseudoId, term);
                }
            }
        }
        else if ((incType != null) && (from.getValue() != null)
                && (cashIncentiveType.getDescription().equals(incType) || giftAwardIncentiveType.getDescription().equals(incType))) {
            to.setCash(from.getValue().intValue());
        }
        if (Boolean.TRUE.equals(from.getUpTo())) {
            to.setUpTo(true);
        }
        to.setMaxNumberOfPaymentsWaived(from.getMaxNumberOfPaymentsWaived());
        to.setMaxValueOfPaymentsWaived(from.getMaxValueOfPaymentsWaived());
        to.setMaxValueOfPaymentsWaivedPerPayment(from.getMaxValueOfPaymentsWaivedPerPayment());
        to.setExitingGroup(from.getExitingGroup());
        to.setLeasesExpiringFrom(from.getLeasesExpiringFrom());
        to.setLeasesExpiringTo(from.getLeasesExpiringTo());
        to.setDescription(from.getDescription());
        to.setNote(from.getNote());
        to.setNumberOfDays(from.getNumberOfDays());
        to.setMrm(from.getMaximumResidualizableMsrp());
        to.setPercent(from.getPercent());
        to.setBasePriceOnly(from.getBasePriceOnly());
        to.setDollarValueEach(from.getDollarValueEach());
        to.setUpToMaxNumber(from.getUpToMaxNumber());
        to.setEarnedPeriodFrom(from.getEarnedPeriodFrom());
        to.setEarnedPeriodTo(from.getEarnedPeriodTo());
        data.getProgramValueList().add(to);
    }


    private void addTerm(String programValueId, String termId, com.chromedata.incentives.extract.business.model.Term from) {
        Term to = new Term();
        to.setProgramValueId(programValueId);
        to.setTermId(termId);
        to.setFrom(from.getFrom());
        to.setTo(from.getTo());
        to.setValue(from.getValue());
        to.setValueType(from.getValueType());
        if ((from.getVariance() != null) && (from.getVariance() != 0)) {
            to.setVariance(from.getVariance());
        }
        to.setFutureValue(from.getFutureValue());
        to.setAmountFinancedFrom(from.getAmountFinancedFrom());
        to.setAmountFinancedTo(from.getAmountFinancedTo());
        to.setFinancialDisclosure(from.getFinancialDisclosure());

        if (from.getMileageRestrictions() != null && !from.getMileageRestrictions().isEmpty()) {
            from.getMileageRestrictions().forEach(mileageRestriction -> addMileageRestriction(termId, mileageRestriction));
        }

        if (!CollectionUtils.isEmpty(from.getAdvances())) {
            from.getAdvances().forEach(advance -> addAdvance(termId, advance));
        }

        data.getTermList().add(to);
    }

    private void addMileageRestriction(String termId, com.chromedata.incentives.extract.business.model.MileageRestriction from) {
        // Only carry the value if its true.  Otherwise use null.
        Boolean isDefault = from.isDefault() ? from.isDefault() : null;
        data.getMileageRestrictionList().add(new MileageRestriction(termId, from.getMileage(), from.getResidual(), isDefault));
    }

    private void addAdvance(String termId, com.chromedata.incentives.extract.business.model.Advance from) {
        // Only carry the value if its true.  Otherwise use null.
        Boolean isDefault = from.isDefault() ? from.isDefault() : null;
        data.getLoanToValues().add(new LoanToValue(termId, from.getLtvRange(), from.getLtvValue(), from.getMaxAdvance() , isDefault));
    }

    public void addVehicleIncentive(BatchVehicleIncentive from) {
        VehicleIncentive to = new VehicleIncentive();
        to.setIncentiveID(from.getIncentiveID());
        to.setAcode(from.getAcode());
        data.getVehicleIncentiveList().add(to);
    }


    public void addVehicle(com.chromedata.incentives.extract.dao.model.Vehicle from) {
        Vehicle to = new Vehicle();
        to.setAcode(from.getAcode());
        to.setCountry(from.getCountry());
        to.setDivision(from.getDivision());
        to.setYear(from.getYear());
        to.setModel(from.getModel());
        to.setTrim(from.getTrim());
        to.setVariation(from.getVariation());
        data.getVehicleList().add(to);
    }


    public void addVehicleStyle(BatchVehicleStyle from) {
        VehicleStyle to = new VehicleStyle();
        to.setAcode(from.getAcode());
        data.getVehicleStyleList().add(to);
        to.setStyleID(from.getStyleID());
    }


    public void addGeography(BatchGeography from) {
        LURegion to = new LURegion();
        to.setCountry(from.getCountry());
        to.setDescription(from.getDescription());
        to.setGeographyID(from.getGeographyId());
        data.getLuRegionList().add(to);
    }


    public void addGeoDetail(BatchGeographyDetail from) {
        RegionDetail to = new RegionDetail();
        to.setGeographyID(from.getGeographyId());
        to.setZipPostalPattern(from.getZipPostalPattern());
        data.getGeoDetailList().add(to);
    }

    public void addGeographyDetail(BatchGeographiesDetail from) {
        RegionDetail to = new RegionDetail();
        to.setGeographyID(from.getGeographyId());
        to.setZipPostalPattern(from.getZipPostalPattern());
        data.getGeoDetailList().add(to);
    }


    public void addStackability(SignatureStackability from) {
        Stackability to = new Stackability();
        to.setComplexLogic(from.getComplexLogic());
        to.setRelationshipType(from.getRelationType().getRelationTypeCode());
        to.setSignatureHistoryID(new Integer(from.getFromSignatureHistoryId()));
        to.setToSignatureID(new Integer(from.getToSignatureId()));
        data.getStackabilityList().add(to);
    }


    public void addTaxRule(com.chromedata.incentives.extract.dao.lookup.model.TaxRule from) {
        data.getTaxRuleList().add(new TaxRule(from.getId(), from.getDescription()));
    }

    public void addCategory(Category from) {
        data.getLuCategoryList().add(new LUCategory(from.getId(), from.getGroup(), from.getDescription()));
    }

    public void addDeliveryType(com.chromedata.incentives.extract.dao.lookup.model.DeliveryType from) {
        data.getLuDeliveryTypeList().add(new LUDeliveryType(from.getId(), from.getDescription()));
    }

    public void addEndReceipt(EndRecipient from) {
        data.getLuEndRecipientList().add(new LUEndRecipient(from.getId(), from.getDescription()));
    }

    public void addGroupAffiliation(GroupAffiliation from) {
        data.getLuGroupAffiliationList().add(new LUGroupAffiliation(from.getId(), from.getDescription()));
    }

   public void addInstitution(com.chromedata.incentives.extract.dao.lookup.model.Institution from) {
        data.getLuInstitutionList().add(new LUInstitution(from.getId(),  from.getDescription()));
    }

    public void addPreviousOwnership(PreviousOwnership from) {
        data.getLuPreviousOwnershipList().add(new LUPreviousOwnership(from.getId(), from.getDescription()));
    }

    public void addVehicleStatus(com.chromedata.incentives.extract.dao.lookup.model.VehicleStatus from) {
        data.getLuVehicleStatusList().add(new LUVehicleStatus(from.getId(), from.getDescription()));
    }

    public void addMarket(final com.chromedata.incentives.extract.dao.lookup.model.Market from) {
        data.getLuMarketList().add(new LUMarket(from.getId(), from.getDescription()));
    }

    public void addIncentiveType(final com.chromedata.incentives.extract.dao.lookup.model.IncentiveType from) {
        data.getLuIncentiveTypeList().add(new LUIncentiveType(from.getId(), from.getDescription()));
    }

    public void addDivision(final Division from) {
        data.getLuDivisionList().add(new LUDivision(from.getId(), from.getDescription()));
    }

    public void addLookupData(LookupDataSet lookupDataSet) {
        data.getLuGroupAffiliationList().addAll(lookupDataSet.getGroupAffiliations().stream()
                                                           .map(from -> new LUGroupAffiliation(from.getId(), from.getDescription()))
                                                           .collect(Collectors.toList()));

        data.getLuPreviousOwnershipList().addAll(lookupDataSet.getPreviousOwnerships().stream()
                                                            .map(from -> new LUPreviousOwnership(from.getId(), from.getDescription()))
                                                            .collect(Collectors.toList()));

        data.getLuDeliveryTypeList().addAll(lookupDataSet.getDeliveryTypes().stream()
                                                       .map(from -> new LUDeliveryType(from.getId(), from.getDescription()))
                                                       .collect(Collectors.toList()));

        data.getLuCategoryList().addAll(lookupDataSet.getCategories().stream()
                                                   .map(from -> new LUCategory(from.getId(), from.getGroup(), from.getDescription()))
                                                   .collect(Collectors.toList()));

        data.getLuDivisionList().addAll(lookupDataSet.getDivisions().stream()
                                                   .map(from -> new LUDivision(from.getCode(), from.getDescription()))
                                                   .collect(Collectors.toList()));

        data.getLuInstitutionList().addAll(lookupDataSet.getFinancialInstitutions().stream()
                                                      .map(from -> new LUInstitution(from.getId(), from.getDescription()))
                                                      .collect(Collectors.toList()));

        data.getLuIncentiveTypeList().addAll(lookupDataSet.getIncentiveTypes().stream()
                                                        .map(from -> new LUIncentiveType(from.getCode(), from.getDescription()))
                                                        .collect(Collectors.toList()));

        data.getLuMarketList().addAll(lookupDataSet.getMarkets().stream()
                                                 .map(from -> new LUMarket(from.getId(), from.getDescription()))
                                                 .collect(Collectors.toList()));

        data.getLuEndRecipientList().addAll(lookupDataSet.getEndRecipients().stream()
                                                       .map(from -> new LUEndRecipient(from.getId(), from.getDescription()))
                                                       .collect(Collectors.toList()));

        data.getLuVehicleStatusList().addAll(lookupDataSet.getVehicleStatuses().stream()
                                                        .map(from -> new LUVehicleStatus(from.getId(), from.getDescription()))
                                                        .collect(Collectors.toList()));

        data.getTaxRuleList().addAll(lookupDataSet.getTaxRules().stream()
                                                  .map(from -> new TaxRule(from.getId(), from.getDescription()))
                                                  .collect(Collectors.toList()));
    }

    public CSVDataSet getData() {
        return data;
    }
}
