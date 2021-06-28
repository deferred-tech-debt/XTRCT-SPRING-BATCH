package com.chromedata.incentives.extract.dao;

import com.chromedata.incentives.filter.model.FilterGroup;

/**
 * Provides a contract for accessing product filters
 */
public interface FilterDao {

    FilterGroup getProductFilters();
}
