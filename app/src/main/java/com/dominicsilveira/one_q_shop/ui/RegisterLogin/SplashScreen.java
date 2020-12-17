package com.dominicsilveira.one_q_shop.ui.RegisterLogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.dominicsilveira.one_q_shop.ui.MainActivity;

import com.dominicsilveira.one_q_shop.utils.AppConstants;

import com.dominicsilveira.oneqshoprestapi.api_calls.ApiListener;
import com.dominicsilveira.oneqshoprestapi.api_calls.ApiResponse;
import com.dominicsilveira.oneqshoprestapi.pojo_classes.Auth.Login;
import com.dominicsilveira.oneqshoprestapi.rest_api.RestApiClient;
import com.dominicsilveira.oneqshoprestapi.rest_api.RestApiMethods;
import com.dominicsilveira.oneqshoprestapi.pojo_classes.ErrorMessage;
import com.dominicsilveira.oneqshoprestapi.pojo_classes.Product.ProductBarCodes;
import com.dominicsilveira.oneqshoprestapi.pojo_classes.User.User;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SplashScreen extends AppCompatActivity implements ApiListener {
    private static String TAG = SplashScreen.class.getSimpleName();

    RestApiMethods restMethods;
    String token;
    Intent intent,prevIntent;
    AppConstants globalClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        globalClass=(AppConstants)SplashScreen.this.getApplicationContext();

        SharedPreferences sh = getSharedPreferences("TokenAuth", MODE_PRIVATE);// The value will be default as empty string because for the very first time when the app is opened, there is nothing to show
        token=sh.getString("token", "0");// We can then use the data
        Log.i(String.valueOf(SplashScreen.this.getComponentName().getClassName()),token);

        //Builds HTTP Client for API Calls
        restMethods = RestApiClient.buildHTTPClient();

        initPrevUrlIntent();
    }

    private void initPrevUrlIntent() {
        Uri uri = getIntent().getData();
        String type="",password_reset_token;
        if (uri != null) {
            type = uri.getQueryParameter("type"); // type = "some-type"
            if(type.equals("password_reset")){
                password_reset_token = uri.getQueryParameter("token"); // token = "some-token"
                resetPasswordActivity(password_reset_token);
            }
        }else{
            retriveProductBarCodes();
        }
    }

    private void resetPasswordActivity(String password_reset_token) {
        intent=new Intent(SplashScreen.this, ResetPasswordActivity.class);
        intent.putExtra("TOKEN",password_reset_token);
        startActivity(intent);
        finish();
    }


    private void checkUserAuth() {
        Call<User> isAuthCall = restMethods.isAuthenticated(token);
        ApiResponse.callRetrofitApi(isAuthCall, RestApiMethods.isAuthenticatedRequest, this);
    }

    private void retriveProductBarCodes() {
        Map<String, String> data = new HashMap<>();
        Call<ProductBarCodes> getProductBarCodesCall = restMethods.getProductBarCodes(data);
        ApiResponse.callRetrofitApi(getProductBarCodesCall, RestApiMethods.getProductBarCodesRequest, this);
    }

    @Override
    public void onApiResponse(String strApiName, int status, Object data, String error) {
        if (strApiName.equals(RestApiMethods.isAuthenticatedRequest)) {
            if(data!=null){
                User user = (User) data;
                globalClass.setUserObj(user);
                Toast.makeText(globalClass, "Auth Successful!", Toast.LENGTH_SHORT).show();
                intent=new Intent(SplashScreen.this, MainActivity.class);
            }else{
                Toast.makeText(SplashScreen.this, "Error "+(error), Toast.LENGTH_SHORT).show();
                intent=new Intent(SplashScreen.this,LoginActivity.class);
            }
            startActivity(intent);
            finish();
        }
        if (strApiName.equals(RestApiMethods.getProductBarCodesRequest)) {
            if(data!=null){
                ProductBarCodes productBarCodes = (ProductBarCodes) data;
                SharedPreferences sharedPreferences = getSharedPreferences("ProductBarCodes", MODE_PRIVATE);// Storing data into SharedPreferences
                SharedPreferences.Editor prefsEditor = sharedPreferences.edit();// Creating an Editor object to edit(write to the file)
                Gson gson = new Gson();
                String json = gson.toJson(productBarCodes);
                prefsEditor.putString("barcodesObj", json);// Storing the key and its value as the data fetched from edittext
                prefsEditor.apply();// Once the changes have been made, we need to commit to apply those changes made, otherwise, it will throw an error
                checkUserAuth();
            }else{
                Toast.makeText(SplashScreen.this, "Error".concat(error), Toast.LENGTH_SHORT).show();
            }
        }
    }
}