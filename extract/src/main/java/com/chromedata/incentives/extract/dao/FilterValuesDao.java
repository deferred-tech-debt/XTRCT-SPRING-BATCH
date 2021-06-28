package com.chromedata.incentives.extract.dao;


import com.chromedata.incentives.extract.dao.model.IncentiveType;


public interface FilterValuesDao {

    IncentiveType getIncentiveType(String code, String language);
}

