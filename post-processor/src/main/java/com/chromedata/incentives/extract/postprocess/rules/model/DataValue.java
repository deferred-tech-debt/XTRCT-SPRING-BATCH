package com.chromedata.incentives.extract.postprocess.rules.model;

import lombok.Data;

@Data
public class DataValue {

    private String column;
    private String newValue;
}