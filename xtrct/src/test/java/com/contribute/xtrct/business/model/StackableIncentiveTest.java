package com.contribute.xtrct.business.model;


import com.contribute.xtrct.MutableTestIncentive;
import org.junit.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;


public class StackableIncentiveTest {

    @Test
    public void shouldBeStackable() {
        int sigUid = 43;
        MutableTestIncentive incentive = new MutableTestIncentive();
        incentive.setSignatureId(sigUid);
        Incentive sut = new StackableIncentive(null, Collections.singleton(sigUid));

        assertThat(sut.isStackableWith(incentive)).isTrue();
    }

    @Test
    public void shouldNotBeStackable() {
        int sigUid = 43;
        MutableTestIncentive incentive = new MutableTestIncentive();
        incentive.setSignatureId(sigUid);
        Incentive sut = new StackableIncentive(null, Collections.emptySet());

        assertThat(sut.isStackableWith(incentive)).isFalse();
    }

    @Test
    public void nullSigIdSetShouldNotBeStackable() {
        int sigUid = 43;
        MutableTestIncentive incentive = new MutableTestIncentive();
        incentive.setSignatureId(sigUid);
        Incentive sut = new StackableIncentive(null, null);

        assertThat(sut.isStackableWith(incentive)).isFalse();
    }

    @Test
    public void nullIncentiveShouldNotBeStackable() {
        int sigUid = 43;
        Incentive sut = new StackableIncentive(null, Collections.singleton(sigUid));

        assertThat(sut.isStackableWith(null)).isFalse();
    }

    @Test
    public void nullSigIdShouldNotBeStackable() {
        MutableTestIncentive incentive = new MutableTestIncentive();
        incentive.setSignatureId(null);
        Incentive sut = new StackableIncentive(null, Collections.emptySet());

        assertThat(sut.isStackableWith(incentive)).isFalse();
    }
}
