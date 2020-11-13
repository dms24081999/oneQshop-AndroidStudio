package com.dominicsilveira.one_q_shop.jsonschema2pojo_classes.Product;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductDetails {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("barcode")
    @Expose
    private String barcode;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("short_name")
    @Expose
    private String shortName;
    @SerializedName("categories")
    @Expose
    private List<Integer> categories = null;
    @SerializedName("categories_details")
    @Expose
    private List<CategoriesDetails> categoriesDetails = null;
    @SerializedName("images")
    @Expose
    private List<Integer> images = null;
    @SerializedName("images_details")
    @Expose
    private List<ImagesDetails> imagesDetails = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public List<Integer> getCategories() {
        return categories;
    }

    public void setCategories(List<Integer> categories) {
        this.categories = categories;
    }

    public List<CategoriesDetails> getCategoriesDetails() {
        return categoriesDetails;
    }

    public void setCategoriesDetails(List<CategoriesDetails> categoriesDetails) {
        this.categoriesDetails = categoriesDetails;
    }

    public List<Integer> getImages() {
        return images;
    }

    public void setImages(List<Integer> images) {
        this.images = images;
    }

    public List<ImagesDetails> getImagesDetails() {
        return imagesDetails;
    }

    public void setImagesDetails(List<ImagesDetails> imagesDetails) {
        this.imagesDetails = imagesDetails;
    }
}

