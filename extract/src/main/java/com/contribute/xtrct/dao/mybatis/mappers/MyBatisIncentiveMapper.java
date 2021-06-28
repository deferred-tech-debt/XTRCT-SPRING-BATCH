package com.contribute.xtrct.dao.mybatis.mappers;

import com.contribute.xtrct.dao.model.AugmentedIncentiveData;
import org.springframework.stereotype.Component;

import java.util.List;


@Component("extractIncentiveMapper")
public interface MyBatisIncentiveMapper {

    List<AugmentedIncentiveData> getCompleteIncentiveData();
}
