package com.lucianodev.controlecertificado.dtos;

import java.time.Instant;

public class CustomError {

    private Instant instant;
    private Integer status;
    private String message;
    private String path;

    public CustomError(Instant instant, Integer status, String message, String path) {
        this.instant = instant;
        this.status = status;
        this.message = message;
        this.path = path;
    }


    public Instant getInstant() {
        return instant;
    }

    public void setInstant(Instant instant) {
        this.instant = instant;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
