package com.chromedata.incentives.extract.business.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;

/**
 * This class uses the decorator pattern to represent a combined maximum residualized MSRP incentive.  These incentives
 * are a combination of a regular incentive and an MRM specific incentive.  These two incentives must be stackable with
 * each other.  The stackability of these two incentives is assumed to have been already determined before being passed
 * to this class.
 */
public class MrmIncentive extends DecoratedIncentive {

    private static final Logger LOG = LogManager.getLogger(MrmIncentive.class);
    private static final String SENTENCE_SEPARATOR = ".  ";
    private Incentive mrmOnlyIncentive;

    /**
     * Constructor
     *
     * @param baseIncentive The base incentive that we are augmenting with the MRM data
     * @param mrmOnlyIncentive  MRM incentive holding MRM data to add to the base incentive
     */
    public MrmIncentive(Incentive baseIncentive, Incentive mrmOnlyIncentive) {
        super(baseIncentive);
        this.mrmOnlyIncentive = mrmOnlyIncentive;
        extractMrm(baseIncentive, mrmOnlyIncentive);
    }

    /**
     * Joins the base incentive's eligibility requirement with the mrm incentives eligibility requirement if they are
     * available.
     *
     * @return Combined string representing the eligibility requirements for both incentives
     */
    @Override
    public String getEligibility() {
        return (formatSentence(super.getEligibility()) + formatSentence(mrmOnlyIncentive.getEligibility())).trim();
    }

    /**
     * Joins the base incentive's qualification requirement with the mrm incentives qualification requirement if
     * they are available.
     *
     * @return Combined string representing the qualification requirements for both incentives
     */
    @Override
    public String getQualification() {
        return (formatSentence(super.getQualification()) + formatSentence(mrmOnlyIncentive.getQualification())).trim();
    }

    private static String formatSentence(String text) {
        return (text != null && !text.isEmpty()) ? (text + SENTENCE_SEPARATOR) : "";
    }

    // Pull the MRM value out of the MRM incentive and augment the regular incentive with this data
    private void extractMrm(Incentive regularIncentive, Incentive mrmIncentive) {

        if (isValidMrmIncentive(mrmIncentive)) {
            BigDecimal mrm = mrmIncentive.getVariationList().get(0).getMaximumResidualizableMsrp();

            for (Variation variation : regularIncentive.getVariationList()) {
                // Make sure we set the MRM value on the variations for the regular/base incentive since that was the
                // one passed to the super class and will be the one callers retrieve variations for.
                variation.setMaximumResidualizableMsrp(mrm);
            }
        } else {
            LOG.error("Found invalid MRM Incentive with id {}", mrmIncentive.getId());
        }
    }

    // An MRM incentive is considered valid if it has a variation list with a single, default variation containing the
    // MRM value
    private static boolean isValidMrmIncentive(Incentive mrmIncentive) {
        return mrmIncentive.getVariationList() != null
                && !mrmIncentive.getVariationList().isEmpty()
                && mrmIncentive.getVariationList().get(0) != null
                && mrmIncentive.getVariationList().get(0).getMaximumResidualizableMsrp() != null;
    }
}
