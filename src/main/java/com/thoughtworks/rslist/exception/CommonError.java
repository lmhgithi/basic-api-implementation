package com.thoughtworks.rslist.exception;

public class CommonError {
    private String error;
    public CommonError(){};
    public CommonError(String message){
        this.error = message;
    }
    public String getError() {
        return error;
    }
    public void setError(String error) {
        this.error = error;
    }
}
