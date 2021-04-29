package com.dominicsilveira.one_q_shop.ui.product;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.dominicsilveira.one_q_shop.R;
import com.dominicsilveira.one_q_shop.ui.cart.CartActivity;
import com.dominicsilveira.one_q_shop.ui.cart.CartHistoryActivity;
import com.dominicsilveira.one_q_shop.utils.AppConstants;
import com.dominicsilveira.one_q_shop.utils.BasicUtils;
import com.dominicsilveira.one_q_shop.utils.ViewAnimationUtils;
import com.dominicsilveira.one_q_shop.utils.adapters.ProductListAdapter;
import com.dominicsilveira.oneqshoprestapi.api_calls.ApiListener;
import com.dominicsilveira.oneqshoprestapi.api_calls.ApiResponse;
import com.dominicsilveira.oneqshoprestapi.pojo_classes.Cart.CartDetails;
import com.dominicsilveira.oneqshoprestapi.pojo_classes.Product.CategoriesDetails;
import com.dominicsilveira.oneqshoprestapi.pojo_classes.Product.MiniCartDetails;
import com.dominicsilveira.oneqshoprestapi.pojo_classes.Product.ProductDetails;
import com.dominicsilveira.oneqshoprestapi.pojo_classes.Product.ProductRecommendations;
import com.dominicsilveira.oneqshoprestapi.rest_api.RestApiClient;
import com.dominicsilveira.oneqshoprestapi.rest_api.RestApiMethods;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;
import java.util.HashMap;
import java.util.Map;
import retrofit2.Call;

public class ProductDetailsActivity extends AppCompatActivity implements ApiListener {
    static String TAG = ProductDetailsActivity.class.getSimpleName();
    TextView priceText,productName,brandName,cart_count;
    ImageView productImage;
    AppBarLayout app_bar_layout;
    Drawable upArrow;
    LinearLayout categoryTags,in_cart,not_in_cart,lyt_expand_description;
    ImageButton bt_toggle_description;
    NestedScrollView nested_scroll_view;
    AppConstants globalClass;
    RecyclerView recyclerView;
    ProductListAdapter mAdapter;
    FloatingActionButton cart_increase,cart_decrease;
    AppCompatButton add_to_cart,update_cart,remove_from_cart;
    Intent prevIntent;
    Integer productId;
    String token;
    ProductDetails productDetails;
    RestApiMethods restMethods;
    MiniCartDetails miniCartDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        initPrevIntent();
        initToolbar();
        initComponents();
        loadProductRecommendations();
        setData();
        attachListeners();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("reload", 1);
        setResult(RESULT_OK, intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);  //slide from left to right
    }


    private void initPrevIntent() {
        prevIntent=getIntent();
        productDetails = (ProductDetails) prevIntent.getSerializableExtra("PRODUCT_DETAILS");
        productId=Integer.parseInt(productDetails.getBarcode());
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        BasicUtils.setActionBar(ProductDetailsActivity.this,null);
        app_bar_layout=findViewById(R.id.app_bar_layout);
        upArrow = ResourcesCompat.getDrawable(getResources(),R.drawable.ic_baseline_arrow_back_24_000000,null);
        app_bar_layout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset)-appBarLayout.getTotalScrollRange() == 0) {
//                    Log.i(TAG, "Collapsed");
                    BasicUtils.setColorFilter(upArrow, Color.parseColor("#FFFFFF"));
                    getSupportActionBar().setHomeAsUpIndicator(upArrow);
                } else {
//                    Log.i(TAG, "Expanded");
                    BasicUtils.setColorFilter(upArrow, Color.parseColor("#000000"));
                    getSupportActionBar().setHomeAsUpIndicator(upArrow);
                }
            }
        });
    }

    private void initComponents() {
        globalClass=(AppConstants)getApplicationContext();
        restMethods = RestApiClient.buildHTTPClient(); //Builds HTTP Client for API Calls
        token=BasicUtils.getSharedPreferencesString(ProductDetailsActivity.this,"TokenAuth","token","0");

        productName=findViewById(R.id.productName);
        brandName=findViewById(R.id.brandName);
        priceText=findViewById(R.id.priceText);
        productImage=findViewById(R.id.productImage);
        categoryTags=findViewById(R.id.categoryTags);
        nested_scroll_view=findViewById(R.id.nested_scroll_view);
        cart_increase=findViewById(R.id.cart_increase);
        cart_decrease=findViewById(R.id.cart_decrease);
        add_to_cart=findViewById(R.id.add_to_cart);
        update_cart=findViewById(R.id.update_cart);
        remove_from_cart=findViewById(R.id.remove_from_cart);
        in_cart=findViewById(R.id.in_cart);
        not_in_cart=findViewById(R.id.not_in_cart);
        cart_count=findViewById(R.id.cart_count);

        // section description
        bt_toggle_description = findViewById(R.id.bt_toggle_description);
        lyt_expand_description = findViewById(R.id.lyt_expand_description);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(ProductDetailsActivity.this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
    }

    private void loadProductRecommendations() {
        Map<String, String> data = new HashMap<String, String>();;
        Call<ProductRecommendations> req = restMethods.getProductRecommendationListDetails(productId,data);
        ApiResponse.callRetrofitApi(req, RestApiMethods.getProductRecommendationListDetailsRequest, this);
    }

    private void setData() {
        Log.e(TAG,"productId"+Integer.toString(productId));
        if(productId==-1){
            productName.setText(String.valueOf(productId));
        }else{
            productName.setText(productDetails.getName());
            brandName.setText(productDetails.getBrandDetails().getName());
            priceText.setText("₹ ".concat(productDetails.getPrice()));
            if(productDetails.getCartDetails()==null){
                updateNotInCartAndCartCount();
            }else{
                miniCartDetails=productDetails.getCartDetails();
                updateInCartAndCartCount(productDetails.getCartDetails().getCount());
            }

            Picasso.get().load(AppConstants.BACKEND_URL.concat(productDetails.getImagesDetails().get(0).getImage())).into(productImage);
            Log.e(TAG, AppConstants.BACKEND_URL.concat(productDetails.getImagesDetails().get(0).getImage()));
            for(final CategoriesDetails categoriesDetails:productDetails.getCategoriesDetails()){
                View categoryView = getLayoutInflater().inflate(R.layout.include_round_chips, null);
                Button chipName=categoryView.findViewById(R.id.chipName);
                chipName.setText(categoriesDetails.getName());
                chipName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(ProductDetailsActivity.this,ProductCategoriesActivity.class);
                        intent.putExtra("CATEGORY_ID",categoriesDetails.getId());
                        intent.putExtra("CATEGORY_NAME",categoriesDetails.getName());
                        startActivity(intent);
                    }
                });
                categoryTags.addView(categoryView);
            }
        }
    }

    private void attachListeners() {
        bt_toggle_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSection(view, lyt_expand_description);
            }
        });

        toggleArrow(bt_toggle_description);// expand first description
        lyt_expand_description.setVisibility(View.VISIBLE);

        cart_decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = Integer.parseInt(cart_count.getText().toString());
                if (qty > 1) {
                    miniCartDetails.setCount(--qty);
                    cart_count.setText(qty+"");
                }
            }
        });

        cart_increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = Integer.parseInt(cart_count.getText().toString());
                if (qty < productDetails.getCount()) {
                    miniCartDetails.setCount(++qty);
                    cart_count.setText(qty+"");
                }
            }
        });

        add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadUpdateCartDetailsAPI();
            }
        });
        update_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadUpdateCartDetailsAPI();
            }
        });
        remove_from_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<CartDetails> req = restMethods.deleteCartDetails(token,productDetails.getCartDetails().getId());
                ApiResponse.callRetrofitApi(req, RestApiMethods.deleteCartDetailsRequest, ProductDetailsActivity.this);
            }
        });
    }

    private void loadUpdateCartDetailsAPI() {
        Call<CartDetails> req = restMethods.updateCartDetails(token,globalClass.getUserObj().getId(),productId,miniCartDetails.getCount());
        ApiResponse.callRetrofitApi(req, RestApiMethods.updateCartDetailsRequest, ProductDetailsActivity.this);
    }



    @Override
    public void onApiResponse(String strApiName, int status, Object data, String error) {
        if (strApiName.equals(RestApiMethods.getProductRecommendationListDetailsRequest)) {
            ProductRecommendations productRecommendations = (ProductRecommendations) data;
            mAdapter = new ProductListAdapter(productRecommendations.getResults()); //set data and list adapter
            recyclerView.setAdapter(mAdapter);
        }
        if (strApiName.equals(RestApiMethods.updateCartDetailsRequest)) {
            CartDetails cartDetails = (CartDetails) data;
            miniCartDetails.setCount(cartDetails.getCount());
            updateInCartAndCartCount(cartDetails.getCount());
            Toast.makeText(ProductDetailsActivity.this,"Updated!",Toast.LENGTH_SHORT).show();
        }
        if (strApiName.equals(RestApiMethods.deleteCartDetailsRequest)) {
            updateNotInCartAndCartCount();
            Toast.makeText(ProductDetailsActivity.this,"Deleted!",Toast.LENGTH_SHORT).show();
        }
    }

    private void updateNotInCartAndCartCount() {
        miniCartDetails=new MiniCartDetails();
        miniCartDetails.setCount(1);
        not_in_cart.setVisibility(View.VISIBLE);
        in_cart.setVisibility(View.GONE);
        cart_count.setText("1");
    }

    private void updateInCartAndCartCount(Integer count) {
        not_in_cart.setVisibility(View.GONE);
        in_cart.setVisibility(View.VISIBLE);
        cart_count.setText(count+"");
    }

    private void toggleSection(View bt, final View lyt) {
        boolean show = toggleArrow(bt);
        if (show) {
            ViewAnimationUtils.expand(lyt, new ViewAnimationUtils.AnimListener() {
                @Override
                public void onFinish() {
                    BasicUtils.nestedScrollTo(nested_scroll_view, lyt);
                }
            });
        } else {
            ViewAnimationUtils.collapse(lyt);
        }
    }

    public boolean toggleArrow(View view) {
        if (view.getRotation() == 0) {
            view.animate().setDuration(200).rotation(180);
            return true;
        } else {
            view.animate().setDuration(200).rotation(0);
            return false;
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