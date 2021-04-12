package com.dominicsilveira.one_q_shop.ui.cart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dominicsilveira.one_q_shop.R;
import com.dominicsilveira.one_q_shop.utils.AppConstants;
import com.dominicsilveira.one_q_shop.utils.adapters.CartListAdapter;
import com.dominicsilveira.oneqshoprestapi.api_calls.ApiListener;
import com.dominicsilveira.oneqshoprestapi.api_calls.ApiResponse;
import com.dominicsilveira.oneqshoprestapi.pojo_classes.Cart.CartListDetails;
import com.dominicsilveira.oneqshoprestapi.pojo_classes.Product.ProductListDetails;
import com.dominicsilveira.oneqshoprestapi.rest_api.RestApiClient;
import com.dominicsilveira.oneqshoprestapi.rest_api.RestApiMethods;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;


public class CartActivity extends AppCompatActivity implements ApiListener {
    static String TAG = com.dominicsilveira.one_q_shop.ui.product.ProductCategoriesActivity.class.getSimpleName();
    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    LinearLayout backBtn,nextBtn;

    RestApiMethods restMethods;
    AppConstants globalClass;
    Integer categoryId;
    String categoryName;
    Map<String, String> nextURL,backURL;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_categories);
        initComponents();
        attachListeners();
    }

    private void initComponents() {
        Intent intent=getIntent();
        globalClass=(AppConstants)getApplicationContext();
        restMethods = RestApiClient.buildHTTPClient(); //Builds HTTP Client for API Calls

        SharedPreferences sh = getSharedPreferences("TokenAuth", MODE_PRIVATE);// The value will be default as empty string because for the very first time when the app is opened, there is nothing to show
        token=sh.getString("token", "0");// We can then use the data

        categoryId=intent.getIntExtra("CATEGORY_ID",-1);
        categoryName=intent.getStringExtra("CATEGORY_NAME");
        if(categoryName==null)
            categoryName="All Categories";

        getSupportActionBar().setTitle(categoryName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        backBtn=findViewById(R.id.backBtn);
        nextBtn=findViewById(R.id.nextBtn);
        recyclerView = (RecyclerView) findViewById(R.id.productListRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(CartActivity.this);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void attachListeners() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadData(true,false);
            }
        });
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadData(false,true);
            }
        });
        loadData(false,false);
    }

    private void loadData(Boolean goBack,Boolean goNext) {
        Map<String, String> data=new HashMap<String, String>();
        Call<CartListDetails> req = restMethods.getCartListDetails(token,data);
        ApiResponse.callRetrofitApi(req, RestApiMethods.getProductListDetailsRequest, this);
    }

    @Override
    public void onApiResponse(String strApiName, int status, Object data, String error) {
        if (strApiName.equals(RestApiMethods.getProductListDetailsRequest)) {
            if(data!=null){
                CartListDetails cartListDetails = (CartListDetails) data;
                mAdapter = new CartListAdapter(cartListDetails.getResults());
                recyclerView.setAdapter(mAdapter);
                LinearLayoutManager llm = (LinearLayoutManager) recyclerView.getLayoutManager();
                llm.scrollToPositionWithOffset(0, 0);
//                Log.i(TAG, String.valueOf(productDetailsArrayList));
            }else{
                Toast.makeText(CartActivity.this, "Error "+error, Toast.LENGTH_SHORT).show();
            }
        }
    }
}