package com.dominicsilveira.one_q_shop.jsonschema2pojo_classes.Product;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductBarCodes {

    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("results")
    @Expose
    private Map<String,Integer> results=new HashMap<String,Integer>();

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }



    public Map<String,Integer> getResults() {
        return results;
    }

    public void setResults(Map<String,Integer> results) {
        this.results = results;
    }

}
