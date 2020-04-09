package com.springbootcamp.ecommerceapp.utils;


import java.util.Date;

public class ErrorVO extends VO{

    private String error;

    public ErrorVO() {
    }

    public ErrorVO(String error, String message, Date timestamp) {
        super(message, timestamp);
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "ErrorVO{" +
                super.toString() +
                "error='" + error + '\'' +
                '}';
    }
}
