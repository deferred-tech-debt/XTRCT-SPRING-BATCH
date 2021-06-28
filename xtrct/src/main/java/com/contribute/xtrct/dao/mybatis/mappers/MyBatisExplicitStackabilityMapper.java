package com.contribute.xtrct.dao.mybatis.mappers;

import com.contribute.xtrct.dao.model.SignatureStackability;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface MyBatisExplicitStackabilityMapper {

    List<SignatureStackability> getStackabilityList(@Param("sigUid") Integer sigUid);
}
