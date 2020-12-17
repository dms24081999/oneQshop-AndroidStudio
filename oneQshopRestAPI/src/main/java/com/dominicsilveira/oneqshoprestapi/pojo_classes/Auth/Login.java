package com.dominicsilveira.oneqshoprestapi.pojo_classes.Auth;


import com.dominicsilveira.oneqshoprestapi.pojo_classes.User.User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Login {

    @SerializedName("expiry")
    @Expose
    private String expiry;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("user")
    @Expose
    private User user;

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}

