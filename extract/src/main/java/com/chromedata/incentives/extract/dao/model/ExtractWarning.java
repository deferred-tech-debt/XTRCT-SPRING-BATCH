package com.chromedata.incentives.extract.dao.model;

import java.io.Serializable;

/**
 * This class represents a warning that occurred during the processing of the extract.  It is not an exception, but it may contain an exception.
 */
public class ExtractWarning implements Serializable {

    private final String message;
    private final Exception exception;
    private static final long serialVersionUID = -343413648433860654L;
    /**
     * Constructor
     *
     * @param message   User friendly message to be displayed to stakeholders.  This messaage should clearly define the problem.
     * @param exception If there was an exception associated with this warning
     */
    public ExtractWarning(String message, Exception exception) {
        this.message = message;
        this.exception = exception;
    }

    public String getMessage() {
        return message;
    }

    public Exception getException() {
        return exception;
    }
}
