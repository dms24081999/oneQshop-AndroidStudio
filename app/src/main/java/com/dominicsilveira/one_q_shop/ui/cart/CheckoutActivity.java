package com.dominicsilveira.one_q_shop.ui.cart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.dominicsilveira.one_q_shop.R;
import com.dominicsilveira.one_q_shop.utils.AppConstants;
import com.dominicsilveira.one_q_shop.utils.BasicUtils;
import com.dominicsilveira.one_q_shop.utils.InvoiceGenerator;
import com.dominicsilveira.oneqshoprestapi.api_calls.ApiListener;
import com.dominicsilveira.oneqshoprestapi.pojo_classes.Cart.CartDetails;
import com.dominicsilveira.oneqshoprestapi.pojo_classes.Cart.CartListDetails;
import com.dominicsilveira.oneqshoprestapi.pojo_classes.Product.ProductDetails;
import com.dominicsilveira.oneqshoprestapi.rest_api.RestApiClient;
import com.dominicsilveira.oneqshoprestapi.rest_api.RestApiMethods;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CheckoutActivity extends AppCompatActivity implements ApiListener {

    AutoCompleteTextView full_name_txt,address_txt,city_txt,state_txt,postal_code_txt,country_txt;
    Button bt_submit;
    RestApiMethods restMethods;
    AppConstants globalClass;
    String token;
    Intent prevIntent;
    CartListDetails cartListDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        initPrevIntent();
        initComponents();
        attachListeners();
    }

    private void initPrevIntent() {
        prevIntent=getIntent();
        cartListDetails = (CartListDetails) prevIntent.getSerializableExtra("CART_DETAILS");
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);  //slide from left to right
    }

    private void initComponents() {
        globalClass=(AppConstants)getApplicationContext();
        restMethods = RestApiClient.buildHTTPClient(); //Builds HTTP Client for API Calls
        token=BasicUtils.getSharedPreferencesString(CheckoutActivity.this,"TokenAuth","token","0");

        BasicUtils.setActionBar(CheckoutActivity.this,"Checkout");

        full_name_txt=findViewById(R.id.full_name_txt);
        address_txt=findViewById(R.id.address_txt);
        city_txt=findViewById(R.id.city_txt);
        state_txt=findViewById(R.id.state_txt);
        postal_code_txt=findViewById(R.id.postal_code_txt);
        country_txt=findViewById(R.id.country_txt);
        bt_submit=findViewById(R.id.bt_submit);

        full_name_txt.setText(globalClass.getUserObj().getFirstName().concat(" ").concat(globalClass.getUserObj().getLastName()));
    }

    private void attachListeners() {
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date();
                String line1=address_txt.getText().toString()+", "+city_txt.getText().toString()+",";
                String line2=state_txt.getText().toString()+"-"+postal_code_txt.getText().toString()+", "+country_txt.getText().toString();
                SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("dd-MMM-yyyy, hh:mm a");
//                        dateTimeFormatter.setTimeZone(TimeZone.getTimeZone("IST"));
                File file = new File(CheckoutActivity.this.getExternalCacheDir(), File.separator + dateTimeFormatter.format(date) +".pdf");
                InvoiceGenerator invoiceGenerator=new InvoiceGenerator(CheckoutActivity.this,cartListDetails,globalClass.getUserObj(),file,date,line1,line2,restMethods);
                invoiceGenerator.create();
                invoiceGenerator.uploadFile();
            }
        });
    }

    @Override
    public void onApiResponse(String strApiName, int status, Object data, String error) {
        if (strApiName.equals(RestApiMethods.postInvoiceDetailsRequest)) {
            Toast.makeText(CheckoutActivity.this,"Uploaded PDF!",Toast.LENGTH_SHORT).show();
        }
    }
}