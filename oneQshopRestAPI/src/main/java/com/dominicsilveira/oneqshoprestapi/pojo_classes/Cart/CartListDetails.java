package com.dominicsilveira.oneqshoprestapi.pojo_classes.Cart;

import com.dominicsilveira.oneqshoprestapi.pojo_classes.Product.CategoriesDetails;
import com.dominicsilveira.oneqshoprestapi.pojo_classes.Product.ProductDetails;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class CartListDetails implements Serializable {

    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("price")
    @Expose
    private Double price;
    @SerializedName("results")
    @Expose
    private List<CartDetails> results = null;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public List<CartDetails> getResults() {
        return results;
    }

    public void setResults(List<CartDetails> results) {
        this.results = results;
    }
}
