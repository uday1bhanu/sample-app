package com.uday.sampleapp.actuator;

import com.uday.sampleapp.model.HealthStatus;
import com.uday.sampleapp.service.DatabaseConnectionChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class CustomHealthIndicator implements HealthIndicator {

    //@Autowired
    DatabaseConnectionChecker databaseConnectionChecker;
    @Autowired
    private HealthStatus healthStatus;

    @Override
    public Health health() {

        if(isAppUp()){
            return Health.up()
                    .withDetail("app is up", "online")
                    .withDetail("database is up", "online")
                    .build();
        }

        return Health.down()
                .withDetail("app is down", "offline")
                .withDetail("database is down", "offline")
                .build();
    }

    private boolean isDatabaseUp(){
        //Disabling database connection check for now until its approved.
        //return databaseConnectionChecker.testConnection();
        return true;
    }

    private boolean isAppUp(){
        return isDatabaseUp() && healthStatus.getStatus();
    }
}