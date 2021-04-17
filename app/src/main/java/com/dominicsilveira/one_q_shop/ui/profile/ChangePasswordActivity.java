package com.dominicsilveira.one_q_shop.ui.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.dominicsilveira.one_q_shop.R;
import com.dominicsilveira.one_q_shop.ui.RegisterLogin.SplashScreen;
import com.dominicsilveira.one_q_shop.utils.BasicUtils;
import com.dominicsilveira.oneqshoprestapi.api_calls.ApiListener;
import com.dominicsilveira.oneqshoprestapi.api_calls.ApiResponse;
import com.dominicsilveira.oneqshoprestapi.rest_api.RestApiClient;
import com.dominicsilveira.oneqshoprestapi.rest_api.RestApiMethods;
import com.google.android.material.textfield.TextInputLayout;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class ChangePasswordActivity extends AppCompatActivity implements ApiListener {
    static String TAG = ChangePasswordActivity.class.getSimpleName();
    AppCompatEditText oldPasswordText,newPasswordText,confirmPasswordText;
    Button bt_submit;
    TextInputLayout oldPasswordLayout,newPasswordLayout,confirmPasswordLayout;
    String token,newPass,oldPass,confirmPass;
    RestApiMethods restMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

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
        restMethods = RestApiClient.buildHTTPClient(); //Builds HTTP Client for API Calls
        token=BasicUtils.getSharedPreferencesString(ChangePasswordActivity.this,"TokenAuth","token","0");

        BasicUtils.setActionBar(ChangePasswordActivity.this,"Change Password");

        oldPasswordText=findViewById(R.id.oldPasswordText);
        newPasswordText=findViewById(R.id.newPasswordText);
        confirmPasswordText=findViewById(R.id.confirmPasswordText);
        oldPasswordLayout=findViewById(R.id.oldPasswordLayout);
        newPasswordLayout=findViewById(R.id.newPasswordLayout);
        confirmPasswordLayout=findViewById(R.id.confirmPasswordLayout);
        bt_submit=findViewById(R.id.bt_submit);
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
                    ApiResponse.callRetrofitApi(req, RestApiMethods.changePasswordRequest, ChangePasswordActivity.this);
                }else{
                    Toast.makeText(ChangePasswordActivity.this, "New Passwords don't match", Toast.LENGTH_SHORT).show();
                    newPasswordLayout.setError("Passwords don't match");
                    confirmPasswordLayout.setError("Passwords don't match");
                }
            }
        });
    }

    @Override
    public void onApiResponse(String strApiName, int status, Object data, String error) {
        if (strApiName.equals(RestApiMethods.changePasswordRequest)) {
            if(status==200){
                Toast.makeText(ChangePasswordActivity.this, "Password Changed!", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(ChangePasswordActivity.this, "Error "+error, Toast.LENGTH_SHORT).show();
            }
        }
    }
}