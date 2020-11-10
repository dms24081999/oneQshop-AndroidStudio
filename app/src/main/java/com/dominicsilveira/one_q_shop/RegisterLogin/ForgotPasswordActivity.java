package com.dominicsilveira.one_q_shop.RegisterLogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dominicsilveira.one_q_shop.R;
import com.dominicsilveira.one_q_shop.jsonschema2pojo_classes.ErrorMessage;
import com.dominicsilveira.one_q_shop.ui.profile.ChangePasswordActivity;
import com.dominicsilveira.one_q_shop.utils.api.RestClient;
import com.dominicsilveira.one_q_shop.utils.api.RestMethods;
import com.google.gson.Gson;

import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {

    private Button sendMailBtn;
    private EditText email;
    RestMethods restMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        //Builds HTTP Client for API Calls
        restMethods = RestClient.buildHTTPClient();

        initComponents();
        attachListeners();
    }

    private void initComponents() {
        Intent in = getIntent();
        String prevEmail = in.getStringExtra("EMAIL");
        sendMailBtn=findViewById(R.id.sendMailBtn);
        email=findViewById(R.id.emailField);
        email.setText(prevEmail);
        email.setSelection(email.getText().length());
    }

    private void attachListeners() {
        sendMailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_email=email.getText().toString();
                if(TextUtils.isEmpty(txt_email)){
                    Toast.makeText(ForgotPasswordActivity.this,"Email can't be blank!",Toast.LENGTH_SHORT).show();
                }else{
                    resetPasswordMail(txt_email);
                }
            }
        });
    }

    private void resetPasswordMail(final String email) {
        Call<ResponseBody> req = restMethods.postRequestResetPassword(email);
        req.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code()==200) {
                    Toast.makeText(ForgotPasswordActivity.this, "Password reset Email sent to "+email, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ForgotPasswordActivity.this, "Request failed!", Toast.LENGTH_SHORT).show();
                    Gson gson = new Gson();
                    ErrorMessage error=gson.fromJson(response.errorBody().charStream(),ErrorMessage.class);
                    Log.i(String.valueOf(ForgotPasswordActivity.this.getComponentName().getClassName()), String.valueOf(error.getMessage()));
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ForgotPasswordActivity.this, "Request failed", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }
}