package com.chromedata.incentives.extract.dao.model;

import java.io.Serializable;

public class BatchStyleIncentive implements Serializable {

    private Integer incentiveID;
    private Integer style;

    public BatchStyleIncentive(Integer incentiveID, Integer style) {
        this.incentiveID = incentiveID;
        this.style = style;
    }

    public Integer getIncentiveID() {
        return incentiveID;
    }

    public Integer getStyle() {
        return style;
    }
}