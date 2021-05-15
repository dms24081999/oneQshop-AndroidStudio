package com.dominicsilveira.oneqshoprestapi.api_calls;

public interface ApiListener {
    void onApiResponse(String strApiName, int status, Object data, int error);
}
