package com.dominicsilveira.one_q_shop.jsonschema2pojo_classes.Product;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ImagesDetails implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("main_image")
    @Expose
    private Boolean mainImage;
    @SerializedName("is_deleted")
    @Expose
    private Boolean isDeleted;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Boolean getMainImage() {
        return mainImage;
    }

    public void setMainImage(Boolean mainImage) {
        this.mainImage = mainImage;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}