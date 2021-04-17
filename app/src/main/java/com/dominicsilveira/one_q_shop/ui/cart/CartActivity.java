package com.dominicsilveira.one_q_shop.ui.cart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dominicsilveira.one_q_shop.R;
import com.dominicsilveira.one_q_shop.utils.AppConstants;
import com.dominicsilveira.one_q_shop.utils.BasicUtils;
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
    CartListAdapter mAdapter;
    RecyclerView.LayoutManager layoutManager;

    RestApiMethods restMethods;
    AppConstants globalClass;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        initComponents();
        attachListeners();
    }

    private void initComponents() {
        globalClass=(AppConstants)getApplicationContext();
        restMethods = RestApiClient.buildHTTPClient(); //Builds HTTP Client for API Calls
        token=BasicUtils.getToken(CartActivity.this);
        BasicUtils.setActionBar(CartActivity.this,"My Cart");


        recyclerView = (RecyclerView) findViewById(R.id.productListRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(CartActivity.this);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void attachListeners() {
        loadData();
    }

    private void loadData() {
        Map<String, String> data=new HashMap<String, String>();
        Call<CartListDetails> req = restMethods.getCartListDetails(token,data);
        ApiResponse.callRetrofitApi(req, RestApiMethods.getCartListDetailsRequest, this);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK) {
            int reload = data.getIntExtra("reload",0);
            if(reload==1){
                Log.i(TAG,"Reloading Cart...");
                loadData();
            }
        }
    }

    @Override
    public void onApiResponse(String strApiName, int status, Object data, String error) {
        if (strApiName.equals(RestApiMethods.getCartListDetailsRequest)) {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if(mAdapter!=null){
                    mAdapter.getFilter().filter(query);
                }
                return false;
            }
        });
        item.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}