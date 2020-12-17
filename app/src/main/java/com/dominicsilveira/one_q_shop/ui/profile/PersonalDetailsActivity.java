package com.dominicsilveira.one_q_shop.ui.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dominicsilveira.one_q_shop.R;

import com.dominicsilveira.one_q_shop.utils.AppConstants;

import com.dominicsilveira.oneqshoprestapi.rest_api.RestApiClient;
import com.dominicsilveira.oneqshoprestapi.rest_api.RestApiMethods;
import com.dominicsilveira.oneqshoprestapi.pojo_classes.ErrorMessage;
import com.dominicsilveira.oneqshoprestapi.pojo_classes.User.User;
import com.google.gson.Gson;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonalDetailsActivity extends AppCompatActivity {

    AppCompatEditText firstNameText,lastNameText,phoneText,emailText,newEmailText,currentPasswordText;
    Button bt_submit,bt_submit_email;

    User userObj;
    String email,first_name,last_name,phone_no,token;

    RestApiMethods restMethods;

    AppConstants globalClass;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_details);

        //Builds HTTP Client for API Calls
        restMethods = RestApiClient.buildHTTPClient();

        initComponents();
        attachListeners();
    }

    private void initComponents() {
        getSupportActionBar().setTitle("Personal Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        globalClass=(AppConstants)getApplicationContext();
        userObj=globalClass.getUserObj();

        SharedPreferences sh = getSharedPreferences("TokenAuth", MODE_PRIVATE);// The value will be default as empty string because for the very first time when the app is opened, there is nothing to show
        token=sh.getString("token", "0");// We can then use the data

        firstNameText=findViewById(R.id.firstNameText);
        lastNameText=findViewById(R.id.lastNameText);
        phoneText=findViewById(R.id.phoneText);
        emailText=findViewById(R.id.emailText);
        bt_submit=findViewById(R.id.bt_submit);

        firstNameText.setText(userObj.getFirstName());
        firstNameText.setSelection(firstNameText.getText().length());
        lastNameText.setText(userObj.getLastName());
        lastNameText.setSelection(lastNameText.getText().length());
        phoneText.setText(userObj.getPhoneNumber());
        phoneText.setSelection(phoneText.getText().length());
        emailText.setText(userObj.getEmail());
        emailText.setSelection(emailText.getText().length());
    }

    private void attachListeners() {
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                first_name=firstNameText.getText().toString();
                last_name=lastNameText.getText().toString();
                email=emailText.getText().toString();
                phone_no=phoneText.getText().toString();

                Call<User> req = restMethods.updateUserDetails(userObj.getId(),token, first_name,last_name,email,phone_no);
                req.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()) {
                            if(response.code()==200){
                                Toast.makeText(PersonalDetailsActivity.this, "Updated Details!", Toast.LENGTH_SHORT).show();
                                globalClass.setUserObj(response.body());
                            }else{
                                Toast.makeText(PersonalDetailsActivity.this, "Request failed!", Toast.LENGTH_SHORT).show();
                                Gson gson = new Gson();
                                ErrorMessage error=gson.fromJson(response.errorBody().charStream(),ErrorMessage.class);
                                Log.i(String.valueOf(PersonalDetailsActivity.this.getComponentName().getClassName()), String.valueOf(error.getMessage()));
                            }

                        } else {
                            try {
                                String resp=response.errorBody().string();
                                JSONObject obj = new JSONObject(resp);
                                Log.e(String.valueOf(PersonalDetailsActivity.this.getComponentName().getClassName()), String.valueOf(obj));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(PersonalDetailsActivity.this, "Request failed", Toast.LENGTH_SHORT).show();
                        t.printStackTrace();
                    }
                });

            }
        });
    }
}