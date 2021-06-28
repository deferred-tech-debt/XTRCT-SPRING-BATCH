package com.contribute.xtrct.postprocess.rules.model;

import lombok.Data;

import java.util.List;

@Data
public class Relation {

    private String csv;
    private List<Mapping> mapping;
}
