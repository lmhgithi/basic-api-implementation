package com.thoughtworks.rslist.exception;

public class CommonError extends Exception {
    private String error;

    public CommonError(String errorMessage) {
        super(errorMessage);
        this.error = errorMessage;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
