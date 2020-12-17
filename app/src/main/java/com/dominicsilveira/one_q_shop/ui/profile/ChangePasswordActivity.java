package com.dominicsilveira.one_q_shop.ui.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dominicsilveira.one_q_shop.R;
import com.dominicsilveira.oneqshoprestapi.RestApiClient;
import com.dominicsilveira.oneqshoprestapi.RestApiMethods;
import com.dominicsilveira.oneqshoprestapi.pojo_classes.ErrorMessage;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {

    AppCompatEditText oldPasswordText,newPasswordText,confirmPasswordText;
    Button bt_submit;
    TextInputLayout oldPasswordLayout,newPasswordLayout,confirmPasswordLayout;
    String token,newPass,oldPass,confirmPass;

    RestApiMethods restMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        //Builds HTTP Client for API Calls
        restMethods = RestApiClient.buildHTTPClient();

        initComponents();
        attachListeners();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);  //slide from left to right
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);  //slide from left to right
    }

    private void initComponents() {
        getSupportActionBar().setTitle("Change Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        oldPasswordText=findViewById(R.id.oldPasswordText);
        newPasswordText=findViewById(R.id.newPasswordText);
        confirmPasswordText=findViewById(R.id.confirmPasswordText);
        oldPasswordLayout=findViewById(R.id.oldPasswordLayout);
        newPasswordLayout=findViewById(R.id.newPasswordLayout);
        confirmPasswordLayout=findViewById(R.id.confirmPasswordLayout);
        bt_submit=findViewById(R.id.bt_submit);

        SharedPreferences sh = getSharedPreferences("TokenAuth", MODE_PRIVATE);// The value will be default as empty string because for the very first time when the app is opened, there is nothing to show
        token=sh.getString("token", "0");// We can then use the data
    }

    private void attachListeners() {
        confirmPasswordText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override public void afterTextChanged(Editable s) {
                String confirmPasswordString = s.toString();
                if(confirmPasswordString.equals(newPasswordText.getText().toString())){
                    newPasswordLayout.setError("");
                    confirmPasswordLayout.setError("");
                }else if(confirmPasswordString.equals(oldPasswordText.getText().toString())) {
                    confirmPasswordLayout.setError("Confirm-password can't be Old-password!");
                }else{
                    newPasswordLayout.setError("Passwords don't match");
                    confirmPasswordLayout.setError("Passwords don't match");
                }
            }
        });

        newPasswordText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override public void afterTextChanged(Editable s) {
                String newPasswordString = s.toString();
                if(newPasswordString.equals(oldPasswordText.getText().toString())){
                    newPasswordLayout.setError("New-password can't be Old-password!");
                }else{
                    newPasswordLayout.setError("");
                }
            }
        });

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newPass=newPasswordText.getText().toString();
                oldPass=oldPasswordText.getText().toString();
                confirmPass=confirmPasswordText.getText().toString();
                if(newPass.matches("") || oldPass.matches("") || confirmPass.matches("")){
                    Toast.makeText(ChangePasswordActivity.this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
                }else if(newPass.equals(confirmPass) && !newPass.equals(oldPass)){
                    newPasswordLayout.setError("");
                    confirmPasswordLayout.setError("");
                    Call<ResponseBody> req = restMethods.changePassword(token, oldPass,newPass);
                    req.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.code()==200) {
                                Toast.makeText(ChangePasswordActivity.this, "Password Changed!", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(ChangePasswordActivity.this, "Request failed!", Toast.LENGTH_SHORT).show();
                                Gson gson = new Gson();
                                ErrorMessage error=gson.fromJson(response.errorBody().charStream(),ErrorMessage.class);
                                Log.i(String.valueOf(ChangePasswordActivity.this.getComponentName().getClassName()), String.valueOf(error.getMessage()));
                            }
                        }
                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(ChangePasswordActivity.this, "Request failed", Toast.LENGTH_SHORT).show();
                            t.printStackTrace();
                        }
                    });
                }else{
                    Toast.makeText(ChangePasswordActivity.this, "New Passwords don't match", Toast.LENGTH_SHORT).show();
                    newPasswordLayout.setError("Passwords don't match");
                    confirmPasswordLayout.setError("Passwords don't match");
                }
            }
        });
    }
}