package com.chromedata.incentives.extract.batch.reader;

import com.chromedata.incentives.extract.dao.model.AugmentedIncentiveData;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Extract pagination reader to read the incentives in chunks for given country & locale
 */
@Configuration
public class ExtractReader {

    @Value("${records.per.read:25000}")
    private int pageSize;

    private static final String INCENTIVES_QUERY = "com.chromedata.incentives.extract.dao.mybatis.mappers.MyBatisIncentiveMapper.getCompleteIncentiveData";

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @StepScope
    @Bean
    public ItemReader<AugmentedIncentiveData> incentivesReader(final String sqlProductFilterWhereClause,
                                                               @Value("#{jobParameters[country]}") final String country,
                                                               @Value("#{jobParameters[language]}") final String language) throws Exception {

        final MyBatisPagingItemReader<AugmentedIncentiveData> incentivesReader = new MyBatisPagingItemReader<>();
        final Map<String, Object>                             params           = new HashMap<>();
        params.put("countryCode", country);
        params.put("languageCode", language);
        params.put("filterSql", sqlProductFilterWhereClause);
        incentivesReader.setSqlSessionFactory(sqlSessionFactory);
        incentivesReader.setQueryId(INCENTIVES_QUERY);
        incentivesReader.setParameterValues(params);
        incentivesReader.setPageSize(pageSize);
        incentivesReader.afterPropertiesSet();
        return incentivesReader;
    }
}
