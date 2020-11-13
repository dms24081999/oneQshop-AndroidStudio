package com.dominicsilveira.one_q_shop.ui.dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.dominicsilveira.one_q_shop.MainActivity;
import com.dominicsilveira.one_q_shop.R;
import com.dominicsilveira.one_q_shop.RegisterLogin.LoginActivity;
import com.dominicsilveira.one_q_shop.RegisterLogin.ResetPasswordActivity;
import com.dominicsilveira.one_q_shop.RegisterLogin.SplashScreen;
import com.dominicsilveira.one_q_shop.jsonschema2pojo_classes.ErrorMessage;
import com.dominicsilveira.one_q_shop.jsonschema2pojo_classes.Product.ProductDetails;
import com.dominicsilveira.one_q_shop.jsonschema2pojo_classes.Product.ProductListDetails;
import com.dominicsilveira.one_q_shop.jsonschema2pojo_classes.User.User;
import com.dominicsilveira.one_q_shop.utils.AppConstants;
import com.dominicsilveira.one_q_shop.utils.adapters.ProductListAdapter;
import com.dominicsilveira.one_q_shop.utils.api.RestClient;
import com.dominicsilveira.one_q_shop.utils.api.RestMethods;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductCategoriesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    RestMethods restMethods;
    List<ProductDetails> productDetailsArrayList=new ArrayList<ProductDetails>();
    AppConstants globalClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_categories);

        initComponents();
        attachListeners();
    }

    private void initComponents() {
        globalClass=(AppConstants)getApplicationContext();

        //Builds HTTP Client for API Calls
        restMethods = RestClient.buildHTTPClient();

        getSupportActionBar().setTitle("All Categories");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.productListRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(ProductCategoriesActivity.this);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void attachListeners() {

        Call<ProductListDetails> req = restMethods.getProductListDetails();
        Log.i(String.valueOf(ProductCategoriesActivity.this.getComponentName().getClassName()), "Called1");
        req.enqueue(new Callback<ProductListDetails>() {
            @Override
            public void onResponse(Call<ProductListDetails> call, Response<ProductListDetails> response) {
                Toast.makeText(ProductCategoriesActivity.this, response.code() + " ", Toast.LENGTH_SHORT).show();
                Log.i(String.valueOf(ProductCategoriesActivity.this.getComponentName().getClassName()), "Called2");
                if (response.isSuccessful()) {
                    productDetailsArrayList=response.body().getResults();
                    mAdapter = new ProductListAdapter(productDetailsArrayList);
                    recyclerView.setAdapter(mAdapter);
                    Log.i(String.valueOf(ProductCategoriesActivity.this.getComponentName().getClassName()), "Called3");
                    Log.i(String.valueOf(ProductCategoriesActivity.this.getComponentName().getClassName()), String.valueOf(response.code())+" "+productDetailsArrayList);
                } else {
                    Log.i(String.valueOf(ProductCategoriesActivity.this.getComponentName().getClassName()), "Called4");
                    Toast.makeText(ProductCategoriesActivity.this, "Request failed!", Toast.LENGTH_SHORT).show();
                    Gson gson = new Gson();
                    ErrorMessage error=gson.fromJson(response.errorBody().charStream(),ErrorMessage.class);
                    Log.i(String.valueOf(ProductCategoriesActivity.this.getComponentName().getClassName()), String.valueOf(error.getMessage()));
                }
            }
            @Override
            public void onFailure(Call<ProductListDetails> call, Throwable t) {
                Log.i(String.valueOf(ProductCategoriesActivity.this.getComponentName().getClassName()), "Called5");
                Toast.makeText(ProductCategoriesActivity.this, "Request failed", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });

    }
}