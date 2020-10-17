package com.dominicsilveira.one_q_shop.RegisterLogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.dominicsilveira.one_q_shop.R;


public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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