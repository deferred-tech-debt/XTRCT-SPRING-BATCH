package com.contribute.xtrct.presentation.model;

import com.chromedata.commons.dsvio.DSVColumn;
import com.chromedata.commons.identity.IdentityGenerator;
import com.chromedata.commons.identity.utils.IdentityGenerators;

public class LUDivision implements Comparable<LUDivision>{

    private static final IdentityGenerator IDENTITY = IdentityGenerators.createIdentityGenerator(LUDivision.class);

    @DSVColumn(header = "DivisionID", qualified = false)
    private String id;
    @DSVColumn(header = "Description")
    private String description;

    public LUDivision(final String id, final String description) {
        this.id = id;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public int compareTo(final LUDivision o) {
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
