package com.bridgelabz.fundoo.authentication.exception;

public class LoginException extends RuntimeException{
    private String message;
    private int statusCode;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public LoginException(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }

    @Override
    public String toString() {
        return "LoginException{" +
                "message='" + message + '\'' +
                ", statusCode=" + statusCode +
                '}';
    }
}
