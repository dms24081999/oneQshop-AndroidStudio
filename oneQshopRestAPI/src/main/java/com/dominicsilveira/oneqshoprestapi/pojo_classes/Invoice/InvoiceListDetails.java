package com.dominicsilveira.oneqshoprestapi.pojo_classes.Invoice;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InvoiceListDetails {

    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("results")
    @Expose
    private List<InvoiceDetails> results = null;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<InvoiceDetails> getResults() {
        return results;
    }

    public void setResults(List<InvoiceDetails> results) {
        this.results = results;
    }

}