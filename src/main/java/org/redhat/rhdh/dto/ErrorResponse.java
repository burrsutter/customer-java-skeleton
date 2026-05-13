package org.redhat.rhdh.dto;

public class ErrorResponse {

    public String error;
    public String message;
    public int status;

    public ErrorResponse(String error, String message, int status) {
        this.error = error;
        this.message = message;
        this.status = status;
    }
}
