package com.dominicsilveira.one_q_shop.RegisterLogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.dominicsilveira.one_q_shop.MainActivity;
import com.dominicsilveira.one_q_shop.R;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.dominicsilveira.one_q_shop.jsonschema2pojo_classes.Login;
import com.dominicsilveira.one_q_shop.utils.AppConstants;
import com.dominicsilveira.one_q_shop.utils.api.RestClient;
import com.dominicsilveira.one_q_shop.utils.api.RestMethods;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    EditText emailField,firstNameField,lastNameField,usernameField,passwordField;
    Button registerBtn;
    TextView loginSwitchText;

    RestMethods restMethods;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Builds HTTP Client for API Calls
        restMethods = RestClient.buildHTTPClient();

        initComponents();
        attachListeners();
    }

    private void initComponents() {
        firstNameField=findViewById(R.id.firstNameField);
        lastNameField=findViewById(R.id.lastNameField);
        usernameField=findViewById(R.id.usernameField);
        emailField=findViewById(R.id.emailField);
        passwordField=findViewById(R.id.passwordField);
        registerBtn=findViewById(R.id.registerBtn);
        loginSwitchText=findViewById(R.id.loginSwitchText);
    }

    private void attachListeners() {
        registerBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String txt_email=emailField.getText().toString();
                String txt_password=passwordField.getText().toString();
                String txt_first_name=firstNameField.getText().toString();
                String txt_last_name=lastNameField.getText().toString();
                String txt_username=usernameField.getText().toString();

                if(TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                    Toast.makeText(RegisterActivity.this,"Empty",Toast.LENGTH_SHORT).show();
                }else if(txt_password.length()<6){
                    Toast.makeText(RegisterActivity.this,"Password too short",Toast.LENGTH_SHORT).show();
                }else{
                    registerUser(txt_username,txt_first_name,txt_last_name,txt_email,txt_password);
                }
            }
        });
        loginSwitchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                finish();
            }
        });
    }

    private void registerUser(String txt_username, String txt_first_name, String txt_last_name, String txt_email, String txt_password) {
        final AppConstants globalClass=(AppConstants)getApplicationContext();
        Call<Login> req = restMethods.postRegister(txt_username,txt_first_name,txt_last_name,txt_email,txt_password);
        req.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                Toast.makeText(RegisterActivity.this, response.code() + " ", Toast.LENGTH_SHORT).show();
                if (response.isSuccessful()) {
                    globalClass.setUserObj(response.body().getUser());
                    String token = response.body().getToken();
                    Log.i(String.valueOf(RegisterActivity.this.getComponentName().getClassName()), String.valueOf(token));
                    SharedPreferences sharedPreferences = getSharedPreferences("TokenAuth", MODE_PRIVATE);// Storing data into SharedPreferences
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();// Creating an Editor object to edit(write to the file)
                    myEdit.putString("token", "Token "+token); // Storing the key and its value as the data fetched from edittext
                    myEdit.apply(); // Once the changes have been made, we need to commit to apply those changes made, otherwise, it will throw an error
                    Intent intent=new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    try {
                        String resp=response.errorBody().string();
                        JSONObject obj = new JSONObject(resp);
                        Log.e(String.valueOf(RegisterActivity.this.getComponentName().getClassName()), String.valueOf(obj));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Request failed", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }
}