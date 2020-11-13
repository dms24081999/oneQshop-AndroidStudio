package com.dominicsilveira.one_q_shop.ui.dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductCategoriesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    LinearLayout backBtn,nextBtn;

    RestMethods restMethods;
    List<ProductDetails> productDetailsArrayList=new ArrayList<ProductDetails>();
    AppConstants globalClass;
    Integer categoryId;
    String categoryName;
    Map<String, String> nextURL,backURL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_categories);

        initComponents();
        attachListeners();
    }

    private void initComponents() {
        Intent intent=getIntent();
        categoryId=intent.getIntExtra("CATEGORY_ID",-1);
        categoryName=intent.getStringExtra("CATEGORY_NAME");
        if(categoryName==null)
            categoryName="All Categories";

        globalClass=(AppConstants)getApplicationContext();
        //Builds HTTP Client for API Calls
        restMethods = RestClient.buildHTTPClient();

        getSupportActionBar().setTitle(categoryName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        backBtn=findViewById(R.id.backBtn);
        nextBtn=findViewById(R.id.nextBtn);
        recyclerView = (RecyclerView) findViewById(R.id.productListRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(ProductCategoriesActivity.this);
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

    public static Map<String, String> getQueryMap(String urlString) {
        Map<String, String> map = new HashMap<String, String>();
        if(urlString==null) return map;
        String url = urlString.split("\\?")[1];;
        String[] params = url.split("&");
        for (String param : params) {
            String name = param.split("=")[0];
            String value = param.split("=")[1];
            map.put(name, value);
        }
        return map;
    }

    private void loadData(Boolean goBack,Boolean goNext) {
        Map<String, String> data;
        productDetailsArrayList.clear();
        if(goBack)
            data = backURL;
        else if(goNext)
            data=nextURL;
        else
            data = new HashMap<String, String>();
        if(categoryId!=-1)
            data.put("category", String.valueOf(categoryId));
        Call<ProductListDetails> req = restMethods.getProductListDetails(data);
        req.enqueue(new Callback<ProductListDetails>() {
            @Override
            public void onResponse(Call<ProductListDetails> call, Response<ProductListDetails> response) {
                Toast.makeText(ProductCategoriesActivity.this, response.code() + " ", Toast.LENGTH_SHORT).show();
                if (response.isSuccessful()) {
                    productDetailsArrayList=response.body().getResults();
                    nextURL=getQueryMap(response.body().getNext());
                    backURL=getQueryMap(response.body().getPrevious());
                    mAdapter = new ProductListAdapter(productDetailsArrayList);
                    recyclerView.setAdapter(mAdapter);
                    Log.i(String.valueOf(ProductCategoriesActivity.this.getComponentName().getClassName()), String.valueOf(response.code())+" "+productDetailsArrayList);
                } else {
                    Toast.makeText(ProductCategoriesActivity.this, "Request failed!", Toast.LENGTH_SHORT).show();
                    Gson gson = new Gson();
                    ErrorMessage error=gson.fromJson(response.errorBody().charStream(),ErrorMessage.class);
                    Log.i(String.valueOf(ProductCategoriesActivity.this.getComponentName().getClassName()), String.valueOf(error.getMessage()));
                }
            }
            @Override
            public void onFailure(Call<ProductListDetails> call, Throwable t) {
                Toast.makeText(ProductCategoriesActivity.this, "Request failed", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }
}