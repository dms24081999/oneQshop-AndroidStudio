package com.dominicsilveira.one_q_shop.ui.RegisterLogin;

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
import com.dominicsilveira.one_q_shop.ui.MainActivity;
import com.dominicsilveira.one_q_shop.R;
import com.dominicsilveira.one_q_shop.utils.AppConstants;
import com.dominicsilveira.oneqshoprestapi.api_calls.ApiListener;
import com.dominicsilveira.oneqshoprestapi.api_calls.ApiResponse;
import com.dominicsilveira.oneqshoprestapi.rest_api.RestApiClient;
import com.dominicsilveira.oneqshoprestapi.rest_api.RestApiMethods;
import com.dominicsilveira.oneqshoprestapi.pojo_classes.Auth.Login;
import retrofit2.Call;


public class LoginActivity extends AppCompatActivity implements ApiListener {
    private static String TAG = LoginActivity.class.getSimpleName();

    private EditText email;
    private EditText password;
    private Button loginBtn;
    private TextView forgotPasswordText,registerSwitchText;
    AppConstants globalClass;
    RestApiMethods restMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        globalClass=(AppConstants)getApplicationContext();
        restMethods = RestApiClient.buildHTTPClient(); //Builds HTTP Client for API Calls

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
        final
        Call<Login> loginApiCall = restMethods.postLogin(email, password);
        ApiResponse.callRetrofitApi(loginApiCall, RestApiMethods.postLoginRequest, this);
    }

    @Override
    public void onApiResponse(String strApiName, int status, Object data, String error) {
        if (strApiName.equals(RestApiMethods.postLoginRequest)) {
            if(data!=null){
                Login loginData=(Login)data;
                globalClass.setUserObj(loginData.getUser());
                String token = loginData.getToken();
                Log.i(TAG, String.valueOf(token));
                SharedPreferences sharedPreferences = getSharedPreferences("TokenAuth", MODE_PRIVATE);// Storing data into SharedPreferences
                SharedPreferences.Editor myEdit = sharedPreferences.edit();// Creating an Editor object to edit(write to the file)
                myEdit.putString("token", "Token "+token); // Storing the key and its value as the data fetched from edittext
                myEdit.apply(); // Once the changes have been made, we need to commit to apply those changes made, otherwise, it will throw an error
                Intent intent=new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(LoginActivity.this, "Error "+error, Toast.LENGTH_SHORT).show();
            }
        }
    }
}