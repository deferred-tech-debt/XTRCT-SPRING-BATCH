package com.contribute.xtrct.dao.lookup.model;

import com.chromedata.incentives.response.AbstractCopyRightResponse;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Display class modeling a vehicle status response object
 */
@XmlRootElement(name = "VehicleStatusResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class VehicleStatusResponse extends AbstractCopyRightResponse {

    @XmlElement(name = "VehicleStatus")
    private List<VehicleStatus> vehicleStatus;

    // Private no-arg constructor required by jax-b
    private VehicleStatusResponse() {
    }

    public VehicleStatusResponse(List<VehicleStatus> vehicleStatus) {
        this.vehicleStatus = vehicleStatus;
    }

    public List<VehicleStatus> getVehicleStatus() {
        return vehicleStatus;
    }
}
