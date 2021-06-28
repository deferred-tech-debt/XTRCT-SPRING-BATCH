package com.chromedata.incentives.extract.business.model.mappers;

import com.chromedata.incentives.extract.business.model.Incentive;
import com.chromedata.incentives.extract.business.model.IncentiveXML;
import com.chromedata.incentives.extract.business.model.XMLIncentive;
import com.chromedata.incentives.extract.business.model.XMLIncentiveFactory;
import com.chromedata.incentives.extract.dao.model.AugmentedIncentiveData;
import com.chromedata.incentives.extract.dao.model.WarnableResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Maps incentives from the data layer to the business layer
 */
@Component
public class IncentiveExtractDomainMapper implements ExtractDomainMapper<com.chromedata.eid.aos.incentive.model.Incentive, Incentive> {

    private final XMLIncentiveFactory incentiveFactory;

    @Autowired
    public IncentiveExtractDomainMapper(XMLIncentiveFactory incentiveFactory) {
        this.incentiveFactory = incentiveFactory;
    }

    @Override
    @SuppressWarnings("unchecked")
    public WarnableResult<Incentive> mapIncentive(final com.chromedata.eid.aos.incentive.model.Incentive daoIncentive,
                                                  final AugmentedIncentiveData incentiveData,
                                                  final Locale requestedLocale) {


        WarnableResult<XMLIncentive> incentiveResult = incentiveFactory.create(new IncentiveXML(daoIncentive, requestedLocale),
                                                                               incentiveData);
        return new WarnableResult(incentiveResult.getResult(), incentiveResult.getWarnings());
    }
}
