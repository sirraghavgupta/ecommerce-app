package com.springbootcamp.ecommerceapp.utils;

import java.util.Date;

public class VO{
    private String message;
    private Date timestamp;

    public VO() {
    }

    public VO(String message, Date timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "VO{" +
                "message='" + message + '\'' +
                ", date=" + timestamp +
                '}';
    }
}
