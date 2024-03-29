package com.dominicsilveira.oneqshoprestapi.rest_api;

import android.util.Log;
import androidx.annotation.NonNull;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import static okhttp3.logging.HttpLoggingInterceptor.Level.BODY;

/*
    http://www.jsonschema2pojo.org
    Target language: Java
    Source type::JSON
    Annotation style: Gson
*/

public class RestApiClient {
// https://dms24-v0.loca.lt
// http://oneqshopv0-env.eba-p79dya5i.ap-south-1.elasticbeanstalk.com
    public static final String BACKEND_URL = "http://oneqshopv0-env.eba-p79dya5i.ap-south-1.elasticbeanstalk.com";

    public static RestApiMethods buildHTTPClient() {

        //TODO Replace with your URL [Must have backslash '/' in end]
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BACKEND_URL)
                .client(getClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(RestApiMethods.class);
    }

    //Create OKHttp Client used by retrofit
    private static OkHttpClient getClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(provideHttpLoggingInterceptor())
                .build();
    }

    //Attach logging intercept to print Logs in LogCat
    private static HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor =
                new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(@NonNull String message) {
                        Log.d("HTTP", message);
                    }
                });
        httpLoggingInterceptor.setLevel(BODY);
        return httpLoggingInterceptor;
    }
}

