package com.dominicsilveira.one_q_shop.ui.product;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.dominicsilveira.one_q_shop.R;
import com.dominicsilveira.one_q_shop.utils.AppConstants;
import com.dominicsilveira.one_q_shop.utils.adapters.ProductListAdapter;
import com.dominicsilveira.oneqshoprestapi.api_calls.ApiListener;
import com.dominicsilveira.oneqshoprestapi.api_calls.ApiResponse;
import com.dominicsilveira.oneqshoprestapi.rest_api.RestApiClient;
import com.dominicsilveira.oneqshoprestapi.rest_api.RestApiMethods;
import com.dominicsilveira.oneqshoprestapi.pojo_classes.Product.ProductDetails;
import com.dominicsilveira.oneqshoprestapi.pojo_classes.Product.ProductListDetails;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import retrofit2.Call;

public class ProductCategoriesActivity extends AppCompatActivity implements ApiListener {
    static String TAG = ProductCategoriesActivity.class.getSimpleName();
    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    LinearLayout backBtn,nextBtn;

    RestApiMethods restMethods;
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
        globalClass=(AppConstants)getApplicationContext();
        restMethods = RestApiClient.buildHTTPClient(); //Builds HTTP Client for API Calls

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
            data = nextURL;
        else
            data = new HashMap<String, String>();
        if(categoryId!=-1)
            data.put("category", String.valueOf(categoryId));
        Call<ProductListDetails> req = restMethods.getProductListDetails(data);
        ApiResponse.callRetrofitApi(req, RestApiMethods.getProductListDetailsRequest, this);
    }

    @Override
    public void onApiResponse(String strApiName, int status, Object data, String error) {
        if (strApiName.equals(RestApiMethods.getProductListDetailsRequest)) {
            if(data!=null){
                ProductListDetails productListDetails = (ProductListDetails) data;
                productDetailsArrayList=productListDetails.getResults();
                nextURL=getQueryMap(productListDetails.getNext());
                backURL=getQueryMap(productListDetails.getPrevious());
                mAdapter = new ProductListAdapter(productDetailsArrayList);
                recyclerView.setAdapter(mAdapter);
                LinearLayoutManager llm = (LinearLayoutManager) recyclerView.getLayoutManager();
                llm.scrollToPositionWithOffset(0, 0);
                Log.i(TAG, String.valueOf(productDetailsArrayList));
            }else{
                Toast.makeText(ProductCategoriesActivity.this, "Error "+error, Toast.LENGTH_SHORT).show();
            }
        }
    }
}