package com.dominicsilveira.oneqshoprestapi.pojo_classes.Cart;

import com.dominicsilveira.oneqshoprestapi.pojo_classes.Product.ProductDetails;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class CartDetails implements Serializable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("cart_history_id ")
    @Expose
    private Integer cartHistoryId;
    @SerializedName("cart_details")
    @Expose
    private ProductDetails cartDetails;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("product_id")
    @Expose
    private Integer productId;
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

    public Integer getCartHistoryId() {
        return cartHistoryId;
    }

    public void setCartHistoryId(Integer cartHistoryId) {
        this.cartHistoryId = cartHistoryId;
    }

    public ProductDetails getCartDetails() {
        return cartDetails;
    }

    public void setCartDetails(ProductDetails cartDetails) {
        this.cartDetails = cartDetails;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
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
}