package com.contribute.xtrct.business.model.mappers;

import com.contribute.xtrct.business.model.Incentive;
import com.contribute.xtrct.business.model.IncentiveXML;
import com.contribute.xtrct.business.model.XMLIncentive;
import com.contribute.xtrct.business.model.XMLIncentiveFactory;
import com.contribute.xtrct.dao.model.AugmentedIncentiveData;
import com.contribute.xtrct.dao.model.WarnableResult;
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
