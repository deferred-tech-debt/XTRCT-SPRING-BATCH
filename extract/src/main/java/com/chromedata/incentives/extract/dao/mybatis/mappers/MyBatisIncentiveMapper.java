package com.chromedata.incentives.extract.dao.mybatis.mappers;

import com.chromedata.incentives.extract.dao.model.AugmentedIncentiveData;
import org.springframework.stereotype.Component;

import java.util.List;


@Component("extractIncentiveMapper")
public interface MyBatisIncentiveMapper {

    List<AugmentedIncentiveData> getCompleteIncentiveData();
}
