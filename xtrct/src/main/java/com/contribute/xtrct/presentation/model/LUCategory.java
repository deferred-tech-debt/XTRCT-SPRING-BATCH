package com.contribute.xtrct.presentation.model;

import com.chromedata.commons.dsvio.DSVColumn;
import com.chromedata.commons.identity.IdentityGenerator;
import com.chromedata.commons.identity.utils.IdentityGenerators;

public class LUCategory implements Comparable<LUCategory>{

    private static final IdentityGenerator IDENTITY = IdentityGenerators.createIdentityGenerator(LUCategory.class);

    @DSVColumn(header = "CategoryId", qualified = false)
    private int id;
    @DSVColumn(header = "CategoryGroup")
    private String categoryGroup;
    @DSVColumn(header = "CategoryDescription")
    private String description;

    public LUCategory(final int id, final String categoryGroup, final String description) {
        this.id = id;
        this.categoryGroup = categoryGroup;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public String getCategoryGroup() {
        return categoryGroup;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public int compareTo(LUCategory category) {
        if (category == this) {
            return 0;
        }
        int comparison = this.getId().compareTo(category.getId());
        if (comparison != 0) {
            return comparison;
        }
        comparison = this.getCategoryGroup().compareTo(category.categoryGroup);
        if(comparison != 0) {
            return comparison;
        }
        return this.getDescription().compareTo(category.getDescription());
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
