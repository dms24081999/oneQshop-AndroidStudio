package com.dominicsilveira.one_q_shop.ui.RegisterLogin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dominicsilveira.one_q_shop.R;
import com.dominicsilveira.oneqshoprestapi.RestApiClient;
import com.dominicsilveira.oneqshoprestapi.RestApiMethods;
import com.dominicsilveira.oneqshoprestapi.pojo_classes.ErrorMessage;
import com.google.gson.Gson;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordActivity extends AppCompatActivity {

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
        req.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code()==200) {
                    Toast.makeText(ResetPasswordActivity.this, "Password reset Successful!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ResetPasswordActivity.this,LoginActivity.class));
                }else{
                    Toast.makeText(ResetPasswordActivity.this, "Request failed!", Toast.LENGTH_SHORT).show();
                    Gson gson = new Gson();
                    ErrorMessage error=gson.fromJson(response.errorBody().charStream(),ErrorMessage.class);
                    Log.i(String.valueOf(ResetPasswordActivity.this.getComponentName().getClassName()), String.valueOf(error.getMessage()));
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ResetPasswordActivity.this, "Request failed", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }
}