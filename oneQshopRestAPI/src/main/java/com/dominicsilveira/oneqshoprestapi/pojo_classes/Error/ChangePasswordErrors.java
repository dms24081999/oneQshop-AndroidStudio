package com.dominicsilveira.oneqshoprestapi.pojo_classes.Error;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ChangePasswordErrors {
    @SerializedName("old_password")
    @Expose
    private List<String> oldPassword = null;
    @SerializedName("new_password")
    @Expose
    private List<String> newPassword = null;

    public List<String> getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(List<String> oldPassword) {
        this.oldPassword = oldPassword;
    }

    public List<String> getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(List<String> newPassword) {
        this.newPassword = newPassword;
    }

    public String getErrorMsg() {
        if(this.getOldPassword()!=null && this.getNewPassword()!=null)
            return this.getOldPassword().get(0)+"\n"+this.getNewPassword().get(0);
        else if(this.getOldPassword()!=null)
            return this.getOldPassword().get(0);
        else if(this.getNewPassword()!=null)
            return this.getNewPassword().get(0);
        else
            return "Error!";
    }
}
