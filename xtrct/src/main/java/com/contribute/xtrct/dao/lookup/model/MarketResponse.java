package com.contribute.xtrct.dao.lookup.model;

import com.chromedata.incentives.response.AbstractCopyRightResponse;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Display class modeling a market container response object
 */
@XmlRootElement(name = "MarketResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class MarketResponse extends AbstractCopyRightResponse {

    @XmlElement(name = "Market")
    private List<Market> markets;

    // Private no-arg constructor required by jax-b
    private MarketResponse() {
    }

    public MarketResponse(List<Market> markets) {
        this.markets = markets;
    }

    public List<Market> getMarkets() {
        return markets;
    }
}
