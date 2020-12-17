package com.dominicsilveira.one_q_shop.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;


import com.dominicsilveira.oneqshoprestapi.RestApiClient;
import com.dominicsilveira.oneqshoprestapi.RestApiMethods;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class CallbackUtils {
    //Builds HTTP Client for API Calls
    RestApiMethods restMethods = RestApiClient.buildHTTPClient();
    AppConstants globalClass;
    Context applicationContext;
    public AsyncResponse asyncCallback = null;

    public CallbackUtils(Context context, AsyncResponse asyncCallback){
        this.applicationContext=context;
        this.asyncCallback=asyncCallback;
    }

    public interface AsyncResponse {
        void callbackMethod(Bitmap output);
    }

    public void setBitmapFromURL(String path) {
        globalClass=(AppConstants)applicationContext;
        if(path==null){
            asyncCallback.callbackMethod(null);
        }else if(globalClass.getUserProfilePic()!=null){
            asyncCallback.callbackMethod(globalClass.getUserProfilePic());
        }else{
            String src=AppConstants.BACKEND_URL.concat(path);
            Log.i("IMAGE_URL",src);
            Call<ResponseBody> req = restMethods.getImageFile(src);
            req.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            // display the image data in a ImageView or save it
                            Bitmap bmp = BitmapFactory.decodeStream(response.body().byteStream());
                            globalClass.setUserProfilePic(bmp);
                            asyncCallback.callbackMethod(bmp);
//                        userAvatar.setImageBitmap(bmp);
                        }
                    }else{
                        asyncCallback.callbackMethod(null);
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    // TODO
                    asyncCallback.callbackMethod(null);
                }
            });
        }
    }

}
