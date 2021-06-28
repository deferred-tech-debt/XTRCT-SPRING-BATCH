package com.contribute.xtrct.dao.mybatis.spring;


import com.contribute.xtrct.dao.FilterValuesDao;
import com.contribute.xtrct.dao.model.IncentiveType;
import com.contribute.xtrct.dao.mybatis.mappers.MyBatisFilterValuesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(isolation = Isolation.READ_UNCOMMITTED)
public class MyBatisSpringFilterValuesDao implements FilterValuesDao {

    private final MyBatisFilterValuesMapper filterValuesMapper;

    @Autowired
    public MyBatisSpringFilterValuesDao(@Qualifier("extractFilterValuesMapper") MyBatisFilterValuesMapper filterValuesMapper) {
        this.filterValuesMapper = filterValuesMapper;
    }

    /**
     * Gets the Incentive Type represented by the given id and returns it in the requested language
     *
     * @param id       Unique id of the requested incentive type
     * @param language The language the text of the incentive type will be in
     * @return incentive type with the given id in the given language
     */
    @Override
    public IncentiveType getIncentiveType(String id, String language) {
        return filterValuesMapper.getIncentiveType(id, language);
    }
}
