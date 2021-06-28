package com.contribute.xtrct.postprocess.rules.model;

import lombok.Data;

import java.util.List;

@Data
public class Definition {

    private Operation operation;
    private String newFileName;
    private List<String> deleteColumns;
    private List<Column> newColumns;
    private List<Criteria> criteria;
    private List<DataValue> updatedValues;
}
