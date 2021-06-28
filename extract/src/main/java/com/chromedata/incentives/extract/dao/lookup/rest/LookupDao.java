package com.chromedata.incentives.extract.dao.lookup.rest;

import java.util.Locale;

/**
 * Provides a data for lookup Micro service
 */
public interface LookupDao {

    public <T> T getLookupData(String path, Locale locale, Class<T> type);
}
