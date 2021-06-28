package com.chromedata.incentives.extract.presentation.model;

import com.chromedata.commons.dsvio.DSVColumn;

/**
 * Represents a row in the TaxRule.csv output file.  Tax rules live inside an Incentive object.
 */
public class TaxRule implements Comparable<TaxRule> {

    @DSVColumn(header = "TaxRuleID", qualified = false)
    private int id;
    @DSVColumn(header = "Description")
    private String description;

    public TaxRule(int id, String description) {
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
    public int compareTo(TaxRule taxRule) {
        if (taxRule == this) {
            return 0;
        }

        int comparison = this.getId().compareTo(taxRule.getId());
        if (comparison != 0) {
            return comparison;
        }

        return this.getDescription().compareTo(taxRule.getDescription());
    }
}
