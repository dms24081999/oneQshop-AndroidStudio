package com.dominicsilveira.oneqshoprestapi.api_calls;

import android.util.Log;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//    https://stackoverflow.com/questions/48921846/how-to-make-generic-retrofit-library-for-api-calling
public class ApiResponse {
    private static String TAG = ApiResponse.class.getSimpleName();
//    final ProgressDialog progressDialog = new ProgressDialog(context);
//    progressDialog.setMessage("Loading...");
//    progressDialog.setCancelable(false);
//    progressDialog.show();

    public static <T> void callRetrofitApi(Call<T> req, final String strApiName, final ApiListener apiListener) {
        req.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if (response.isSuccessful()) {
                    try{
                        Log.d(TAG, strApiName + " : onResponse: " + response.body().toString() + " : STATUS: " + response.code());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    apiListener.onApiResponse(strApiName, response.code(), response.body(),0);
                } else {
                    try{
                        Log.d(TAG, strApiName + " : onError: " + response.errorBody().toString() + " : STATUS: " + response.code());
                        apiListener.onApiResponse(strApiName, response.code(), response.errorBody().string(),1);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<T> call, Throwable t) {
                Log.d(TAG, strApiName + " : onFailure: " + t.toString());
                apiListener.onApiResponse(strApiName, 404,null, 1);
                t.printStackTrace();
            }
        });
    }
}
