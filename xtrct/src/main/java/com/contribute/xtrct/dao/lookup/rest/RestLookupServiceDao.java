package com.contribute.xtrct.dao.lookup.rest;

import com.contribute.xtrct.dao.lookup.enums.LookupType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Locale;

/**
 * Responsible for accessing lookup data from the Lookup Micro Service's RESTful API
 */
@Repository
public class RestLookupServiceDao implements LookupDao {

    private RestTemplate restTemplate;
    private URI lookupServiceBaseContextPath;

    /**
     * Constructor
     *
     * @param restTemplate                 REST client for communicating with the lookup service
     * @param lookupServiceBaseContextPath Base URI path for the lookup service
     */
    @Autowired
    public RestLookupServiceDao(RestTemplate restTemplate, URI lookupServiceBaseContextPath) {
        this.restTemplate = restTemplate;
        this.lookupServiceBaseContextPath = lookupServiceBaseContextPath;
    }

    /**
     * Calls the lookup service to retrieve the lookup data for given lookup type
     * @param path lookup path
     * @param locale current locale
     * @param type response class
     * @return lookup data
     * @see LookupType
     */
    @Override
    public <T> T getLookupData(String path, Locale locale, Class<T> type) {
        // Create the URI
        URI uri = UriComponentsBuilder.fromUri(lookupServiceBaseContextPath)
                                      .path(path)
                                      .query("lang="+locale.getLanguage()+"-"+locale.getCountry())
                                      .query("product=XTL")
                                      .build()
                                      .toUri();

        // Call the micro service
        return restTemplate.getForObject(uri, type);

    }
}
