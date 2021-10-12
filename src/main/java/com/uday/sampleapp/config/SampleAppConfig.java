package com.uday.sampleapp.config;

import com.uday.sampleapp.filter.ResponseHeadersFilter;
import com.uday.sampleapp.model.HealthStatus;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;

@Configuration
@Import({PersistenceConfigProperties.class})
public class SampleAppConfig {

    //@Bean
    public DataSource dataSource(
            PersistenceConfigProperties properties) {
        BasicDataSource ds = new BasicDataSource();

        ds.setDriverClassName(properties.getDriverClass());
        ds.setUrl(properties.getUrl());
        ds.setUsername(properties.getUsername());
        ds.setPassword(properties.getPassword());
        // Setting autocommit to false for maintaining transactions
        ds.setDefaultAutoCommit(false);
        ds.setInitialSize(properties.getInitialPoolSize());
        ds.setMaxActive(properties.getMaxPoolSize());
        ds.setMaxIdle(properties.getMaxIdleConnections());
        ds.setMaxWait(properties.getMaxWaitTime());

        System.out.println("DS connection: URL: "+ds.getUrl() + ", username: "+ds.getUsername());
        return ds;
    }

    @Bean
    public ResponseHeadersFilter responseHeadersFilter(){
        return new ResponseHeadersFilter();
    }

    @Bean("healthStatus")
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public HealthStatus getHealth(){
        return new HealthStatus();
    }

    @Bean RestTemplate restTemplate() {
        return new RestTemplate();
    }
}