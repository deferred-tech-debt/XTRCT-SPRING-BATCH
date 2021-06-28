package com.chromedata.incentives.extract.presentation.model;

import com.chromedata.commons.dsvio.DSVColumn;
import com.chromedata.commons.identity.IdentityGenerator;
import com.chromedata.commons.identity.utils.IdentityGenerators;


public class Version {

    private static final IdentityGenerator IDENTITY = IdentityGenerators.createIdentityGenerator(Version.class);

    @DSVColumn(header = "Product")
    private String product;
    @DSVColumn(header = "Schema Version")
    private String applicationVersion;
    @DSVColumn(header = "Country")
    private String country;
    @DSVColumn(header = "Language")
    private String language;
    @DSVColumn(header = "Data Version")
    private String dataVersion;
    @DSVColumn(header = "Copyright")
    private String copyright;

    public Version(String product, String applicationVersion, String country, String language, String dataVersion, String copyright) {
        this.product = product;
        this.applicationVersion = applicationVersion;
        this.country = country;
        this.language = language;
        this.dataVersion = dataVersion;
        this.copyright=copyright;
    }

    // Required no-arg constructor
    private Version() {
    }

    public String getProduct() {
        return product;
    }

    public String getApplicationVersion() {
        return applicationVersion;
    }

    public String getCountry() {
        return country;
    }

    public String getLanguage() {
        return language;
    }

    public String getDataVersion() {
        return dataVersion;
    }

    public String getCopyright() {
        return copyright;
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
