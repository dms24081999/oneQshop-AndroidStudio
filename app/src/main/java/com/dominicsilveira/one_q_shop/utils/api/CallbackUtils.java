package com.dominicsilveira.one_q_shop.utils.api;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.dominicsilveira.one_q_shop.utils.AppConstants;
import com.mikhaellopez.circularimageview.CircularImageView;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class CallbackUtils {
    //Builds HTTP Client for API Calls
    RestMethods restMethods = RestClient.buildHTTPClient();
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

    public void setBitmapFromURL(String src) {
        globalClass=(AppConstants)applicationContext;
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
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // TODO
            }
        });
    }

}
