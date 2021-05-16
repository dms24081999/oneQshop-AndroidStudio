package com.dominicsilveira.oneqshoprestapi.pojo_classes.Error;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PersonalDetailsErrors {
    @SerializedName("email")
    @Expose
    private List<String> email = null;

    @SerializedName("phone_number")
    @Expose
    private List<String> phoneNumber = null;

    public List<String> getEmail() {
        return email;
    }

    public void setEmail(List<String> email) {
        this.email = email;
    }

    public List<String> getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(List<String> phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getErrorMsg() {
        if(this.getPhoneNumber()!=null && this.getEmail()!=null)
            return this.getPhoneNumber().get(0)+"\n"+this.getEmail().get(0);
        else if(this.getPhoneNumber()!=null)
            return this.getPhoneNumber().get(0);
        else if(this.getEmail()!=null)
            return this.getEmail().get(0);
        else
            return "Error!";
    }
}
