package com.chromedata.incentives.extract.dao.mybatis.mappers;

import com.chromedata.incentives.extract.dao.model.IncentiveType;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;


@Component("extractFilterValuesMapper")
public interface MyBatisFilterValuesMapper {

    IncentiveType getIncentiveType(@Param("id") String id, @Param("languageCode") String languageCode);
}
