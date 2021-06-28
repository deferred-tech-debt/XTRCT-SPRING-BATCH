package com.contribute.xtrct.dao.mybatis.mappers;

import com.contribute.xtrct.dao.model.IncentiveType;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;


@Component("extractFilterValuesMapper")
public interface MyBatisFilterValuesMapper {

    IncentiveType getIncentiveType(@Param("id") String id, @Param("languageCode") String languageCode);
}
