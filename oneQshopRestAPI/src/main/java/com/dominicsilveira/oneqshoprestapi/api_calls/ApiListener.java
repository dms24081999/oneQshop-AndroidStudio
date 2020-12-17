package com.dominicsilveira.oneqshoprestapi.api_calls;

import com.dominicsilveira.oneqshoprestapi.pojo_classes.Auth.Login;

import retrofit2.Response;

public interface ApiListener {
    void onApiResponse(String strApiName, int status, Object data, String error);
}
