package com.chromedata.incentives.extract.business.model;

import com.chromedata.incentives.extract.dao.model.AugmentedIncentiveData;
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
