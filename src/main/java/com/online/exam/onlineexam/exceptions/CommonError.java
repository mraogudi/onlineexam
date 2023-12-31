package com.online.exam.onlineexam.exceptions;

import java.time.LocalDateTime;

public class CommonError {
    public CommonError() {
    }

    private String errorMessage;
    private int statusCode;
    private LocalDateTime time;
    private String contextPath;

    public CommonError(String errorMessage, int statusCode, LocalDateTime time, String contextPath) {
        this.errorMessage = errorMessage;
        this.statusCode = statusCode;
        this.time = time;
        this.contextPath = contextPath;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
