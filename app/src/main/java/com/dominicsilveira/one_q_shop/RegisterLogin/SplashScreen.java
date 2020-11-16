package com.dominicsilveira.one_q_shop.RegisterLogin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.dominicsilveira.one_q_shop.MainActivity;
import com.dominicsilveira.one_q_shop.R;
import com.dominicsilveira.one_q_shop.jsonschema2pojo_classes.ErrorMessage;
import com.dominicsilveira.one_q_shop.jsonschema2pojo_classes.Product.CategoriesDetails;
import com.dominicsilveira.one_q_shop.jsonschema2pojo_classes.Product.CategoriesListDetails;
import com.dominicsilveira.one_q_shop.jsonschema2pojo_classes.Product.ProductBarCodes;
import com.dominicsilveira.one_q_shop.jsonschema2pojo_classes.Product.ProductDetails;
import com.dominicsilveira.one_q_shop.jsonschema2pojo_classes.User.User;
import com.dominicsilveira.one_q_shop.ui.product.ProductCategoriesActivity;
import com.dominicsilveira.one_q_shop.utils.AppConstants;
import com.dominicsilveira.one_q_shop.utils.api.RestClient;
import com.dominicsilveira.one_q_shop.utils.api.RestMethods;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SplashScreen extends AppCompatActivity {

    RestMethods restMethods;
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
        restMethods = RestClient.buildHTTPClient();

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
        Call<User> req = restMethods.isAuthenticated(token);
        req.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Toast.makeText(SplashScreen.this, response.code() + " ", Toast.LENGTH_SHORT).show();
                if (response.isSuccessful()) {
                    String resp=response.body().getEmail();
                    globalClass.setUserObj(response.body());
                    Log.i(String.valueOf(SplashScreen.this.getComponentName().getClassName()), String.valueOf(response.code()+" "+" "+resp));
                    intent=new Intent(SplashScreen.this, MainActivity.class);
                } else {
                    try {
                        String resp=response.errorBody().string();
                        JSONObject obj = new JSONObject(resp);
                        Log.e(String.valueOf(SplashScreen.this.getComponentName().getClassName()), String.valueOf(obj));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    intent=new Intent(SplashScreen.this,LoginActivity.class);
                }
                startActivity(intent);
                finish();
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(SplashScreen.this, "Request failed", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
                intent=new Intent(SplashScreen.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void retriveProductBarCodes() {
        Map<String, String> data = new HashMap<>();
        Call<ProductBarCodes> getProductBarCodes = restMethods.getProductBarCodes(data);
        getProductBarCodes.enqueue(new Callback<ProductBarCodes>() {
            @Override
            public void onResponse(Call<ProductBarCodes> call, Response<ProductBarCodes> response) {
                Toast.makeText(SplashScreen.this, response.code() + " ", Toast.LENGTH_SHORT).show();
                if (response.isSuccessful()) {
                    Log.i(String.valueOf(SplashScreen.this.getComponentName().getClassName()), String.valueOf(response.code()));
                    SharedPreferences sharedPreferences = getSharedPreferences("ProductBarCodes", MODE_PRIVATE);// Storing data into SharedPreferences
                    SharedPreferences.Editor prefsEditor = sharedPreferences.edit();// Creating an Editor object to edit(write to the file)
                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());
                    prefsEditor.putString("barcodesObj", json);// Storing the key and its value as the data fetched from edittext
                    prefsEditor.apply();// Once the changes have been made, we need to commit to apply those changes made, otherwise, it will throw an error
                    checkUserAuth();
                } else {
                    Toast.makeText(SplashScreen.this, "Request failed!", Toast.LENGTH_SHORT).show();
                    Gson gson = new Gson();
                    ErrorMessage error=gson.fromJson(response.errorBody().charStream(),ErrorMessage.class);
                    Log.i(String.valueOf(SplashScreen.this.getComponentName().getClassName()), String.valueOf(error.getMessage()));
                }
            }
            @Override
            public void onFailure(Call<ProductBarCodes> call, Throwable t) {
                Toast.makeText(SplashScreen.this, "Request failed", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }
}