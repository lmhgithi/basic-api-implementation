package com.thoughtworks.rslist.exception;

public class InvalidParamException extends Exception{
    private String error;
    public InvalidParamException(){};
    public InvalidParamException(String message){
        super(message);
        this.error = message;
    }
    public String getError() {
        return error;
    }
    public void setError(String error) {
        this.error = error;
    }
}
