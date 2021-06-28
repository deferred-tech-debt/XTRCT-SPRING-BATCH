package com.chromedata.incentives.extract.dao.model;

import java.io.Serializable;
import java.util.Objects;

public class BatchVehicleStyle implements Serializable {

    private String acode;
    private int styleID;

    public BatchVehicleStyle(String acode, Integer styleID) {
        this.acode = acode;
        this.styleID = styleID;
    }

    public String getAcode() {
        return acode;
    }

    public int getStyleID() {
        return styleID;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BatchVehicleStyle that = (BatchVehicleStyle) o;
        return styleID == that.styleID &&
               Objects.equals(acode, that.acode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(acode, styleID);
    }
}
