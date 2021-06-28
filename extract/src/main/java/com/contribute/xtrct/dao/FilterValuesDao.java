package com.contribute.xtrct.dao;


import com.contribute.xtrct.dao.model.IncentiveType;


public interface FilterValuesDao {

    IncentiveType getIncentiveType(String code, String language);
}

