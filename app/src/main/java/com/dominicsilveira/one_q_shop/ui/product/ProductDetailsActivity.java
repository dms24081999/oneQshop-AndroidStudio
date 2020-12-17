package com.dominicsilveira.one_q_shop.ui.product;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.bumptech.glide.request.animation.ViewAnimation;
import com.dominicsilveira.one_q_shop.R;
import com.dominicsilveira.one_q_shop.utils.AppConstants;
import com.dominicsilveira.one_q_shop.utils.BasicUtils;
import com.dominicsilveira.one_q_shop.utils.ViewAnimationUtils;
import com.dominicsilveira.oneqshoprestapi.pojo_classes.Product.CategoriesDetails;
import com.dominicsilveira.oneqshoprestapi.pojo_classes.Product.ProductDetails;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hootsuite.nachos.NachoTextView;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProductDetailsActivity extends AppCompatActivity {

    TextView priceText,productName,brandName;
    ImageView productImage;
    AppBarLayout app_bar_layout;
    Drawable upArrow;
    LinearLayout categoryTags;
    ImageButton bt_toggle_description;
    LinearLayout lyt_expand_description;
    NestedScrollView nested_scroll_view;

    Intent prevIntent;
    Integer productId;
    ProductDetails productDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        initPrevIntent();
        initToolbar();
        initComponents();
        attachListeners();
    }


    private void initPrevIntent() {
        prevIntent=getIntent();
        productId=prevIntent.getIntExtra("BARCODE_VALUE",-1);
        productDetails = (ProductDetails) prevIntent.getSerializableExtra("PRODUCT_DETAILS");
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        app_bar_layout=findViewById(R.id.app_bar_layout);
        upArrow = ResourcesCompat.getDrawable(getResources(),R.drawable.ic_baseline_arrow_back_000000_24,null);
        app_bar_layout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset)-appBarLayout.getTotalScrollRange() == 0) {
//                    Log.i("ProductDetailsActivity", "Collapsed");
                    BasicUtils.setColorFilter(upArrow, Color.parseColor("#FFFFFF"));
                    getSupportActionBar().setHomeAsUpIndicator(upArrow);
                } else {
//                    Log.i("ProductDetailsActivity", "Expanded");
                    BasicUtils.setColorFilter(upArrow, Color.parseColor("#000000"));
                    getSupportActionBar().setHomeAsUpIndicator(upArrow);
                }
            }
        });
    }

    private void initComponents() {
        productName=findViewById(R.id.productName);
        brandName=findViewById(R.id.brandName);
        priceText=findViewById(R.id.priceText);
        productImage=findViewById(R.id.productImage);
        categoryTags=findViewById(R.id.categoryTags);
        nested_scroll_view=findViewById(R.id.nested_scroll_view);

        // section description
        bt_toggle_description = findViewById(R.id.bt_toggle_description);
        lyt_expand_description = findViewById(R.id.lyt_expand_description);


        if(productId!=-1){
            productName.setText(String.valueOf(productId));
        }else{
            productName.setText(productDetails.getName());
            brandName.setText(productDetails.getBrandDetails().getName());
            priceText.setText("₹ ".concat(productDetails.getPrice()));
            Picasso.get().load(AppConstants.BACKEND_URL.concat(productDetails.getImagesDetails().get(0).getImage())).into(productImage);
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

        // expand first description
        toggleArrow(bt_toggle_description);
        lyt_expand_description.setVisibility(View.VISIBLE);
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

}