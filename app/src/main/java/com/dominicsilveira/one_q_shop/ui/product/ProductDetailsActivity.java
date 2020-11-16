package com.dominicsilveira.one_q_shop.ui.product;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;
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
import com.dominicsilveira.one_q_shop.R;
import com.dominicsilveira.one_q_shop.jsonschema2pojo_classes.Product.CategoriesDetails;
import com.dominicsilveira.one_q_shop.jsonschema2pojo_classes.Product.ProductDetails;
import com.dominicsilveira.one_q_shop.utils.AppConstants;
import com.dominicsilveira.one_q_shop.utils.BasicUtils;
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

//        // section description
//        bt_toggle_description = (ImageButton) findViewById(R.id.bt_toggle_description);
//        lyt_expand_description = (View) findViewById(R.id.lyt_expand_description);
//        bt_toggle_description.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                toggleSection(view, lyt_expand_description);
//            }
//        });



    }

}