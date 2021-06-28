package com.chromedata.incentives.extract.postprocess.rules.model;

import lombok.Data;

import java.util.List;

@Data
public class Format {

    private FormatTypes type;
    private String value;
    private List<String> columns;
}
