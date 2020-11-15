package com.dominicsilveira.one_q_shop.jsonschema2pojo_classes.Product;



import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoriesListDetails implements Serializable {

    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("results")
    @Expose
    private List<CategoriesDetails> results = null;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<CategoriesDetails> getResults() {
        return results;
    }

    public void setResults(List<CategoriesDetails> results) {
        this.results = results;
    }
}