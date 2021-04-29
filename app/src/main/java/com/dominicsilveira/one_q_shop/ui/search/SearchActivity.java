package com.dominicsilveira.one_q_shop.ui.search;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.dominicsilveira.one_q_shop.R;
import com.dominicsilveira.one_q_shop.utils.AppConstants;
import com.dominicsilveira.one_q_shop.utils.BasicUtils;
import com.dominicsilveira.one_q_shop.utils.adapters.ProductListAdapter;
import com.dominicsilveira.oneqshoprestapi.api_calls.ApiListener;
import com.dominicsilveira.oneqshoprestapi.api_calls.ApiResponse;
import com.dominicsilveira.oneqshoprestapi.pojo_classes.Product.ProductDetails;
import com.dominicsilveira.oneqshoprestapi.pojo_classes.Product.ProductListDetails;
import com.dominicsilveira.oneqshoprestapi.rest_api.RestApiClient;
import com.dominicsilveira.oneqshoprestapi.rest_api.RestApiMethods;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import retrofit2.Call;


public class SearchActivity extends AppCompatActivity  implements ApiListener {
    private static String TAG = SearchActivity.class.getSimpleName();

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    LinearLayout backBtn,nextBtn,recyclerLinearLayout;

    RestApiMethods restMethods;
    List<ProductDetails> productDetailsArrayList=new ArrayList<ProductDetails>();
    AppConstants globalClass;
    String searchQuery="",token;
    Map<String, String> nextURL,backURL;

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);  //slide from left to right
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);  //slide from left to right
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initComponents();
        attachListeners();
        loadData(false,false);
    }

    private void initComponents() {
        globalClass=(AppConstants)getApplicationContext();
        restMethods = RestApiClient.buildHTTPClient(); //Builds HTTP Client for API Calls
        token=BasicUtils.getSharedPreferencesString(SearchActivity.this,"TokenAuth","token","0");

        backBtn=findViewById(R.id.backBtn);
        nextBtn=findViewById(R.id.nextBtn);

        recyclerView = (RecyclerView) findViewById(R.id.productListRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(SearchActivity.this);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void attachListeners() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                recyclerView.smoothScrollToPosition(0);
                loadData(true,false);
            }
        });
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                recyclerView.smoothScrollToPosition(0);
                loadData(false,true);
            }
        });
    }

    private void loadData(Boolean goBack,Boolean goNext) {
        Map<String, String> data;
        productDetailsArrayList.clear();
        if(goBack)
            data = backURL;
        else if(goNext)
            data = nextURL;
        else{
            data = new HashMap<String, String>();
            data.put("s", searchQuery);
        }
        Call<ProductListDetails> req = restMethods.getProductListDetails(token,data);
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
                Toast.makeText(SearchActivity.this, "Error "+error, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static Map<String, String> getQueryMap(String urlString) {
        Map<String, String> map = new HashMap<String, String>();
        if(urlString==null) return map;
        String url = urlString.split("\\?")[1];;
        String[] params = url.split("&");
        for (String param : params) {
            try{
                String name = param.split("=")[0];
                String value = param.split("=")[1];
                map.put(name, value);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return map;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        item.expandActionView();
        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchQuery=query;
                loadData(false,false);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
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
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);  //slide from left to right
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}