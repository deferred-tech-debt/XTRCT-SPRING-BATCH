package com.chromedata.incentives.extract.dao.mybatis.spring.config;

import com.chromedata.incentives.filter.transformers.FilterTransformer;
import com.chromedata.incentives.filter.transformers.sql.SqlFilterTransformer;
import com.zaxxer.hikari.HikariDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.net.URI;

/**
 * Spring configuration file for the data access layer
 */
@Configuration
@EnableTransactionManagement
@MapperScan("com.chromedata.incentives.extract.dao.mybatis.mappers")
@ComponentScan(basePackages = "com.chromedata.incentives.extract.dao")
public class ExtractMyBatisSpringConfig {

    @Bean
    public ExtractDatabaseConnectionProperties extractDatabaseConnectionProperties(Environment environment) {
        return new ExtractDatabaseConnectionProperties(environment.getProperty("incentiveBatch.jdbc.driverClassName"),
                                                       environment.getProperty("incentiveBatch.jdbc.url"),
                                                       environment.getProperty("incentiveBatch.jdbc.username"),
                                                       environment.getProperty("incentiveBatch.jdbc.password"),
                                                       Integer.parseInt(environment.getProperty("incentiveBatch.max.pool.size")),
                                                       Long.parseLong(environment.getProperty("incentiveBatch.connection.timeout")),
                                                       Long.parseLong(environment.getProperty("incentiveBatch.idle.timeout")),
                                                       Long.parseLong(environment.getProperty("incentiveBatch.max.life.time")));
    }

    /**
     * Overriding bean definition in parent configuration
     */
    @Bean
    public URI filterServiceBaseContextPath(Environment environment) throws NamingException {
        return URI.create(environment.getProperty("filter.service.path"));
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public FilterTransformer<String> sqlFilterTransformer() {
        return new SqlFilterTransformer();
    }

    @Bean
    public DataSource extractDataSource(ExtractDatabaseConnectionProperties extractDatabaseConnectionProperties) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(extractDatabaseConnectionProperties.getDriverClassName());
        dataSource.setJdbcUrl(extractDatabaseConnectionProperties.getUrl());
        dataSource.setUsername(extractDatabaseConnectionProperties.getUsername());
        dataSource.setPassword(extractDatabaseConnectionProperties.getPassword());
        dataSource.setMaximumPoolSize(extractDatabaseConnectionProperties.getMaxPoolSize());
        dataSource.setConnectionTimeout(extractDatabaseConnectionProperties.getConnectionTimeOut());
        dataSource.setIdleTimeout(extractDatabaseConnectionProperties.getIdleTimeOut());
        dataSource.setMaxLifetime(extractDatabaseConnectionProperties.getMaxLifeTime());
        return dataSource;
    }

    @Bean
    @Primary
    public PlatformTransactionManager extractTransactionManager(@Qualifier("extractDataSource") DataSource extractDataSource) {
        return new DataSourceTransactionManager(extractDataSource);
    }

    @Bean
    @Primary
    public SqlSessionFactoryBean sqlSessionFactory(@Qualifier("extractDataSource") DataSource extractDataSource) {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(extractDataSource);
        return sessionFactory;
    }

    @Bean(name = "dataSource_job_repository")
    @Primary
    public DataSource dataSource(){
        return new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2)
            .setName("JOB REPOSITORY")
            .build();
    }

    @PostConstruct
    public void initMyBatisLogger() {
        org.apache.ibatis.logging.LogFactory.useLog4J2Logging();
    }
}
