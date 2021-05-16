package com.dominicsilveira.oneqshoprestapi.pojo_classes.Error;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PasswordResetErrors {
    @SerializedName("password")
    @Expose
    private List<String> password = null;
    @SerializedName("token")
    @Expose
    private List<String> token = null;
    @SerializedName("detail")
    @Expose
    private String detail;

    public List<String> getPassword() {
        return password;
    }

    public void setPassword(List<String> password) {
        this.password = password;
    }

    public List<String> getToken() {
        return token;
    }

    public void setToken(List<String> token) {
        this.token = token;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getErrorMsg() {
        if(this.getPassword()!=null && this.getToken()!=null)
            return this.getPassword().get(0)+"\n"+this.getToken().get(0);
        else if(this.getPassword()!=null)
            return this.getPassword().get(0);
        else if(this.getToken()!=null)
            return this.getToken().get(0);
        else if(this.getDetail()!=null)
            return "Invalid Password Reset Token!\nPlease request a new Password Reset Token!";
        else
            return "Error!";
    }
}
