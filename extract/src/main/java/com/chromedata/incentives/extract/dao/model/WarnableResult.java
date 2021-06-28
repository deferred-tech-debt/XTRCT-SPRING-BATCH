package com.chromedata.incentives.extract.dao.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Generic class useful for bundling warning messages that may occur during the processing of a result set
 */
public class WarnableResult<T> {

    private final T result;
    private final List<ExtractWarning> warnings;

    /**
     * Constructor
     *
     * @param result   The main result from the performed processing
     * @param warnings A list of warnings that may have occurred during the result processing
     */
    public WarnableResult(T result, List<ExtractWarning> warnings) {
        this.result = result;
        this.warnings = new ArrayList<>(warnings);
    }

    public T getResult() {
        return result;
    }

    public List<ExtractWarning> getWarnings() {
        return Collections.unmodifiableList(warnings);
    }
}
