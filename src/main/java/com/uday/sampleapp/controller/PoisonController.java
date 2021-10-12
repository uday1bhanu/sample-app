package com.uday.sampleapp.controller;

import com.uday.sampleapp.model.HealthStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PoisonController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private HealthStatus healthStatus;

    /**
     * Poison the app
     *
     * @return return current health status
     */
    @RequestMapping(value = "/poison", method = RequestMethod.GET)
    public String poison(){
        logger.info("Marking the app as unhealthy");
        healthStatus.setStatus(false);
        return "App health is DOWN";
    }

    /**
     * Cure the app
     *
     * @return return current health status
     */
    @RequestMapping(value = "/cure", method = RequestMethod.GET)
    public String cure(){
        logger.info("Marking the app as healthy");
        healthStatus.setStatus(true);
        return "App health is UP";
    }
}
