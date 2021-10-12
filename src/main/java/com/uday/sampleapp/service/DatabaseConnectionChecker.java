package com.uday.sampleapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

//@Service
public class DatabaseConnectionChecker implements DatabaseChecker{

    @Autowired
    DataSource dataSource;

    @Override
    public Boolean testConnection() {
        Boolean status = false;
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            jdbcTemplate.execute("SELECT 1");

            status = true;
        } catch (DataAccessException e) {
            System.out.println("Caught DataAccessException: "+ e.getMessage());
        }
        return status;
    }
}
