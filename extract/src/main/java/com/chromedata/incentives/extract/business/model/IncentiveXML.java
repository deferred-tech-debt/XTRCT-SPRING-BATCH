package com.chromedata.incentives.extract.business.model;

import com.chromedata.eid.aos.incentive.model.CustomDataRow;
import com.chromedata.eid.aos.incentive.model.DataValue;
import com.chromedata.eid.aos.incentive.model.Incentive;
import com.chromedata.eid.aos.incentive.model.Language;
import com.chromedata.eid.aos.incentive.model.VariationType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class IncentiveXML {


    public static final int VARIATION_TYPE_TIER = 1;
    public static final int VARIATION_TYPE_TERM = 2;
    public static final int VARIATION_TYPE_FUTURE_VALUE = 3;
    public static final int VARIATION_TYPE_RANGE = 4;
    public static final int VARIATION_TYPE_AMOUNT = 6;
    public static final int VARIATION_TYPE_ADDITIONAL_VARIANCE = 7;

    public static final int DATA_FIELD_DESCRIPTION = 1;
    public static final int DATA_FIELD_MSRP = 2;
    public static final int DATA_FIELD_DOLLAR_VALUE = 5;
    public static final int DATA_FIELD_UP_TO = 6;
    public static final int DATA_FIELD_RATE = 7;
    public static final int DATA_FIELD_RATE_DISCOUNT = 8;
    public static final int DATA_FIELD_VARIANCE = 9;
    public static final int DATA_FIELD_NUMBER_OF_DAYS = 10;
    public static final int DATA_FIELD_MONEY_FACTOR = 11;
    public static final int DATA_FIELD_MAX_NUMBER_OF_PAYMENTS_WAIVED = 13;
    public static final int DATA_FIELD_MAX_VALUE_OF_PAYMENTS_WAIVED = 14;
    public static final int DATA_FIELD_MAX_VALUE_OF_PAYMENTS_WAIVED_PER_PAYMENT = 15;
    public static final int DATA_FIELD_EXITING_GROUP = 16;
    public static final int DATA_FIELD_LEASES_EXPIRING_FROM = 17;
    public static final int DATA_FIELD_LEASES_EXPIRING_TO = 18;
    public static final int DATA_FIELD_MAX_RESIDUALIZED_MSRP = 23;
    public static final int DATA_FIELD_VALUE_TYPE = 24;
    public static final int DATA_FIELD_VALUE = 25;
    public static final int DATA_FIELD_FEATURE = 26;
    public static final int DATA_FIELD_NOTE = 29;
    public static final int DATA_FIELD_MONEY_FACTOR_DISCOUNT = 30;
    public static final int DATA_FIELD_AMOUNT_FINANCED_FROM = 31;
    public static final int DATA_FIELD_AMOUNT_FINANCED_TO = 32;
    public static final int DATA_FIELD_DEFAULT_MILEAGE_RESTRICTION = 60;
    public static final int DATA_FIELD_DOLLAR_VALUE_EACH = 61;
    public static final int DATA_FIELD_UP_TO_MAX_NUMBER = 62;
    public static final int DATA_FIELD_EARNED_PERIOD_FROM = 63;
    public static final int DATA_FIELD_EARNED_PERIOD_TO = 64;
    public static final int DATA_FIELD_PERCENT = 65;
    public static final int DATA_FIELD_BASE_PRICE_ONLY = 66;
    public static final int DATA_FIELD_LTV_ADJUSTMENTS = 75;
    public static final int DATA_FIELD_LTV_MAX_ADVANCE = 76;
    public static final int DATA_FIELD_LTV_DEFAULT_ADVANCE = 77;
    public static final int DATA_FIELD_LTV_ADD_TO_DEFAULT = 78;
    public static final int DATA_FIELD_LTV_ADJUSTMENT_TYPE = 79;


    private Incentive xml = null;
    private Locale locale;
    private Map<Integer, List<Integer>> variationIDsByType = null;
    private Map<Integer, com.chromedata.eid.aos.incentive.model.Variation> variationsByID = null;
    private Map<Integer, List<DataValue>> dataValuesByField = null;
    private Map<Integer, List<DataValue>> dataValuesByFirstVID = null;

    public IncentiveXML(Incentive xml, Locale locale) {
        this.xml = xml;
        this.locale = locale;
        index();
    }

    public Locale getLocale() {
        return locale;
    }

    private void index() {
        variationIDsByType = new LinkedHashMap<>();
        variationsByID = new HashMap<>();
        dataValuesByField = new HashMap<>();
        dataValuesByFirstVID = new HashMap<>();
        for (VariationType type : xml.getDataCell().getVariationTypeList()) {
            List<Integer> variationIDs = new ArrayList<>();
            for (com.chromedata.eid.aos.incentive.model.Variation variation : type.getVariationList()) {
                variationIDs.add(variation.getVid());
                variationsByID.put(variation.getVid(), variation);
            }
            variationIDsByType.put(type.getCvuid(), variationIDs);
        }
        for (DataValue value : xml.getDataCell().getDataValueList()) {
            List<DataValue> values = dataValuesByField.get(value.getDfuid());
            if (values == null) {
                values = new ArrayList<>();
                dataValuesByField.put(value.getDfuid(), values);
            }
            values.add(value);
            if (!value.getVids().isEmpty()) {
                values = dataValuesByFirstVID.get(value.getVids().get(0));
                if (values == null) {
                    values = new ArrayList<>();
                    dataValuesByFirstVID.put(value.getVids().get(0), values);
                }
                values.add(value);
            }
        }
    }

    public Integer getID() {
        return xml.getIncUid();
    }

    public Integer getVehicleID() {
        return xml.getDataCell().getVehUID();
    }

    public Integer getGeographyID() {
        return xml.getDataCell().getGeoUID();
    }

    public Integer getSignatureID() {
        return xml.getSigUid();
    }

    public Integer getSignatureHistoryID() {
        return xml.getSigHistUid();
    }

    public String getProgramNumber() {
        return xml.getProgramNumber();
    }

    public String getRegionDesc() {
        return getLocalizedMessage(xml.getRegionDescList());
    }

    public Date getLockedDate() {
        return xml.getLockedDate();
    }

    public Date getStartDate() {
        return xml.getStartDate();
    }

    public Date getEndDate() {
        return xml.getEndDate();
    }

    public String getMasterProgram() {
        return getLocalizedMessage(xml.getMasterProgramList());
    }

    public String getProgramDescription() {
        return getLocalizedMessage(xml.getProgramDescList());
    }

    public String getProgramDetail() {
        return getLocalizedMessage(xml.getProgramDetailList());
    }

    public String getFullDescription() {
        return getLocalizedMessage(xml.getFullDescriptionList());
    }

    public String getProgramName() {
        return getLocalizedMessage(xml.getProgramNameList());
    }

    public String getVariationDescription() {
        return getLocalizedMessage(xml.getVariationDescList());
    }

    public String getGenericDescription() {
        return getLocalizedMessage(xml.getGenericDescList());
    }

    public String getOfferType() {
        return getLocalizedMessage(xml.getOfferTypeList());
    }

    public String getPreviousOwnership() {
        return getLocalizedMessage(xml.getPreviousOwnershipList());
    }

    public String getPreviousOwnershipDetail() {
        return getLocalizedMessage(xml.getPreviousOwnershipDetailList());
    }

    public String getGroupAffiliation() {
        return getLocalizedMessage(xml.getGroupAffiliationList());
    }

    public String getGroupAffiliationDetail() {
        return getLocalizedMessage(xml.getGroupAffiliationDetailList());
    }

    public Integer getCategoryID() {
        return xml.getCatID();
    }

    public String getCategoryDescription() {
        return getLocalizedMessage(xml.getCategoryDescList());
    }

    public String getCategoryGroup() {
        return getLocalizedMessage(xml.getCategoryGroupList());
    }

    public String getIncentiveType() {
        return getLocalizedMessage(xml.getIncentiveTypeList());
    }

    public String getMarket() {
        return getLocalizedMessage(xml.getMarketList());
    }

    public String getEndRecipient() {
        return getLocalizedMessage(xml.getEndRecipientList());
    }

    public String getTaxRule() {
        return getLocalizedMessage(xml.getTaxRuleList());
    }

    public String getSource() {
        return getLocalizedMessage(xml.getSourceList());
    }

    public String getOrderingCode() {
        return xml.getOrderingCode();
    }

    public int getTaxRuleId() {
        return xml.getTrUid();
    }

    public List<FinancialInstitution> getInstitutions() {
        return xml.getInstitutionList().stream()
                .map(xmlInstitution -> new FinancialInstitution(xmlInstitution.getItUid(), getLocalizedMessage(xmlInstitution.getInstitutionDescList())))
                .collect(Collectors.toList());
    }

    public List<DeliveryType> getDeliveryTypeList() {
        return xml.getDeliveryTypeList().stream()
                  .map(deliveryType ->
                           new DeliveryType(deliveryType.getDtUid(), getLocalizedMessage(deliveryType.getDeliveryTypeDescList())) )
                  .collect(Collectors.toList());
    }

    public List<VehicleStatus> getVehicleStatusList() {
        return xml.getVehicleStatusList().stream()
                  .map(vehicleStatus -> new VehicleStatus(vehicleStatus.getVsUid(), getLocalizedMessage(vehicleStatus.getVehicleStatusDescList())))
                  .collect(Collectors.toList());
    }

    public String getEligibility() {
        return getLocalizedMessage(xml.getEligibilityList());
    }

    public String getQualification() {
        return getLocalizedMessage(xml.getQualificationList());
    }

    public Set<Integer> getDataVariationTypeIDs() {
        return variationIDsByType.keySet();
    }


    public List<Integer> getVariationIDs(Integer variationTypeID) {
        return variationIDsByType.get(variationTypeID);
    }


    public String getDataValueString(Integer dataFieldID) {
        // May want to do a sanity check to look for VIDs.
        if (dataValuesByField.containsKey(dataFieldID))
            return dataValuesByField.get(dataFieldID).get(0).getValue();
        return null;
    }

    public String getDataValueTranslated(Integer dataFieldID) {
        // May want to do a sanity check to look for VIDs.
        if (dataValuesByField.containsKey(dataFieldID))
            return getLocalizedMessage(dataValuesByField.get(dataFieldID).get(0).getTranslations());
        return null;
    }

    public BigDecimal getDataValueBigDecimal(Integer dataFieldID) {
        String value = getDataValueString(dataFieldID);
        return (value != null) ? new BigDecimal(number(value)) : null;
    }

    public Integer getDataValueInteger(Integer dataFieldID) {
        String value = getDataValueString(dataFieldID);
        return (value != null) ? Integer.valueOf(number(value)) : null;
    }

    public Boolean getDataValueBoolean(Integer dataFieldID) {
        String value = getDataValueString(dataFieldID);
        return (value != null) ? Boolean.valueOf(value) : null;
    }

    public Double getDataValueDouble(Integer dataFieldID) {
        String value = getDataValueString(dataFieldID);
        return toDouble(value);
    }

    public String getDataValueString(Integer dataFieldID, Integer variationID) {
        if (dataValuesByField.containsKey(dataFieldID))
            for (DataValue value : dataValuesByField.get(dataFieldID))
                if (value.getVids().contains(variationID))
                    return value.getValue();
        return null;
    }

    public String getDataValueTranslated(Integer dataFieldID, Integer variationID) {
        if (dataValuesByField.containsKey(dataFieldID))
            for (DataValue value : dataValuesByField.get(dataFieldID))
                if (value.getVids().contains(variationID))
                    return getLocalizedMessage(value.getTranslations());
        return null;
    }

    public Integer getDataValueInteger(Integer dataFieldID, Integer variationID) {
        String value = getDataValueString(dataFieldID, variationID);
        return (value != null) ? Integer.valueOf(number(value)) : null;
    }

    public Integer getVariationOrder(Integer variationId) {
        return variationsByID.get(variationId).getOrder();
    }

    public String getVariationValue(Integer variationID) {
        return variationsByID.get(variationID).getValue();
    }

    public String getVariationValueExpanded(Integer variationID) {
        return variationsByID.get(variationID).getExpandedValue();
    }

    public Set<Integer> getTermVariationIDs(Integer tierVariationID) {
        Set<Integer> ids = new LinkedHashSet<>();
        if (dataValuesByFirstVID.containsKey(tierVariationID))
            for (DataValue value : dataValuesByFirstVID.get(tierVariationID))
                for (int i = 1; i < value.getVids().size(); i++)
                    if (variationIDsByType.get(VARIATION_TYPE_TERM).contains(value.getVids().get(i)))
                        ids.add(value.getVids().get(i));
        return ids;
    }

    public Set<Integer> getFutureValueVariationIDs(Integer tierVariationID, Integer pairedVariationID) {
        Set<Integer> ids = new LinkedHashSet<>();
        if (dataValuesByFirstVID.containsKey(tierVariationID))
            for (DataValue value : dataValuesByFirstVID.get(tierVariationID))
                if (value.getVids().contains(pairedVariationID))
                    for (int i = 1; i < value.getVids().size(); i++) {
                        List<Integer> futureValueVariationIDs = variationIDsByType.get(VARIATION_TYPE_FUTURE_VALUE);
                        if (futureValueVariationIDs != null && futureValueVariationIDs.contains(value.getVids().get(i)))
                            ids.add(value.getVids().get(i));
                    }


        return ids;
    }

    public Set<Integer> getAdditionalVarianceVariationIDs(Integer tierVariationID, Integer pairedVariationID) {
        Set<Integer> ids = new LinkedHashSet<>();
        if (dataValuesByFirstVID.containsKey(tierVariationID))
            for (DataValue value : dataValuesByFirstVID.get(tierVariationID))
                if (value.getVids().contains(pairedVariationID))
                    for (int i = 1; i < value.getVids().size(); i++) {
                        List<Integer> additionalVarianceVariationIDs = variationIDsByType.get(VARIATION_TYPE_ADDITIONAL_VARIANCE);
                        if (additionalVarianceVariationIDs != null && additionalVarianceVariationIDs.contains(value.getVids().get(i)))
                            ids.add(value.getVids().get(i));
                    }
        return ids;
    }

    public List<DataValue> getDataValues(int dataFieldID, Integer variationID, Set<Integer> pairedVariationIDs) {
        List<DataValue> dataValues = new ArrayList<>();
        if (dataValuesByFirstVID.containsKey(variationID)) {
            for (DataValue value : dataValuesByFirstVID.get(variationID)) {
                if ((value.getDfuid() == dataFieldID) && value.getVids().containsAll(pairedVariationIDs)) {
                    dataValues.add(value);
                }
            }
        }
        return dataValues;
    }

    public String getDataValueString(int dataFieldID, Integer variationID, Set<Integer> pairedVariationIDs) {
        if (dataValuesByFirstVID.containsKey(variationID))
            for (DataValue value : dataValuesByFirstVID.get(variationID))
                if ((value.getDfuid() == dataFieldID) && value.getVids().containsAll(pairedVariationIDs))
                    return value.getValue();
        return null;
    }

    public BigDecimal getDataValueBigDecimal(int dataFieldID, Integer variationID, Set<Integer> pairedVariationIDs) {
        String value = getDataValueString(dataFieldID, variationID, pairedVariationIDs);
        return toBigDecimal(value);
    }

    public Double getDataValueDouble(int dataFieldID, Integer variationID, Set<Integer> pairedVariationIDs) {
        String value = getDataValueString(dataFieldID, variationID, pairedVariationIDs);
        return toDouble(value);
    }

    public String getCustomDataRowDescription(int customRowUID) {
        CustomDataRow customDataRow = getCustomDataRow(customRowUID);
        return customDataRow != null ? customDataRow.getDescription() : null;
    }

    private CustomDataRow getCustomDataRow(int customRowUID) {
        if (null == xml || null == xml.getDataCell() || null == xml.getDataCell().getCustomDataRowList()) {
            return null;
        }
        return xml.getDataCell().getCustomDataRowList().stream()
                .filter(customDataRow -> customDataRow.getCustomRowUID() == customRowUID)
                .findAny()
                .orElse(null);
    }

    public Double toDouble(String value) {
        return (value != null) ? Double.valueOf(number(value)) : null;
    }

    public BigDecimal toBigDecimal(String value) {
        return (value != null) ? new BigDecimal(number(value)) : null;
    }

    public String getFutureValue(Integer variationID, Set<Integer> pairedVariationIDs) {
        if (variationIDsByType.containsKey(VARIATION_TYPE_FUTURE_VALUE))
            for (DataValue value : dataValuesByFirstVID.get(variationID))
                if (value.getDfuid() == DATA_FIELD_RATE)
                    for (Integer futureValueVariationID : variationIDsByType.get(VARIATION_TYPE_FUTURE_VALUE))
                        if (value.getVids().contains(futureValueVariationID) && value.getVids().containsAll(pairedVariationIDs))
                            return variationsByID.get(futureValueVariationID).getValue();
        return null;
    }

    private String number(String value) {
        StringBuilder number = new StringBuilder(value);
        for (int i = 0; i < number.length(); i++) {
            char character = number.charAt(i);
            if (((character >= '0') && (character <= '9')) || (character == '.') || (character == '-'))
                continue;
            number.deleteCharAt(i--);
        }
        return number.toString();
    }

    // Logic string from database
    public String getVehicleAttribute() {
        return xml.getLogic();
    }

    // Text description of logic string
    public String getRequirements() {
        return getLocalizedMessage(xml.getLogicDescList());
    }

    private String getLocalizedMessage(List<Language> translations) {
        final Locale defaultLocale = Locale.US;

        return getMessageForLocale(locale, translations)
                .orElse(getMessageForLocale(defaultLocale, translations)
                        .orElse(""));
    }

    private Optional<String> getMessageForLocale(Locale locale, List<Language> translations) {
        Optional<String> localizedMessage = Optional.empty();

        if (translations != null) {
            localizedMessage = translations.stream()
                    .filter(translation -> locale.getLanguage().equalsIgnoreCase(translation.getCode()))
                    .filter(translation -> translation.getValue() != null && !translation.getValue().isEmpty())
                    .map(Language::getValue)
                    .findAny();
        }

        return localizedMessage;
    }

    public Integer getMarketId() {
        return xml.getMktUid();
    }

    public Integer getGroupAffiliationId() {
        return xml.getGaUid();
    }

    public Integer getPreviousOwnershipId() {
        return xml.getPoUid();
    }

    public Integer getEndRecipientId() {
        return xml.getErUid();
    }
}
