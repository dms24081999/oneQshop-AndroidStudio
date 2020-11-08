package com.dominicsilveira.one_q_shop.RegisterLogin;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.dominicsilveira.one_q_shop.R;

import java.util.Objects;

public class ResetPasswordActivity extends AppCompatActivity {

    String password_reset_token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        getSupportActionBar().setTitle("Reset Password");

        Uri uri = getIntent().getData();
        if (uri != null) {
            password_reset_token = uri.getQueryParameter("token"); // x = "1.2"
            Log.e(String.valueOf(ResetPasswordActivity.this.getClass()),password_reset_token);
        }
    }
}