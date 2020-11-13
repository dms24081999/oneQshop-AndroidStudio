package com.dominicsilveira.one_q_shop.RegisterLogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;


import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.dominicsilveira.one_q_shop.MainActivity;
import com.dominicsilveira.one_q_shop.R;
import com.dominicsilveira.one_q_shop.jsonschema2pojo_classes.Auth.Login;
import com.dominicsilveira.one_q_shop.utils.AppConstants;

import com.dominicsilveira.one_q_shop.utils.api.RestClient;
import com.dominicsilveira.one_q_shop.utils.api.RestMethods;


import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button loginBtn;
    private TextView forgotPasswordText,registerSwitchText;


    RestMethods restMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Builds HTTP Client for API Calls
        restMethods = RestClient.buildHTTPClient();

        initComponents();
        attachListeners();
    }

    private void initComponents() {
        email=findViewById(R.id.emailField);
        password=findViewById(R.id.passwordField);
        loginBtn=findViewById(R.id.loginBtn);
        registerSwitchText=findViewById(R.id.registerSwitchText);
        forgotPasswordText=findViewById(R.id.forgotPasswordText);
    }

    private void attachListeners() {
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_email=email.getText().toString();
                String txt_password=password.getText().toString();
                loginUser(txt_email,txt_password);
            }
        });

        registerSwitchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                finish();
            }
        });

        forgotPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });
    }


    private void loginUser(String email, String password) {
        final AppConstants globalClass=(AppConstants)getApplicationContext();
        Call<Login> req = restMethods.postLogin(email, password);
        req.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                if (response.isSuccessful()) {
                    globalClass.setUserObj(response.body().getUser());
                    String token = response.body().getToken();
                    Log.i(String.valueOf(LoginActivity.this.getComponentName().getClassName()), String.valueOf(token));
                    SharedPreferences sharedPreferences = getSharedPreferences("TokenAuth", MODE_PRIVATE);// Storing data into SharedPreferences
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();// Creating an Editor object to edit(write to the file)
                    myEdit.putString("token", "Token "+token); // Storing the key and its value as the data fetched from edittext
                    myEdit.apply(); // Once the changes have been made, we need to commit to apply those changes made, otherwise, it will throw an error
                    Intent intent=new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    try {
                        String resp=response.errorBody().string();
                        JSONObject obj = new JSONObject(resp);
                        Log.e(String.valueOf(LoginActivity.this.getComponentName().getClassName()), String.valueOf(obj));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Request failed", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }
}