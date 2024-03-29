package com.dominicsilveira.one_q_shop.ui.cart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.dominicsilveira.one_q_shop.R;
import com.dominicsilveira.one_q_shop.utils.BasicUtils;
import com.dominicsilveira.one_q_shop.utils.animations.LineItemDecoration;
import com.dominicsilveira.one_q_shop.utils.adapters.InvoiceListAdapter;
import com.dominicsilveira.oneqshoprestapi.api_calls.ApiListener;
import com.dominicsilveira.oneqshoprestapi.api_calls.ApiResponse;
import com.dominicsilveira.oneqshoprestapi.pojo_classes.Invoice.InvoiceListDetails;
import com.dominicsilveira.oneqshoprestapi.rest_api.RestApiClient;
import com.dominicsilveira.oneqshoprestapi.rest_api.RestApiMethods;
import retrofit2.Call;

public class CartHistoryActivity extends AppCompatActivity implements ApiListener {
    static String TAG = CartHistoryActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private InvoiceListAdapter mAdapter;
    RestApiMethods restMethods;
    String token;
    LinearLayout emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_history);

        initComponent();
    }

    private void initComponent() {
        restMethods = RestApiClient.buildHTTPClient(); //Builds HTTP Client for API Calls
        token = BasicUtils.getSharedPreferencesString(CartHistoryActivity.this,"TokenAuth","token","0");

        BasicUtils.setActionBar(CartHistoryActivity.this,"Cart History");
        recyclerView = (RecyclerView) findViewById(R.id.invoiceListRecyclerView);
        emptyView=findViewById(R.id.emptyView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new LineItemDecoration(this, LinearLayout.VERTICAL));
        recyclerView.setHasFixedSize(true);

        Call<InvoiceListDetails> req = restMethods.getInvoiceListDetails(token);
        ApiResponse.callRetrofitApi(req, RestApiMethods.getInvoiceListDetailsRequest, this);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);  //slide from left to right
    }

    @Override
    public void onApiResponse(String strApiName, int status, Object data, int error) {
        if (strApiName.equals(RestApiMethods.getInvoiceListDetailsRequest)) {
            if(error!=1){
                InvoiceListDetails invoiceListDetails = (InvoiceListDetails) data;
                mAdapter = new InvoiceListAdapter(CartHistoryActivity.this,invoiceListDetails.getResults());
                recyclerView.setAdapter(mAdapter);
                checkHistoryEmpty();
            }else{
                Toast.makeText(CartHistoryActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void checkHistoryEmpty() {
        Log.i(TAG, "Check isEmpty"+(mAdapter.getItemCount() == 0 ? "View.VISIBLE" : "View.GONE"));
        emptyView.setVisibility(mAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(mAdapter.getItemCount() == 0 ? View.GONE : View.VISIBLE);
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