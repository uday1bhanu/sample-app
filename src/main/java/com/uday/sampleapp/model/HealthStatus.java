package com.uday.sampleapp.model;

public class HealthStatus {
    private static Boolean status = true;

    public void setStatus(Boolean value){
        status = value;
    }

    public Boolean getStatus(){
        return status;
    }
}
