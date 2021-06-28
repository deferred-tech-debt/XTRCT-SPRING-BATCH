package com.contribute.xtrct.dao.mybatis.spring.config;

/**
 * DTO for holding database connection information
 */
public class ExtractDatabaseConnectionProperties {

    private String driverClassName;
    private String url;
    private String username;
    private String password;
    private Integer maxPoolSize;
    private Long connectionTimeOut;
    private Long idleTimeOut;
    private Long maxLifeTime;

    public ExtractDatabaseConnectionProperties(final String driverClassName,
                                               final String url,
                                               final String username,
                                               final String password,
                                               final Integer maxPoolSize,
                                               final Long connectionTimeOut, final Long idleTimeOut, final Long maxLifeTime) {
        this.driverClassName = driverClassName;
        this.url = url;
        this.username = username;
        this.password = password;
        this.maxPoolSize = maxPoolSize;
        this.connectionTimeOut = connectionTimeOut;
        this.idleTimeOut = idleTimeOut;
        this.maxLifeTime = maxLifeTime;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Integer getMaxPoolSize() {
        return maxPoolSize;
    }

    public Long getConnectionTimeOut() {
        return connectionTimeOut;
    }

    public Long getIdleTimeOut() {
        return idleTimeOut;
    }

    public Long getMaxLifeTime() {
        return maxLifeTime;
    }
}
