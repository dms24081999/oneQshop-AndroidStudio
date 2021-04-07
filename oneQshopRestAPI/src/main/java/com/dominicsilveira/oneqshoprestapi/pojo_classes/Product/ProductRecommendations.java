package com.dominicsilveira.oneqshoprestapi.pojo_classes.Product;


import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductRecommendations implements Serializable {

    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("results")
    @Expose
    private List<ProductDetails> results = null;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<ProductDetails> getResults() {
        return results;
    }

    public void setResults(List<ProductDetails> results) {
        this.results = results;
    }
}

