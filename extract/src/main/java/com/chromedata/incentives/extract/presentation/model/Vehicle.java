package com.chromedata.incentives.extract.presentation.model;

/**
 * Created with IntelliJ IDEA.
 * User: abhishek.mundewal
 * Date: 11/27/13
 * Time: 5:53 AM
 * To change this template use File | Settings | File Templates.
 */

import com.chromedata.commons.dsvio.DSVColumn;
import com.chromedata.commons.identity.IdentityGenerator;
import com.chromedata.commons.identity.utils.IdentityGenerators;

/**
 * Model class for Vehicle.
 *
 */

public class Vehicle implements Comparable<Vehicle> {

    private static final IdentityGenerator IDENTITY = IdentityGenerators.createIdentityGenerator(Vehicle.class);

    @DSVColumn(header = "Acode", qualified = false)
    private String acode;
    @DSVColumn(header = "Country", qualified = false)
    private String country;
    @DSVColumn(header = "Year", qualified = false)
    private String year;
    @DSVColumn(header = "Division")
    private String division;
    @DSVColumn(header = "Model")
    private String model;
    @DSVColumn(header = "Trim")
    private String trim;
    @DSVColumn(header = "Variation")
    private String variation;



    public Vehicle() {
    }


    public String getAcode() {
        return acode;
    }


    public void setAcode(String acode) {
        this.acode = acode;
    }


    /**
     * Getter method to return the country.
     * @return country
     */
    public String getCountry() {
        return country;
    }

    /**
     * Setter method to set the country.
     * @param country
     */

    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Getter method to return the division.
     * @return division
     */
    public String getDivision() {
        return division;
    }

    /**
     * Setter method to set the division.
     * @param division
     */

    public void setDivision(String division) {
        this.division = division;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getTrim() {
        return trim;
    }

    public void setTrim(String trim) {
        this.trim = trim;
    }

    public String getVariation() {
        return variation;
    }

    public void setVariation(String variation) {
        this.variation = variation;
    }

    @Override
    public int compareTo(Vehicle vehicle) {
        if (vehicle == this) return 0;

        int comparison = this.getAcode().compareTo(vehicle.getAcode());
        if (comparison != 0) return comparison;

        comparison = this.getCountry().compareTo(vehicle.getCountry());
        if (comparison != 0) return comparison;

        comparison = this.getDivision().compareTo(vehicle.getDivision());
        if (comparison != 0) return comparison;

        comparison = this.getModel().compareTo(vehicle.getModel());
        if (comparison != 0) return comparison;

        comparison = this.getTrim().compareTo(vehicle.getTrim());
        if (comparison != 0) return comparison;

        comparison = this.getYear().compareTo(vehicle.getYear());
        if (comparison != 0) return comparison;

        return (this.getVariation().compareTo(vehicle.getVariation()));
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
