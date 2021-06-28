package com.chromedata.incentives.extract.business.model;

import com.chromedata.incentives.extract.dao.model.AugmentedIncentiveData;
import com.chromedata.incentives.extract.dao.model.WarnableResult;

/**
 * Factory to allow assisted injection with Spring for creating XMLIncentive objects
 */
@FunctionalInterface
public interface XMLIncentiveFactory {

    WarnableResult<XMLIncentive> create(IncentiveXML xml, AugmentedIncentiveData augmentedIncentiveData);
}
