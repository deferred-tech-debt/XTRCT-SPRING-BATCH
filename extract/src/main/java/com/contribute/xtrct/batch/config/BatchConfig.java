package com.contribute.xtrct.batch.config;

import com.contribute.xtrct.business.model.CompleteIncentive;
import com.contribute.xtrct.business.model.MarketType;
import com.contribute.xtrct.dao.FilterDao;
import com.chromedata.incentives.filter.EidAosIncentiveInMemoryTester;
import com.chromedata.incentives.filter.model.FilterGroup;
import com.chromedata.incentives.filter.transformers.sql.SqlFilterTransformer;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import javax.sql.DataSource;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Configuration having beans used in extract batch
 */
@Configuration
@ComponentScan(basePackages = "com.chromedata.incentives.extract.batch")
public class BatchConfig {

    private static final Set<String> DEALER_CATEGORY_IDS = Stream.of(130, 131, 132, 135, 136)
                                                                  .map(String::valueOf)
                                                                  .collect(Collectors.toCollection(HashSet::new));

    @Bean
    public FilterGroup productFilters(FilterDao filterDao) {
        return filterDao.getProductFilters();
    }

    @Bean
    public String sqlProductFilterWhereClause(FilterGroup productFilters) {
        return new SqlFilterTransformer().transform(productFilters);
    }

    @Bean
    public EidAosIncentiveInMemoryTester inMemoryIncentivePredicate(FilterGroup productFilters) {
        return new EidAosIncentiveInMemoryTester(productFilters);
    }

    @Bean
    public BatchConfigurer configurer(final DataSource dataSource) {
        return new DefaultBatchConfigurer(dataSource);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Predicate<CompleteIncentive> consumerPredicate() {
        return completeIncentive -> DEALER_CATEGORY_IDS.contains(completeIncentive.getIncentive().getCategoryId()) ||
                                    Objects.equals(completeIncentive.getIncentive().getMarketId(), MarketType.DEALER.getId());
    }
}
