package com.dominicsilveira.oneqshoprestapi.pojo_classes.Error;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LoginErrors {
    @SerializedName("non_field_errors")
    @Expose
    private List<String> nonFieldErrors = null;

    public List<String> getNonFieldErrors() {
        return nonFieldErrors;
    }

    public void setNonFieldErrors(List<String> nonFieldErrors) {
        this.nonFieldErrors = nonFieldErrors;
    }

    // Generate msg
    public String getErrorMsg() {
        if(this.getNonFieldErrors()!=null) // for login only
            return this.getNonFieldErrors().get(0);
        else
            return "Error!";
    }
}
