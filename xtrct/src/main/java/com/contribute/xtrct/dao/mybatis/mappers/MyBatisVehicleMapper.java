package com.contribute.xtrct.dao.mybatis.mappers;

import com.contribute.xtrct.dao.model.Vehicle;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;


@Component("extractVehicleMapper")
public interface MyBatisVehicleMapper {

    List<Vehicle> getVehicles(@Param("languageCode") String languageCode,
                              @Param("countryCode") String countryCode,
                              @Param("incUID") Integer incentiveId);
}
