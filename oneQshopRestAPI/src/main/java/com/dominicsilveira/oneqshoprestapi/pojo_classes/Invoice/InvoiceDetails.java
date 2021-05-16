package com.dominicsilveira.oneqshoprestapi.pojo_classes.Invoice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InvoiceDetails {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("pdf_file")
    @Expose
    private String pdfFile;
    @SerializedName("pdf_file_name")
    @Expose
    private String pdfFileName;
    @SerializedName("uploaded_at")
    @Expose
    private String uploadedAt;
    @SerializedName("is_deleted")
    @Expose
    private Boolean isDeleted;
    @SerializedName("user_id")
    @Expose
    private Integer userId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPdfFile() {
        return pdfFile;
    }

    public void setPdfFile(String pdfFile) {
        this.pdfFile = pdfFile;
    }

    public String getPdfFileName() {
        return pdfFileName;
    }

    public void setPdfFileName(String pdfFileName) {
        this.pdfFileName = pdfFileName;
    }

    public String getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(String uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}