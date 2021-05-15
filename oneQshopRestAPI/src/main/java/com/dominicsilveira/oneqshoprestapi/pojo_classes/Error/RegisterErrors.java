package com.dominicsilveira.oneqshoprestapi.pojo_classes.Error;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.stream.Collectors;

public class RegisterErrors {
    @SerializedName("username")
    @Expose
    private List<String> username = null;
    @SerializedName("email")
    @Expose
    private List<String> email = null;
    @SerializedName("password")
    @Expose
    private List<String> password = null;


    public List<String> getUsername() {
        return username;
    }

    public void setUsername(List<String> username) {
        this.username = username;
    }

    public List<String> getEmail() {
        return email;
    }

    public void setEmail(List<String> email) {
        this.email = email;
    }

    public List<String> getPassword() {
        return password;
    }

    public void setPassword(List<String> password) {
        this.password = password;
    }

    // Generate msg

    @RequiresApi(api = Build.VERSION_CODES.N)
    public String getErrorMsg() {
        if(this.getUsername()!=null && this.getEmail()!=null)
            return this.getUsername().get(0)+"\n"+this.getEmail().get(0);
        else if(this.getUsername()!=null)
            return this.getUsername().get(0);
        else if(this.getEmail()!=null)
            return this.getEmail().get(0);
        else if(this.getPassword()!=null)
            return this.getPassword().stream().collect(Collectors.joining("\n"));
        else
            return "Error!";
    }
}
