package com.dominicsilveira.one_q_shop.jsonschema2pojo_classes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.dominicsilveira.one_q_shop.jsonschema2pojo_classes.User;

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
