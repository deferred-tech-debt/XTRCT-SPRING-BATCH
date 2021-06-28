package com.chromedata.incentives.extract.business.model;


import com.chromedata.incentives.extract.MutableTestIncentive;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class MrmIncentiveTest {

    /**
     * ELIGIBILITY TESTS
     */

    @Test
    public void shouldCombineEligibility() {
        String eligibility1 = "One";
        String eligibility2 = "Two";
        String expected = "One.  Two.";
        MutableTestIncentive regularIncentive = new MutableTestIncentive();
        MutableTestIncentive mrmIncentive = new MutableTestIncentive();
        regularIncentive.setEligibility(eligibility1);
        mrmIncentive.setEligibility(eligibility2);
        Incentive sut = new MrmIncentive(regularIncentive, mrmIncentive);

        String eligibility = sut.getEligibility();

        assertThat(eligibility).isEqualTo(expected);
    }

    @Test
    public void nullMrmEligibilityShouldStillWork() {
        String eligibility1 = "One";
        String expected = "One.";
        MutableTestIncentive regularIncentive = new MutableTestIncentive();
        MutableTestIncentive mrmIncentive = new MutableTestIncentive();
        regularIncentive.setEligibility(eligibility1);
        mrmIncentive.setEligibility(null);
        Incentive sut = new MrmIncentive(regularIncentive, mrmIncentive);

        String eligibility = sut.getEligibility();

        assertThat(eligibility).isEqualTo(expected);
    }

    @Test
    public void emptyMrmEligibilityShouldStillWork() {
        String eligibility1 = "One";
        String eligibility2 = "";
        String expected = "One.";
        MutableTestIncentive regularIncentive = new MutableTestIncentive();
        MutableTestIncentive mrmIncentive = new MutableTestIncentive();
        regularIncentive.setEligibility(eligibility1);
        mrmIncentive.setEligibility(eligibility2);
        Incentive sut = new MrmIncentive(regularIncentive, mrmIncentive);

        String eligibility = sut.getEligibility();

        assertThat(eligibility).isEqualTo(expected);
    }

    @Test
    public void nullRegularEligibilityShouldStillWork() {
        String eligibility1 = "Two";
        String expected = "Two.";
        MutableTestIncentive regularIncentive = new MutableTestIncentive();
        MutableTestIncentive mrmIncentive = new MutableTestIncentive();
        regularIncentive.setEligibility(null);
        mrmIncentive.setEligibility(eligibility1);
        Incentive sut = new MrmIncentive(regularIncentive, mrmIncentive);

        String eligibility = sut.getEligibility();

        assertThat(eligibility).isEqualTo(expected);
    }

    @Test
    public void emptyRegularEligibilityShouldStillWork() {
        String eligibility1 = "";
        String eligibility2 = "Two";
        String expected = "Two.";
        MutableTestIncentive regularIncentive = new MutableTestIncentive();
        MutableTestIncentive mrmIncentive = new MutableTestIncentive();
        regularIncentive.setEligibility(eligibility1);
        mrmIncentive.setEligibility(eligibility2);
        Incentive sut = new MrmIncentive(regularIncentive, mrmIncentive);

        String eligibility = sut.getEligibility();

        assertThat(eligibility).isEqualTo(expected);
    }

    @Test
    public void bothNullEligibility() {
        String expected = "";
        MutableTestIncentive regularIncentive = new MutableTestIncentive();
        MutableTestIncentive mrmIncentive = new MutableTestIncentive();
        regularIncentive.setEligibility(null);
        mrmIncentive.setEligibility(null);
        Incentive sut = new MrmIncentive(regularIncentive, mrmIncentive);

        String eligibility = sut.getEligibility();

        assertThat(eligibility).isEqualTo(expected);
    }

    @Test
    public void bothEmptyEligibility() {
        String expected = "";
        MutableTestIncentive regularIncentive = new MutableTestIncentive();
        MutableTestIncentive mrmIncentive = new MutableTestIncentive();
        regularIncentive.setEligibility("");
        mrmIncentive.setEligibility("");
        Incentive sut = new MrmIncentive(regularIncentive, mrmIncentive);

        String eligibility = sut.getEligibility();

        assertThat(eligibility).isEqualTo(expected);
    }


    /**
     * QUALIFICATION TESTS
     */

    @Test
    public void shouldCombineQualification() {
        String qualification1 = "One";
        String qualification2 = "Two";
        String expected = "One.  Two.";
        MutableTestIncentive regularIncentive = new MutableTestIncentive();
        MutableTestIncentive mrmIncentive = new MutableTestIncentive();
        regularIncentive.setQualification(qualification1);
        mrmIncentive.setQualification(qualification2);
        Incentive sut = new MrmIncentive(regularIncentive, mrmIncentive);

        String qualification = sut.getQualification();

        assertThat(qualification).isEqualTo(expected);
    }

    @Test
    public void nullMrmQualificationShouldStillWork() {
        String qualification1 = "One";
        String expected = "One.";
        MutableTestIncentive regularIncentive = new MutableTestIncentive();
        MutableTestIncentive mrmIncentive = new MutableTestIncentive();
        regularIncentive.setQualification(qualification1);
        mrmIncentive.setQualification(null);
        Incentive sut = new MrmIncentive(regularIncentive, mrmIncentive);

        String qualification = sut.getQualification();

        assertThat(qualification).isEqualTo(expected);
    }

    @Test
    public void emptyMrmQualificationShouldStillWork() {
        String qualification1 = "One";
        String qualification2 = "";
        String expected = "One.";
        MutableTestIncentive regularIncentive = new MutableTestIncentive();
        MutableTestIncentive mrmIncentive = new MutableTestIncentive();
        regularIncentive.setQualification(qualification1);
        mrmIncentive.setQualification(qualification2);
        Incentive sut = new MrmIncentive(regularIncentive, mrmIncentive);

        String qualification = sut.getQualification();

        assertThat(qualification).isEqualTo(expected);
    }

    @Test
    public void nullRegularQualificationShouldStillWork() {
        String qualification1 = "Two";
        String expected = "Two.";
        MutableTestIncentive regularIncentive = new MutableTestIncentive();
        MutableTestIncentive mrmIncentive = new MutableTestIncentive();
        regularIncentive.setQualification(null);
        mrmIncentive.setQualification(qualification1);
        Incentive sut = new MrmIncentive(regularIncentive, mrmIncentive);

        String qualification = sut.getQualification();

        assertThat(qualification).isEqualTo(expected);
    }

    @Test
    public void emptyRegularQualificationShouldStillWork() {
        String qualification1 = "";
        String qualification2 = "Two";
        String expected = "Two.";
        MutableTestIncentive regularIncentive = new MutableTestIncentive();
        MutableTestIncentive mrmIncentive = new MutableTestIncentive();
        regularIncentive.setQualification(qualification1);
        mrmIncentive.setQualification(qualification2);
        Incentive sut = new MrmIncentive(regularIncentive, mrmIncentive);

        String qualification = sut.getQualification();

        assertThat(qualification).isEqualTo(expected);
    }

    @Test
    public void bothNullQualification() {
        String expected = "";
        MutableTestIncentive regularIncentive = new MutableTestIncentive();
        MutableTestIncentive mrmIncentive = new MutableTestIncentive();
        regularIncentive.setQualification(null);
        mrmIncentive.setQualification(null);
        Incentive sut = new MrmIncentive(regularIncentive, mrmIncentive);

        String qualification = sut.getQualification();

        assertThat(qualification).isEqualTo(expected);
    }

    @Test
    public void bothEmptyQualification() {
        String expected = "";
        MutableTestIncentive regularIncentive = new MutableTestIncentive();
        MutableTestIncentive mrmIncentive = new MutableTestIncentive();
        regularIncentive.setQualification("");
        mrmIncentive.setQualification("");
        Incentive sut = new MrmIncentive(regularIncentive, mrmIncentive);

        String qualification = sut.getQualification();

        assertThat(qualification).isEqualTo(expected);
    }

    @Test
    public void invalidStackableMrmIncentiveShouldNotBeCombined() {
        Incentive regularMockedIncentive = mock(Incentive.class);
        // This mrm incentive is invalid because we didn't set the mrm value
        Incentive mrmMockedIncentive = mock(Incentive.class);

        Incentive result = new MrmIncentive(regularMockedIncentive, mrmMockedIncentive);

        // Since the MRM incentive was invalid, the MRM value should not have been pulled over to the base
        // incentive's variations
        assertThat(result.getVariationList()).as("Check variations")
                .overridingErrorMessage("Variations should be an empty collection as the variations " +
                        "from the mrm incentive should not be considered or ported over")
                .isEmpty();
    }
}
