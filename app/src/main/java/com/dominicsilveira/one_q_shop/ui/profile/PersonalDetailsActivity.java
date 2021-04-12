package com.dominicsilveira.one_q_shop.ui.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.dominicsilveira.one_q_shop.R;
import com.dominicsilveira.one_q_shop.utils.AppConstants;
import com.dominicsilveira.oneqshoprestapi.api_calls.ApiListener;
import com.dominicsilveira.oneqshoprestapi.api_calls.ApiResponse;
import com.dominicsilveira.oneqshoprestapi.rest_api.RestApiClient;
import com.dominicsilveira.oneqshoprestapi.rest_api.RestApiMethods;
import com.dominicsilveira.oneqshoprestapi.pojo_classes.User.User;
import retrofit2.Call;

public class PersonalDetailsActivity extends AppCompatActivity implements ApiListener {

    AppCompatEditText firstNameText,lastNameText,phoneText,emailText;
    Button bt_submit;
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
        restMethods = RestApiClient.buildHTTPClient(); //Builds HTTP Client for API Calls
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
                ApiResponse.callRetrofitApi(req, RestApiMethods.updateUserDetailsRequest, PersonalDetailsActivity.this);
            }
        });
    }

    @Override
    public void onApiResponse(String strApiName, int status, Object data, String error) {
        if (strApiName.equals(RestApiMethods.updateUserDetailsRequest)) {
            if(status==200){
                User user = (User) data;
                Toast.makeText(PersonalDetailsActivity.this, "Updated Details!", Toast.LENGTH_SHORT).show();
                globalClass.setUserObj(user);
            }else{
                Toast.makeText(PersonalDetailsActivity.this, "Error "+error, Toast.LENGTH_SHORT).show();
            }
        }
    }
}