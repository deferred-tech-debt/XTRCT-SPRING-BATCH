package com.chromedata.incentives.extract.dao.lookup.model;

import com.chromedata.incentives.response.AbstractCopyRightResponse;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Display class modeling an error to be returned to the client
 */
@XmlRootElement(name = "Error")
@XmlAccessorType(XmlAccessType.FIELD)
public class ErrorResponse extends AbstractCopyRightResponse {

    @XmlElement
    private int code;
    @XmlElement
    private long timestamp;
    @XmlElement
    private String message;

    // No-arg constructor required by JAX-B
    private ErrorResponse() {
    }

    public ErrorResponse(int code, long timestamp, String message) {
        this.code = code;
        this.timestamp = timestamp;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }
}
