package com.contribute.xtrct.business.model;

import com.chromedata.eid.aos.incentive.model.DataValue;
import com.contribute.xtrct.business.util.IncentiveLoanCalculator;
import com.contribute.xtrct.dao.model.ExtractWarning;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class XMLVariationList extends ArrayList<Variation> {

    private static final long serialVersionUID = -343413648433860653L;
    private transient IncentiveXML xml = null;
    private transient IncentiveLoanCalculator loanCalculator;
    private transient Integer ltvLeaseRateMoneyFactorDivider;
    private static final int[] CASH_CAT_IDS = {121, 123, 124, 130, 131, 132, 135, 136, 151};
    private Set<ExtractWarning> warnings;

    public XMLVariationList(IncentiveXML xml, IncentiveLoanCalculator loanCalculator, final Integer ltvLeaseRateMoneyFactorDivider) {
        this.xml = xml;
        this.loanCalculator = loanCalculator;
        this.ltvLeaseRateMoneyFactorDivider = ltvLeaseRateMoneyFactorDivider;
        build();
    }

    public Set<ExtractWarning> getWarnings() {
        return warnings;
    }


    private void build() {
        for (Integer variationTypeID : xml.getDataVariationTypeIDs()) {
            switch (variationTypeID) {
                case IncentiveXML.VARIATION_TYPE_TIER:
                    xml.getVariationIDs(variationTypeID).stream()
                            .map(this::buildTierVariation)
                            .sorted(Comparator.comparing(Variation::getOrder)).forEach(this::add);

                    break;
                // Are there other types that should become variations?  We don't know.
            }
        }
        if (isEmpty())
            addDefaultVariation();
    }


    private Variation buildTierVariation(Integer variationID) {
        Variation tier = new Variation();
        tier.setDescription(xml.getDataValueTranslated(IncentiveXML.DATA_FIELD_DESCRIPTION, variationID));
        tier.setNote(xml.getDataValueTranslated(IncentiveXML.DATA_FIELD_NOTE, variationID));
        tier.setRequirement(xml.getRequirements());   //xml.getDataValue(DATA_FIELD_FEATURE, variationID));
        tier.setOrder(xml.getVariationOrder(variationID));
        String value = xml.getVariationValue(variationID);
        int index = value.indexOf('-');
        if (index != -1) {
            tier.setTierStart(value.substring(0, index));
            tier.setTierEnd(value.substring(index + 1));
        } else {
            tier.setTierStart(value);
            tier.setTierEnd(value);
        }
        tier.setTiers(xml.getVariationValueExpanded(variationID));
        tier.setNumberOfDays(xml.getDataValueInteger(IncentiveXML.DATA_FIELD_NUMBER_OF_DAYS, variationID));
        tier.setTermList(new ArrayList<>());
        for (Integer termVariationID : xml.getTermVariationIDs(variationID)) {
            tier.getTermList().addAll(createTierTerms(variationID, termVariationID));
        }
        // INCWEBSVC-1052 :  Fix value for Cash/Dealer Cash incentives that don't have a term
        if (tier.getTermList().size() == 0 && isCash(xml.getCategoryID())) {
            tier.setValue(xml.getDataValueBigDecimal(IncentiveXML.DATA_FIELD_MSRP, variationID, Collections.singleton(variationID)));
        }
        return tier;
    }

    /**
     * Determines if the supplied category ID is in the list of IDs for CASH or DEALER CASH types.
     * Using IDs because we can't search for CASH in the string when using French.
     *
     * @param categoryID
     * @return
     */
    private boolean isCash(Integer categoryID) {
        for (int cashId : CASH_CAT_IDS) {
            if (categoryID.intValue() == cashId) {
                return true;
            }
        }
        return false;
    }

    private List<Term> createTierTerms(Integer variationID, Integer termVariationID) {

        final Integer dummyVariation = -999999;

        List<Term> tierTerms = new ArrayList<>();

        Set<Integer> futureValueVariationIDs = xml.getFutureValueVariationIDs(variationID, termVariationID);
        if( futureValueVariationIDs == null || futureValueVariationIDs.isEmpty() ){
            futureValueVariationIDs = new HashSet<>();
            futureValueVariationIDs.add(dummyVariation); // we need it to loop at least once
        }

        Set<Integer> additionalVarianceVariationIDs = xml.getAdditionalVarianceVariationIDs(variationID, termVariationID);
        if( additionalVarianceVariationIDs == null || additionalVarianceVariationIDs.isEmpty() ){
            additionalVarianceVariationIDs = new HashSet<>();
            additionalVarianceVariationIDs.add(dummyVariation);  // we need it to loop at least once
        }

        for( Integer futureValueVariationID : futureValueVariationIDs ){
            for( Integer additionalVarianceVariationID : additionalVarianceVariationIDs ){
                Set<Integer> pairedVariationIDs = new LinkedHashSet<>();
                if( !futureValueVariationID.equals(dummyVariation) ){
                    pairedVariationIDs.add(futureValueVariationID);
                }
                if( !additionalVarianceVariationID.equals(dummyVariation) ){
                    pairedVariationIDs.add(additionalVarianceVariationID);
                }
                pairedVariationIDs.add(termVariationID);
                Term term = createTierTerm(variationID, termVariationID, pairedVariationIDs);
                if (term.hasValue()) {
                    tierTerms.add(term);
                }
            }
        }

        return tierTerms;
    }

    private Term createTierTerm(Integer variationID, Integer termVariationID, Set<Integer> pairedVariationIDs) {
        Term term = new Term();
        String value = xml.getVariationValue(termVariationID);
        int index = value.indexOf('-');
        if (index != -1) {
            term.setFrom(Integer.parseInt(value.substring(0, index)));
            term.setTo(Integer.parseInt(value.substring(index + 1)));
        } else {
            term.setFrom(Integer.parseInt(value));
            term.setTo(Integer.parseInt(value));
        }
        switch (xml.getCategoryID()) {
            case 101:
                BigDecimal financeRate = xml.getDataValueBigDecimal(IncentiveXML.DATA_FIELD_RATE, variationID, pairedVariationIDs);
                term.setValue(financeRate);
                term.setFutureValue(xml.getFutureValue(variationID, pairedVariationIDs));
                term.setAmountFinancedFrom(xml.getDataValueBigDecimal(IncentiveXML.DATA_FIELD_AMOUNT_FINANCED_FROM, variationID, pairedVariationIDs));
                term.setAmountFinancedTo(xml.getDataValueBigDecimal(IncentiveXML.DATA_FIELD_AMOUNT_FINANCED_TO, variationID, pairedVariationIDs));
                term.setVariance(xml.getDataValueDouble(IncentiveXML.DATA_FIELD_VARIANCE, variationID, pairedVariationIDs));
                term.setFinancialDisclosure(loanCalculator.generateFinancialDisclosure(financeRate, term.getFrom(), term.getTo(), xml.getLocale()));
                term.setAdvances(buildLtvAdvances(variationID, pairedVariationIDs, financeRate));
                break;
            case 105:
                BigDecimal balloonRate = xml.getDataValueBigDecimal(IncentiveXML.DATA_FIELD_RATE, variationID, pairedVariationIDs);
                term.setValue(balloonRate);
                term.setFutureValue(xml.getFutureValue(variationID, pairedVariationIDs));
                term.setAmountFinancedFrom(xml.getDataValueBigDecimal(IncentiveXML.DATA_FIELD_AMOUNT_FINANCED_FROM, variationID, pairedVariationIDs));
                term.setAmountFinancedTo(xml.getDataValueBigDecimal(IncentiveXML.DATA_FIELD_AMOUNT_FINANCED_TO, variationID, pairedVariationIDs));
                term.setVariance(xml.getDataValueDouble(IncentiveXML.DATA_FIELD_VARIANCE, variationID, pairedVariationIDs));
                term.setFinancialDisclosure(loanCalculator.generateFinancialDisclosure(balloonRate, term.getFrom(), term.getTo(), xml.getLocale()));
                break;
            case 108:
                term.setValue(xml.getDataValueBigDecimal(IncentiveXML.DATA_FIELD_RATE_DISCOUNT, variationID, pairedVariationIDs));
                break;
            case 109:
                BigDecimal moneyFactor = xml.getDataValueBigDecimal(IncentiveXML.DATA_FIELD_MONEY_FACTOR, variationID, pairedVariationIDs);
                term.setValue(moneyFactor);
                term.setAdvances(buildLtvAdvances(variationID, pairedVariationIDs, moneyFactor));
                break;
            case 112:
                // Residual incentives
                term.setMileageRestrictions(buildMileageRestrictions(variationID, pairedVariationIDs));
                String valueType = xml.getDataValueString(IncentiveXML.DATA_FIELD_VALUE_TYPE);
                if (valueType != null)
                    term.setValueType(valueType.charAt(0));
                break;
            // Cash Category Types
            case 121:
            case 123:
            case 124:
            case 130:
            case 131:
            case 132:
            case 135:
            case 136:
                term.setValue(xml.getDataValueBigDecimal(IncentiveXML.DATA_FIELD_MSRP, variationID, pairedVariationIDs));
                break;
            case 151:
                term.setValue(xml.getDataValueBigDecimal(IncentiveXML.DATA_FIELD_MONEY_FACTOR_DISCOUNT, variationID, pairedVariationIDs));
                break;
        }

        return term;
    }

    private List<Advance> buildLtvAdvances(final Integer variationID, final Set<Integer> pairedVariationIDs, final BigDecimal defaultValue) {
        final List<Advance> advances = new ArrayList<>();

        final String maxRangeByTierAndTerms =
            Optional.ofNullable(xml.getDataValueString(IncentiveXML.DATA_FIELD_LTV_MAX_ADVANCE, variationID, pairedVariationIDs))
                    .map(maxLtvRangeForTierAndTerms -> String.format("%s%s", maxLtvRangeForTierAndTerms, '%'))
                    .orElse(null);

        final Optional<String> ltvAdjustmentType =Optional.ofNullable(xml.getDataValueString(IncentiveXML.DATA_FIELD_LTV_ADJUSTMENT_TYPE));

        final String formattedDefaultRange = Optional.ofNullable(xml.getDataValueString(IncentiveXML.DATA_FIELD_LTV_DEFAULT_ADVANCE))
                                                     .map(defaultAdvanceLtvRange -> String.format("%s%s", defaultAdvanceLtvRange, '%'))
                                                     .orElse(null);

        final boolean addToDefault = Optional.ofNullable(xml.getDataValueBoolean(IncentiveXML.DATA_FIELD_LTV_ADD_TO_DEFAULT)).orElse(false);

        if (Objects.nonNull(formattedDefaultRange) && Objects.nonNull(defaultValue)) {
            advances.add(new Advance(formattedDefaultRange, defaultValue, maxRangeByTierAndTerms, true));
        }

        xml.getDataValues(IncentiveXML.DATA_FIELD_LTV_ADJUSTMENTS, variationID, pairedVariationIDs)
           .stream()
           .filter(Objects::nonNull).forEach(dataValue -> {
            String formattedCustomRange = Optional.ofNullable(xml.getCustomDataRowDescription(dataValue.getCustomRowUID()))
                                                  .map(customLtvRange -> String.format("%s%s", customLtvRange, '%'))
                                                  .orElse(null);
            BigDecimal adjustment = Optional.ofNullable(xml.toBigDecimal(dataValue.getValue())).map(value -> {
                if (Objects.equals(xml.getCategoryID(), 109) && (!ltvAdjustmentType.isPresent() || Objects.equals("%", ltvAdjustmentType.get()))) {
                    return value.divide(new BigDecimal(this.ltvLeaseRateMoneyFactorDivider), 5, RoundingMode.HALF_EVEN);
                }
                return value;
            }).orElse(null);

            if (Objects.nonNull(formattedCustomRange) && Objects.nonNull(adjustment)) {
                BigDecimal ltvValue = addToDefault ? defaultValue.add(adjustment) : adjustment;
                advances.add(new Advance(formattedCustomRange, ltvValue, maxRangeByTierAndTerms, false));
            }
        });
        return advances;
    }

    private List<MileageRestriction> buildMileageRestrictions(Integer variationID, Set<Integer> pairedVariationIDs) {
        List<MileageRestriction> mileageRestrictions = new ArrayList<>();

        // Add the default mileage restriction
        BigDecimal defaultResidual = xml.getDataValueBigDecimal(IncentiveXML.DATA_FIELD_VALUE, variationID, pairedVariationIDs);
        String defaultMileage = xml.getDataValueString(IncentiveXML.DATA_FIELD_DEFAULT_MILEAGE_RESTRICTION);
        mileageRestrictions.add(new MileageRestriction(defaultMileage, defaultResidual, true));

        // Add the adjustments (if any) for the default mileage restriction
        for (DataValue residualAdjustment : xml.getDataValues(IncentiveXML.DATA_FIELD_FEATURE, variationID, pairedVariationIDs)) {
            String mileage = xml.getCustomDataRowDescription(residualAdjustment.getCustomRowUID());
            BigDecimal adjustment = xml.toBigDecimal(residualAdjustment.getValue());
            // Mileage with blank or null adjustment value will be removed from Mileage Restrictions list
            if (adjustment != null) {
                BigDecimal adjustedResidual = null == defaultResidual ? adjustment : defaultResidual.add(adjustment);
                mileageRestrictions.add(new MileageRestriction(mileage, adjustedResidual, false));
            }
        }

        return mileageRestrictions;
    }

    private void addDefaultVariation() {
        Variation variation = new Variation();
        variation.setValue(xml.getDataValueBigDecimal(IncentiveXML.DATA_FIELD_MSRP));
        if (variation.getValue() == null) {
            variation.setValue(xml.getDataValueBigDecimal(IncentiveXML.DATA_FIELD_DOLLAR_VALUE));
        }
        variation.setRequirement(xml.getRequirements());
        variation.setUpTo(xml.getDataValueBoolean(IncentiveXML.DATA_FIELD_UP_TO));
        variation.setMaxNumberOfPaymentsWaived(xml.getDataValueInteger(IncentiveXML.DATA_FIELD_MAX_NUMBER_OF_PAYMENTS_WAIVED));
        variation.setMaxValueOfPaymentsWaived(xml.getDataValueInteger(IncentiveXML.DATA_FIELD_MAX_VALUE_OF_PAYMENTS_WAIVED));
        variation.setMaxValueOfPaymentsWaivedPerPayment(xml.getDataValueInteger(IncentiveXML.DATA_FIELD_MAX_VALUE_OF_PAYMENTS_WAIVED_PER_PAYMENT));
        variation.setExitingGroup(xml.getDataValueTranslated(IncentiveXML.DATA_FIELD_EXITING_GROUP));
        variation.setLeasesExpiringFrom(xml.getDataValueString(IncentiveXML.DATA_FIELD_LEASES_EXPIRING_FROM));
        variation.setLeasesExpiringTo(xml.getDataValueString(IncentiveXML.DATA_FIELD_LEASES_EXPIRING_TO));
        variation.setDescription(xml.getDataValueTranslated(IncentiveXML.DATA_FIELD_DESCRIPTION));
        variation.setNote(xml.getDataValueTranslated(IncentiveXML.DATA_FIELD_NOTE));
        variation.setMaximumResidualizableMsrp(xml.getDataValueBigDecimal(IncentiveXML.DATA_FIELD_MAX_RESIDUALIZED_MSRP));
        variation.setPercent(xml.getDataValueDouble(IncentiveXML.DATA_FIELD_PERCENT));
        variation.setBasePriceOnly(xml.getDataValueBoolean(IncentiveXML.DATA_FIELD_BASE_PRICE_ONLY));
        variation.setDollarValueEach(xml.getDataValueBigDecimal(IncentiveXML.DATA_FIELD_DOLLAR_VALUE_EACH));
        variation.setUpToMaxNumber(xml.getDataValueInteger(IncentiveXML.DATA_FIELD_UP_TO_MAX_NUMBER));
        variation.setEarnedPeriodFrom(xml.getDataValueString(IncentiveXML.DATA_FIELD_EARNED_PERIOD_FROM));
        variation.setEarnedPeriodTo(xml.getDataValueString(IncentiveXML.DATA_FIELD_EARNED_PERIOD_TO));
        add(variation);
    }

}
