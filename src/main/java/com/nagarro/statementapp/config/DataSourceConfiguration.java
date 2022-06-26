package com.nagarro.statementapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfiguration {

    @Value("${spring.datasource.driver-class-name}")
    private String dataSourceDriver;

    @Value("${spring.datasource.url}")
    private String dataSourceURL;

    @Bean
    public DataSource accStatementAppDataSource(){
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setDriverClassName(dataSourceDriver);
        driverManagerDataSource.setUrl(dataSourceURL);
        return driverManagerDataSource;
    }
}
