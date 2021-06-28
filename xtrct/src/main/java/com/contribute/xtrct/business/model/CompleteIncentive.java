package com.contribute.xtrct.business.model;

import com.contribute.xtrct.dao.model.AugmentedIncentiveData;
import lombok.Data;

@Data
public class CompleteIncentive {

    private Incentive incentive;
    private AugmentedIncentiveData augmentedIncentiveData;

    public CompleteIncentive(final Incentive incentive, final AugmentedIncentiveData augmentedIncentiveData) {
        this.incentive = incentive;
        this.augmentedIncentiveData = augmentedIncentiveData;
    }
}
