package com.chromedata.incentives.extract.postprocess.rules.model;

import lombok.Data;

import java.util.List;

@Data
public class CSVFormat {

    private String csv;
    private List<Format> formats;
}
