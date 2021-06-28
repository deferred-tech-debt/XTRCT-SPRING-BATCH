package com.chromedata.incentives.extract.presentation.model;

import com.chromedata.commons.dsvio.DSVColumn;
import com.chromedata.commons.identity.IdentityGenerator;
import com.chromedata.commons.identity.utils.IdentityGenerators;

/**
 * Represents a row in the VehicleStatus.csv output file
 */
public class LUVehicleStatus implements Comparable<LUVehicleStatus>{

    private static final IdentityGenerator IDENTITY = IdentityGenerators.createIdentityGenerator(LUVehicleStatus.class);

    @DSVColumn(header = "VehicleStatusID", qualified = false)
    private Integer id;
    @DSVColumn(header = "Description")
    private String description;

    public LUVehicleStatus(final Integer id, final String description) {
        this.id = id;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public int compareTo(final LUVehicleStatus o) {
        if (o == this) {
            return 0;
        }
        int comparison = this.getId().compareTo(o.getId());
        if (comparison != 0) {
            return comparison;
        }
        return this.getDescription().compareTo(o.getDescription());
    }

    @Override
    public boolean equals(Object obj) {
        return IDENTITY.areEqual(this, obj);
    }

    @Override
    public int hashCode() {
        return IDENTITY.getHashCode(this);
    }

    @Override
    public String toString() {
        return IDENTITY.toString(this);
    }
}
