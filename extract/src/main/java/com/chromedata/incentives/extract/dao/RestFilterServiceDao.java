package com.chromedata.incentives.extract.dao;

import com.chromedata.incentives.filter.model.FilterGroup;
import com.chromedata.incentives.filter.model.FilterResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/**
 * Responsible for accessing product filters from the Filter Service's RESTful API
 */
@Repository
public class RestFilterServiceDao implements FilterDao {

    private RestTemplate restTemplate;
    private URI filterServiceBaseContextPath;

    /**
     * Constructor
     *
     * @param restTemplate                 REST client for communicating with the filter service
     * @param filterServiceBaseContextPath Base URI path for the filter service
     */
    @Autowired
    public RestFilterServiceDao(RestTemplate restTemplate, URI filterServiceBaseContextPath) {
        this.restTemplate = restTemplate;
        this.filterServiceBaseContextPath = filterServiceBaseContextPath;
    }

    /**
     * Calls the filter service to retrieve the product filters for the extract
     *
     * @return Filters representing the extract product filters
     */
    @Override
    public FilterGroup getProductFilters() {
        // Create the URI
        URI uri = UriComponentsBuilder.fromUri(filterServiceBaseContextPath)
                .path("/filters/product/XTL")
                .build()
                .toUri();

        // Call the micro service
        return restTemplate.getForObject(uri, FilterResult.class).getFilter();
    }
}
