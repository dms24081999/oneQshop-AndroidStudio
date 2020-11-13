package com.dominicsilveira.one_q_shop.jsonschema2pojo_classes.Product;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImagesDetails {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("main_image")
    @Expose
    private Boolean mainImage;

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

}