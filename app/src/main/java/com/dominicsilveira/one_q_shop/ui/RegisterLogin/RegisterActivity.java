package com.dominicsilveira.one_q_shop.ui.RegisterLogin;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.dominicsilveira.one_q_shop.ui.MainActivity;
import com.dominicsilveira.one_q_shop.R;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.dominicsilveira.one_q_shop.utils.AppConstants;
import com.dominicsilveira.one_q_shop.utils.BasicUtils;
import com.dominicsilveira.oneqshoprestapi.api_calls.ApiListener;
import com.dominicsilveira.oneqshoprestapi.api_calls.ApiResponse;
import com.dominicsilveira.oneqshoprestapi.rest_api.RestApiClient;
import com.dominicsilveira.oneqshoprestapi.rest_api.RestApiMethods;
import com.dominicsilveira.oneqshoprestapi.pojo_classes.Auth.Login;
import retrofit2.Call;

public class RegisterActivity extends AppCompatActivity implements ApiListener {
    static String TAG = RegisterActivity.class.getSimpleName();
    EditText emailField,firstNameField,lastNameField,usernameField,passwordField;
    Button registerBtn;
    TextView loginSwitchText;
    RestApiMethods restMethods;
    AppConstants globalClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initComponents();
        attachListeners();
    }

    private void initComponents() {
        globalClass=(AppConstants)getApplicationContext();
        restMethods = RestApiClient.buildHTTPClient(); //Builds HTTP Client for API Calls
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
        Call<Login> req = restMethods.postRegister(txt_username,txt_first_name,txt_last_name,txt_email,txt_password);
        ApiResponse.callRetrofitApi(req, RestApiMethods.postRegisterRequest, RegisterActivity.this);
    }

    @Override
    public void onApiResponse(String strApiName, int status, Object data, String error) {
        if (strApiName.equals(RestApiMethods.postRegisterRequest)) {
            if(status==200){
                Login login = (Login) data;
                globalClass.setUserObj(login.getUser());
                String token = login.getToken();
                Log.i(TAG, String.valueOf(token));
                BasicUtils.editSharedPreferencesString(RegisterActivity.this,"TokenAuth","token","Token "+token);
                Intent intent=new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(RegisterActivity.this, "Error "+error, Toast.LENGTH_SHORT).show();
            }
        }
    }
}