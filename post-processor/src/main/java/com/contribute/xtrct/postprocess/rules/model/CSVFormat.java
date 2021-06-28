package com.contribute.xtrct.postprocess.rules.model;

import lombok.Data;

import java.util.List;

@Data
public class CSVFormat {

    private String csv;
    private List<Format> formats;
}
