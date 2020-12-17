package com.dominicsilveira.oneqshoprestapi.pojo_classes;


public class ErrorMessage {
    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        if(message==null) this.message="unknown";
        else this.message = message;
    }
}

