package com.dominicsilveira.one_q_shop.ui.RegisterLogin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.dominicsilveira.one_q_shop.R;
import com.dominicsilveira.oneqshoprestapi.api_calls.ApiListener;
import com.dominicsilveira.oneqshoprestapi.api_calls.ApiResponse;
import com.dominicsilveira.oneqshoprestapi.pojo_classes.Error.PasswordResetErrors;
import com.dominicsilveira.oneqshoprestapi.rest_api.RestApiClient;
import com.dominicsilveira.oneqshoprestapi.rest_api.RestApiMethods;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class ResetPasswordActivity extends AppCompatActivity implements ApiListener {
    static String TAG = ResetPasswordActivity.class.getSimpleName();
    String password_reset_token;
    AppCompatEditText confirmPasswordField,newPasswordField;
    Button resetBtn;
    Intent prevIntent;
    RestApiMethods restMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        initComponents();
        attachListeners();
    }


    private void initComponents() {
        restMethods = RestApiClient.buildHTTPClient();//Builds HTTP Client for API Calls
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
                    Toast.makeText(ResetPasswordActivity.this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
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
    public void onApiResponse(String strApiName, int status, Object data, int error) {
        if (strApiName.equals(RestApiMethods.postResetPasswordRequest)) {
            if(status==200){
                Toast.makeText(ResetPasswordActivity.this, "Password reset Successful!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ResetPasswordActivity.this,LoginActivity.class));
                finish();
            }else if(error==1){
                try{
                    JSONObject jObjError = new JSONObject((String) data);
                    PasswordResetErrors errors = new Gson().fromJson(jObjError.toString(), PasswordResetErrors.class);
                    Toast.makeText(ResetPasswordActivity.this, errors.getErrorMsg(), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    Toast.makeText(ResetPasswordActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(ResetPasswordActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}