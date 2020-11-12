package com.dominicsilveira.one_q_shop.utils;

import android.app.Activity;
import android.app.Application;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dominicsilveira.one_q_shop.jsonschema2pojo_classes.User;


public class AppConstants extends Application {
    public static final int CAMERA_REQUEST_CODE = 100;


    /*
    http://www.jsonschema2pojo.org
    Target language: Java
    Source type::JSON
    Annotation style: Gson
    */
    public static final String BACKEND_URL = "https://".concat("dms24-v1.loca.lt");

    private User userObj;
    private Bitmap userProfilePic;

    public User getUserObj(){
        return userObj;
    }

    public void setUserObj(User userObj){
        this.userObj=userObj;
    }

    public Bitmap getUserProfilePic(){
        return userProfilePic;
    }

    public void setUserProfilePic(Bitmap userProfilePic){
        this.userProfilePic=userProfilePic;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // register to be informed of activities starting up
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {

            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {

            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {

            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {

            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {
                Log.i(String.valueOf(activity.getComponentName().getClassName()),"Application onActivityDestroyed");
            }
        });

    } //End of onCreate
}

