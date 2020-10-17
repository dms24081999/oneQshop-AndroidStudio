package com.dominicsilveira.one_q_shop.RegisterLogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.dominicsilveira.one_q_shop.R;


public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sh = getSharedPreferences("TokenAuth", MODE_PRIVATE);// The value will be default as empty string because for the very first time when the app is opened, there is nothing to show
        String token=sh.getString("token", "0");// We can then use the data
        Log.i(String.valueOf(SplashScreen.this.getComponentName().getClassName()),token);
        Intent intent;
//        if(FirebaseAuth.getInstance().getCurrentUser()==null){
            intent=new Intent(SplashScreen.this,LoginActivity.class);
//        }else{
//            intent=new Intent(SplashScreen.this,MainActivity.class);
//        }
        startActivity(intent);
        finish();
    }
}