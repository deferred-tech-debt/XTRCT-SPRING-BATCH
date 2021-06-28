package com.chromedata.incentives.extract.dao.lookup.model;

import com.chromedata.incentives.response.AbstractCopyRightResponse;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Display class modeling a geography container response object
 */
@XmlRootElement(name = "RegionResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class FlexibleGeographyResponse extends AbstractCopyRightResponse {

    @XmlElement(name = "Region")
    private List<FlexibleGeography> regions;

    // Private no-arg constructor required by jax-b
    private FlexibleGeographyResponse() {
    }

    public FlexibleGeographyResponse(List<FlexibleGeography> regions) {
        this.regions = regions;
    }

    public List<FlexibleGeography> getRegions() {
        return regions;
    }
}
