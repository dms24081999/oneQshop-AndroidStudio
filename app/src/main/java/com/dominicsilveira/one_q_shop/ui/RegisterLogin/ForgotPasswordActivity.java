package com.dominicsilveira.one_q_shop.ui.RegisterLogin;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.dominicsilveira.one_q_shop.R;
import com.dominicsilveira.oneqshoprestapi.api_calls.ApiListener;
import com.dominicsilveira.oneqshoprestapi.api_calls.ApiResponse;
import com.dominicsilveira.oneqshoprestapi.rest_api.RestApiClient;
import com.dominicsilveira.oneqshoprestapi.rest_api.RestApiMethods;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class ForgotPasswordActivity extends AppCompatActivity implements ApiListener {
    static String TAG = ForgotPasswordActivity.class.getSimpleName();
    Button sendMailBtn;
    EditText email;
    RestApiMethods restMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        initComponents();
        attachListeners();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);  //slide from left to right
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);  //slide from left to right
    }

    private void initComponents() {
        restMethods = RestApiClient.buildHTTPClient();//Builds HTTP Client for API Calls
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
        ApiResponse.callRetrofitApi(req, RestApiMethods.postRequestResetPasswordRequest, ForgotPasswordActivity.this);
    }

    @Override
    public void onApiResponse(String strApiName, int status, Object data, int error) {
        if (strApiName.equals(RestApiMethods.postRequestResetPasswordRequest)) {
            if(status==200){
                Toast.makeText(ForgotPasswordActivity.this, "Password reset Email sent!", Toast.LENGTH_SHORT).show();
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);  //slide from left to right
            }else{
                Toast.makeText(ForgotPasswordActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}