package com.dominicsilveira.one_q_shop.RegisterLogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.dominicsilveira.one_q_shop.MainActivity;
import com.dominicsilveira.one_q_shop.classes.Users;
import com.dominicsilveira.one_q_shop.utils.api.RestClient;
import com.dominicsilveira.one_q_shop.utils.api.RestMethods;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SplashScreen extends AppCompatActivity {

    RestMethods restMethods;
    String token;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sh = getSharedPreferences("TokenAuth", MODE_PRIVATE);// The value will be default as empty string because for the very first time when the app is opened, there is nothing to show
        token=sh.getString("token", "0");// We can then use the data
        Log.i(String.valueOf(SplashScreen.this.getComponentName().getClassName()),token);


        //Builds HTTP Client for API Calls
        restMethods = RestClient.buildHTTPClient();

//        https://stackoverflow.com/questions/40973633/retrofit-2-get-json-from-response-body
        Call<Users> req = restMethods.isAuthenticated("Token "+token);
        req.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                Toast.makeText(SplashScreen.this, response.code() + " ", Toast.LENGTH_SHORT).show();
                if (response.isSuccessful()) {
                    String resp=response.body().getEmail();
//                        JSONObject obj = new JSONObject(resp); //response.body().string() fetched only once
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
            public void onFailure(Call<Users> call, Throwable t) {
                Toast.makeText(SplashScreen.this, "Request failed", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
                intent=new Intent(SplashScreen.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}