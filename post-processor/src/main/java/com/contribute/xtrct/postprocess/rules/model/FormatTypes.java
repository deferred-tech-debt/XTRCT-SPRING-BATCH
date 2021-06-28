package com.contribute.xtrct.postprocess.rules.model;

public enum FormatTypes {

    ENCAPSULATE,
    ENCAPSULATE_ELSE,
    FORMAT_STRING,
    FORMAT_STRING_ELSE,
    FORMAT_DATE,
    FORMAT_DATE_ELSE;

    public boolean isElseFormat() {
        return this.name().endsWith("_ELSE");
    }
}
