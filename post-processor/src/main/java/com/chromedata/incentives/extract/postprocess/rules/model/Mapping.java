package com.chromedata.incentives.extract.postprocess.rules.model;

import lombok.Data;

@Data
public class Mapping {

    private String csv;
    private String parentColumn;
    private String mappingColumn;
}
