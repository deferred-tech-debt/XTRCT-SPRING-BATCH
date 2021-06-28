package com.chromedata.incentives.extract.dao.mybatis.mappers;

import com.chromedata.incentives.extract.dao.model.SignatureStackability;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface MyBatisExplicitStackabilityMapper {

    List<SignatureStackability> getStackabilityList(@Param("sigUid") Integer sigUid);
}
