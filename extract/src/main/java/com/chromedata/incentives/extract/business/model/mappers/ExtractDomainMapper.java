package com.chromedata.incentives.extract.business.model.mappers;

import com.chromedata.incentives.extract.dao.model.AugmentedIncentiveData;
import com.chromedata.incentives.extract.dao.model.WarnableResult;

import java.util.Locale;

/**
 * Interface defining mapping functions from one object to another with a specific Locale
 *
 * @param <F> Mapping FROM this object type
 * @param <T> Mapping TO this object type
 */
public interface ExtractDomainMapper<F, T> {

    WarnableResult<T> mapIncentive(F daoIncentive, final AugmentedIncentiveData incentiveData, final Locale requestedLocale);
}
