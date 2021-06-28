package com.chromedata.incentives.extract.postprocess.rules.model;

import lombok.Data;

import java.util.List;

@Data
public class Relation {

    private String csv;
    private List<Mapping> mapping;
}
