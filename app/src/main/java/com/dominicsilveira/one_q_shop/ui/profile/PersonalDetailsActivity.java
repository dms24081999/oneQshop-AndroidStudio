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
import com.dominicsilveira.one_q_shop.RegisterLogin.LoginActivity;
import com.dominicsilveira.one_q_shop.classes.ErrorMessage;
import com.dominicsilveira.one_q_shop.classes.Users;
import com.dominicsilveira.one_q_shop.utils.AppConstants;
import com.dominicsilveira.one_q_shop.utils.api.RestClient;
import com.dominicsilveira.one_q_shop.utils.api.RestMethods;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonalDetailsActivity extends AppCompatActivity {

    AppCompatEditText firstNameText,lastNameText,phoneText,emailText,newEmailText,currentPasswordText;
    Button bt_submit,bt_submit_email;

    Users userObj;
    String email,first_name,last_name,phone_no,token;

    RestMethods restMethods;

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
        restMethods = RestClient.buildHTTPClient();

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
//        phoneText.setText(userObj.get);
//        phoneText.setSelection(phoneText.getText().length());
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

                Call<Users> req = restMethods.updateUserDetails(userObj.getId(),"Token "+token, first_name,last_name,email);
                req.enqueue(new Callback<Users>() {
                    @Override
                    public void onResponse(Call<Users> call, Response<Users> response) {
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
                    public void onFailure(Call<Users> call, Throwable t) {
                        Toast.makeText(PersonalDetailsActivity.this, "Request failed", Toast.LENGTH_SHORT).show();
                        t.printStackTrace();
                    }
                });

            }
        });
    }
}