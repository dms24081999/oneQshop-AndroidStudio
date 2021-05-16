package com.dominicsilveira.oneqshoprestapi.pojo_classes.Product;

import com.dominicsilveira.oneqshoprestapi.pojo_classes.Cart.CartDetails;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class MiniCartDetails implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("ratings")
    @Expose
    private Integer ratings;
    @SerializedName("is_deleted")
    @Expose
    private Boolean isDeleted;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getRatings() {
        return ratings;
    }

    public void setRatings(Integer ratings) {
        this.ratings = ratings;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public void convertCartDetails(CartDetails cartDetails){
        this.id=cartDetails.getId();
        this.count=cartDetails.getCount();
        this.ratings=cartDetails.getRatings();
        this.isDeleted=cartDetails.getIsDeleted();
    }
}