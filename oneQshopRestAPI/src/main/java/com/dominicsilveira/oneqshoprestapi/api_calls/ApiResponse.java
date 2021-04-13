package com.dominicsilveira.oneqshoprestapi.api_calls;

import android.util.Log;
import com.dominicsilveira.oneqshoprestapi.pojo_classes.ErrorMessage;
import com.google.gson.Gson;
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
                    apiListener.onApiResponse(strApiName, response.code(), response.body(),"");
                } else {
                    try{
                        Gson gson = new Gson();
                        ErrorMessage error=gson.fromJson(response.errorBody().charStream(),ErrorMessage.class);
                        Log.i(TAG, strApiName + " : onError: " + error.getMessage() + " : STATUS: " + response.code());
                        apiListener.onApiResponse(strApiName, response.code(),null, error.getMessage());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<T> call, Throwable t) {
                Log.d(TAG, strApiName + " : onFailure: " + t.toString());
                apiListener.onApiResponse(strApiName, 404,null, "Request Failed!");
                t.printStackTrace();
            }
        });
    }
}
