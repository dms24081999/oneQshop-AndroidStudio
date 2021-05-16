package com.dominicsilveira.one_q_shop.ui.cart;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;
import com.dominicsilveira.one_q_shop.R;
import com.dominicsilveira.one_q_shop.ui.MainActivity;
import com.dominicsilveira.one_q_shop.utils.AppConstants;
import com.dominicsilveira.one_q_shop.utils.BasicUtils;
import com.dominicsilveira.one_q_shop.utils.InvoiceGenerator;
import com.dominicsilveira.one_q_shop.utils.UPIPayment;
import com.dominicsilveira.oneqshoprestapi.api_calls.ApiListener;
import com.dominicsilveira.oneqshoprestapi.api_calls.ApiResponse;
import com.dominicsilveira.oneqshoprestapi.pojo_classes.Cart.CartListDetails;
import com.dominicsilveira.oneqshoprestapi.rest_api.RestApiClient;
import com.dominicsilveira.oneqshoprestapi.rest_api.RestApiMethods;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class CheckoutActivity extends AppCompatActivity implements ApiListener {
    static String TAG = CheckoutActivity.class.getSimpleName();
    AutoCompleteTextView full_name_txt,address_txt,city_txt,state_txt,postal_code_txt,country_txt;
    Button bt_submit;
    RestApiMethods restMethods;
    AppConstants globalClass;
    String token;
    Intent prevIntent;
    CartListDetails cartListDetails;
    UPIPayment upiPayment=new UPIPayment();
    InvoiceGenerator invoiceGenerator;
    Boolean upi;

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
                String address_txt_str=address_txt.getText().toString();
                String city_txt_str=city_txt.getText().toString();
                String state_txt_str=state_txt.getText().toString();
                String postal_code_txt_str=postal_code_txt.getText().toString();
                String country_txt_str=country_txt.getText().toString();
                if(address_txt_str.matches("") || city_txt_str.matches("") || state_txt_str.matches("") || postal_code_txt_str.matches("") || country_txt_str.matches("")){
                    Toast.makeText(CheckoutActivity.this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
                }else{
                    Date date = new Date();
                    String line1=address_txt_str+", "+city_txt_str+",";
                    String line2=state_txt_str+"-"+postal_code_txt_str+", "+country_txt_str;
                    SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("dd-MMM-yyyy, hh:mm a");
//                        dateTimeFormatter.setTimeZone(TimeZone.getTimeZone("IST"));
                    File file = new File(CheckoutActivity.this.getExternalCacheDir(), File.separator + dateTimeFormatter.format(date) +".pdf");
                    SimpleDateFormat bookFormatter = new SimpleDateFormat("ddMMMMMyyyyHHmmssSSSZ");
                    invoiceGenerator=new InvoiceGenerator(CheckoutActivity.this,cartListDetails,globalClass.getUserObj(),file,date,line1,line2,bookFormatter.format(date),restMethods);
                    invoiceGenerator.create();
                    String note ="Payment for ".concat(bookFormatter.format(date));
//                upi=upiPayment.payUsingUpi(String.valueOf(cartListDetails.getPrice()), "micsilveira111@oksbi", "Michael", note,CheckoutActivity.this);
//                upi=upiPayment.payUsingUpi(String.valueOf(1), "micsilveira111@oksbi", "Michael", note,CheckoutActivity.this);;
                    afterSuccessfulPayment();
                }
            }
        });
    }

    private void afterSuccessfulPayment() {
        invoiceGenerator.uploadFile();
        Call<ResponseBody> req = restMethods.postCartsPaid(token);
        ApiResponse.callRetrofitApi(req, RestApiMethods.postCartsPaidRequest, CheckoutActivity.this);
    }

    @Override
    public void onApiResponse(String strApiName, int status, Object data, int error) {
        if (strApiName.equals(RestApiMethods.postInvoiceDetailsRequest)) {
            Toast.makeText(CheckoutActivity.this,"Uploaded PDF!",Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(CheckoutActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);  //slide from left to right
        }
        if (strApiName.equals(RestApiMethods.postCartsPaidRequest)) {
            Toast.makeText(CheckoutActivity.this,"Updated Cart!",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case AppConstants.UPI_PAYMENT:
                Boolean paid=false;
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");
                        Log.d(String.valueOf(CheckoutActivity.this.getClass()),"UPI:" +"onActivityResult: " + trxt);
                        Map<String, String> myMap = new HashMap<String, String>();
                        String[] pairs = trxt.split("&");
                        for (String pair : pairs) {
                            String[] keyValue = pair.split("=");
                            myMap.put(keyValue[0].toLowerCase(), keyValue[1].toLowerCase());
                        }
                        paid=upiPayment.upiPaymentDataOperation(myMap,CheckoutActivity.this);

                    } else {
                        Log.d(String.valueOf(CheckoutActivity.this.getClass()),"UPI:" + "onActivityResult: " + "Return data is null");
                        Map<String, String> myMap = new HashMap<String, String>();
                        myMap.put("status", "-1");
                        paid=upiPayment.upiPaymentDataOperation(myMap,CheckoutActivity.this);
                    }
                } else {
                    Log.d(String.valueOf(CheckoutActivity.this.getClass()),"UPI:" + "onActivityResult: " + "Return data is null"); //when user simply back without payment
                    Map<String, String> myMap = new HashMap<String, String>();
                    myMap.put("status", "-1");
                    paid=upiPayment.upiPaymentDataOperation(myMap,CheckoutActivity.this);
                }
                if(paid){
                    afterSuccessfulPayment();
                    Log.i(TAG,"User has paid");
                }else{
                    Log.i(TAG,"User has not paid");
                }
                break;
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);  //slide from left to right
        }
        return true;
    }
}