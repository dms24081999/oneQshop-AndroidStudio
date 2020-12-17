package com.dominicsilveira.one_q_shop.ui.RegisterLogin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dominicsilveira.one_q_shop.R;
import com.dominicsilveira.one_q_shop.ui.MainActivity;
import com.dominicsilveira.oneqshoprestapi.api_calls.ApiListener;
import com.dominicsilveira.oneqshoprestapi.api_calls.ApiResponse;
import com.dominicsilveira.oneqshoprestapi.pojo_classes.Auth.Login;
import com.dominicsilveira.oneqshoprestapi.rest_api.RestApiClient;
import com.dominicsilveira.oneqshoprestapi.rest_api.RestApiMethods;
import com.dominicsilveira.oneqshoprestapi.pojo_classes.ErrorMessage;
import com.google.gson.Gson;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordActivity extends AppCompatActivity implements ApiListener {
    private static String TAG = ResetPasswordActivity.class.getSimpleName();

    String password_reset_token;
    AppCompatEditText confirmPasswordField,newPasswordField;
    Button resetBtn;
    Intent prevIntent;
    RestApiMethods restMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        //Builds HTTP Client for API Calls
        restMethods = RestApiClient.buildHTTPClient();

        initComponents();
        attachListeners();
    }

    private void initComponents() {
        newPasswordField=findViewById(R.id.newPasswordField);
        confirmPasswordField=findViewById(R.id.confirmPasswordField);
        resetBtn=findViewById(R.id.resetBtn);

        prevIntent=getIntent();
        password_reset_token=prevIntent.getStringExtra("TOKEN");
    }

    private void attachListeners() {
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPassword=newPasswordField.getText().toString();
                String confirmPassword=confirmPasswordField.getText().toString();
                if(newPassword.isEmpty() && confirmPassword.isEmpty()){

                }else if(!newPassword.equals(confirmPassword)){
                    Toast.makeText(ResetPasswordActivity.this,"Passwords don't match!",Toast.LENGTH_SHORT).show();
                }else{
                    resetPassword(newPassword);
                }
            }
        });
    }

    private void resetPassword(String newPassword) {
        Call<ResponseBody> req = restMethods.postResetPassword(password_reset_token,newPassword);
        ApiResponse.callRetrofitApi(req, RestApiMethods.postResetPasswordRequest, ResetPasswordActivity.this);
    }

    @Override
    public void onApiResponse(String strApiName, int status, Object data, String error) {
        if (strApiName.equals(RestApiMethods.postResetPasswordRequest)) {
            if(status==200){
                Toast.makeText(ResetPasswordActivity.this, "Password reset Successful!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ResetPasswordActivity.this,LoginActivity.class));
                finish();
            }else{
                Toast.makeText(ResetPasswordActivity.this, "Error "+error, Toast.LENGTH_SHORT).show();
            }
        }
    }
}