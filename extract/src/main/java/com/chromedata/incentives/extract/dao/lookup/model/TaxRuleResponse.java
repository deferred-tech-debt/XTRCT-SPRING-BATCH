package com.chromedata.incentives.extract.dao.lookup.model;

import com.chromedata.incentives.response.AbstractCopyRightResponse;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Display class modeling a tax rule response object
 */
@XmlRootElement(name = "TaxRuleResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class TaxRuleResponse extends AbstractCopyRightResponse {

    @XmlElement(name = "TaxRule")
    private List<TaxRule> taxRule;

    // Private no-arg constructor required by jax-b
    private TaxRuleResponse() {
    }

    public TaxRuleResponse(List<TaxRule> taxRule) {
        this.taxRule = taxRule;
    }

    public List<TaxRule> getTaxRules() {
        return taxRule;
    }
}
