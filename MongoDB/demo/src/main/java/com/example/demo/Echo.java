package com.example.demo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties (ignoreUnknown = true)
public class Echo {
    @JsonProperty("success")
    private String success;

    public Echo(){
        this.success="false";
    }

    public Echo(String success) {
        this.success = success;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "Echo{" +
                "success='" + success + '\'' +
                '}';
    }
}
