package com.dominicsilveira.one_q_shop.ui.cart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.dominicsilveira.one_q_shop.R;
import com.dominicsilveira.one_q_shop.utils.AppConstants;
import com.dominicsilveira.one_q_shop.utils.BasicUtils;
import com.dominicsilveira.one_q_shop.utils.animations.SimpleToDeleteCallback;
import com.dominicsilveira.one_q_shop.utils.adapters.CartListAdapter;
import com.dominicsilveira.oneqshoprestapi.api_calls.ApiListener;
import com.dominicsilveira.oneqshoprestapi.api_calls.ApiResponse;
import com.dominicsilveira.oneqshoprestapi.pojo_classes.Cart.CartDetails;
import com.dominicsilveira.oneqshoprestapi.pojo_classes.Cart.CartListDetails;
import com.dominicsilveira.oneqshoprestapi.rest_api.RestApiClient;
import com.dominicsilveira.oneqshoprestapi.rest_api.RestApiMethods;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;


public class CartActivity extends AppCompatActivity implements ApiListener {
    static String TAG = CartActivity.class.getSimpleName();
    RecyclerView recyclerView;
    CartListAdapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    MaterialRippleLayout checkout_btn;
    RestApiMethods restMethods;
    AppConstants globalClass;
    String token;
    CartListDetails cartListDetails;
    TextView total_price;
    LinearLayout emptyView;
    TextView emptyViewTxt;
    List<CartDetails> cartDetails = new ArrayList<CartDetails>();
    boolean restored=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        initComponents();
        loadData();
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);  //slide from left to right
    }

    private void initComponents() {
        globalClass=(AppConstants)getApplicationContext();
        restMethods = RestApiClient.buildHTTPClient(); //Builds HTTP Client for API Calls
        token=BasicUtils.getSharedPreferencesString(CartActivity.this,"TokenAuth","token","0");

        BasicUtils.setActionBar(CartActivity.this,"My Cart");

        checkout_btn=findViewById(R.id.checkout_btn);
        total_price=findViewById(R.id.total_price);
        emptyView=findViewById(R.id.emptyView);
        emptyViewTxt=findViewById(R.id.emptyViewTxt);
        recyclerView = (RecyclerView) findViewById(R.id.cartListRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(CartActivity.this);
        recyclerView.setLayoutManager(layoutManager);

    }

    private void loadData() {
        SimpleToDeleteCallback itemTouchHelperCallback=new SimpleToDeleteCallback(CartActivity.this) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                final int position=viewHolder.getAdapterPosition();
                final CartDetails data = cartDetails.get(position);
                restored=false;
                Snackbar snackbar = Snackbar
                        .make(recyclerView, "Removed", Snackbar.LENGTH_LONG)
                        .addCallback(new Snackbar.Callback() {
                            @Override
                            public void onDismissed(Snackbar snackbar, int event) {
                                if(!restored){
                                    Call<CartDetails> req = restMethods.deleteCartDetails(token,data.getId());
                                    ApiResponse.callRetrofitApi(req, RestApiMethods.deleteCartDetailsRequest, CartActivity.this);
                                    cartListDetails.setCount(cartListDetails.getCount()-1);
                                    cartListDetails.setPrice( cartListDetails.getPrice() - (data.getCount() * Double.parseDouble(data.getCartDetails().getPrice())) );
                                    total_price.setText("₹ ".concat(Double.toString(cartListDetails.getPrice())));
                                }
                                Log.i(TAG,"onDismiss");
                            }

                            @Override
                            public void onShown(Snackbar snackbar) {}
                        }).setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                restored=true;
                                cartDetails.add(position, data);
                                mAdapter.notifyItemInserted(position);
                                recyclerView.scrollToPosition(position);
                                checkEmpty();
                            }
                        });
                snackbar.show();
                cartDetails.remove(position);
                mAdapter.notifyItemRemoved(position);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        Map<String, String> data=new HashMap<String, String>();
        Call<CartListDetails> req = restMethods.getCartListDetails(token,data);
        ApiResponse.callRetrofitApi(req, RestApiMethods.getCartListDetailsRequest, this);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConstants.PRODUCT_CART_PAGE_RELOAD_REQUEST && resultCode == RESULT_OK) {
            int reload = data.getIntExtra("reload",0);
            if(reload==1){
                Log.i(TAG,"Reloading Cart...");
                loadData();
            }
        }
    }

    @Override
    public void onApiResponse(String strApiName, int status, Object data, int error) {
        if (strApiName.equals(RestApiMethods.getCartListDetailsRequest)) {
            if(error!=1){
                cartListDetails = (CartListDetails) data;
                cartDetails=cartListDetails.getResults();
                total_price.setText("₹ ".concat(Double.toString(cartListDetails.getPrice())));
                mAdapter = new CartListAdapter(cartDetails);
                recyclerView.setAdapter(mAdapter);
                mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                    @Override
                    public void onChanged() {
                        super.onChanged();
                        emptyViewTxt.setText("No items match your Search...");
                        checkEmpty();
                    }
                    @Override
                    public void onItemRangeInserted(int positionStart, int itemCount) {
                        super.onItemRangeInserted(positionStart, itemCount);
                    }
                    @Override
                    public void onItemRangeRemoved(int positionStart, int itemCount) {
                        super.onItemRangeRemoved(positionStart, itemCount);
                        emptyViewTxt.setText("Cart is Empty");
                        checkEmpty();
                    }
                });
                emptyViewTxt.setText("Cart is Empty");
                checkEmpty();
                LinearLayoutManager llm = (LinearLayoutManager) recyclerView.getLayoutManager();
                llm.scrollToPositionWithOffset(0, 0);

                checkout_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(CartActivity.this, CheckoutActivity.class);
                        intent.putExtra("CART_DETAILS",cartListDetails);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                });
            }else{
                emptyViewTxt.setText("Cart is Empty");
                checkEmpty();
                Toast.makeText(CartActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        }
        if (strApiName.equals(RestApiMethods.deleteCartDetailsRequest)) {
            Toast.makeText(CartActivity.this,"Deleted!",Toast.LENGTH_SHORT).show();
        }
    }

    private void checkEmpty() {
        Log.i(TAG, "Check isEmpty"+(mAdapter.getItemCount() == 0 ? "View.VISIBLE" : "View.GONE"));
        emptyView.setVisibility(mAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(mAdapter.getItemCount() == 0 ? View.GONE : View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_cart_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if(mAdapter!=null)
                    mAdapter.getFilter().filter(query);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        if (item.getItemId() == R.id.action_history) {
            intent=new Intent(CartActivity.this, CartHistoryActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);  //slide from left to right
        }
        return true;
    }
}