package com.contribute.xtrct.business.model;

import com.contribute.xtrct.dao.model.AugmentedIncentiveData;
import com.contribute.xtrct.dao.model.WarnableResult;

/**
 * Factory to allow assisted injection with Spring for creating XMLIncentive objects
 */
@FunctionalInterface
public interface XMLIncentiveFactory {

    WarnableResult<XMLIncentive> create(IncentiveXML xml, AugmentedIncentiveData augmentedIncentiveData);
}
