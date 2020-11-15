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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.dominicsilveira.one_q_shop.R;
import com.dominicsilveira.one_q_shop.jsonschema2pojo_classes.Product.ProductDetails;
import com.dominicsilveira.one_q_shop.utils.AppConstants;
import com.dominicsilveira.one_q_shop.utils.BasicUtils;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProductDetailsActivity extends AppCompatActivity {

    TextView priceText,productName,brandName;
    ImageView productImage;
    AppBarLayout app_bar_layout;
    Drawable upArrow;

    Integer productId;
    ProductDetails productDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        initToolbar();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        upArrow = ResourcesCompat.getDrawable(getResources(),R.drawable.ic_baseline_arrow_back_000000_24,null);


        productName=findViewById(R.id.productName);
        brandName=findViewById(R.id.brandName);
        priceText=findViewById(R.id.priceText);
        productImage=findViewById(R.id.productImage);
        app_bar_layout=findViewById(R.id.app_bar_layout);

        Intent intent=getIntent();
        productId=intent.getIntExtra("BARCODE_VALUE",-1);
        Log.i("ProductDetailsActivity", String.valueOf(productId));
        if(productId!=-1){
            productName.setText(String.valueOf(productId));
        }else{
            productDetails = (ProductDetails) intent.getSerializableExtra("PRODUCT_DETAILS");
            productName.setText(productDetails.getName());
            if(productDetails.getBrandDetails()!=null){
                brandName.setText(productDetails.getBrandDetails().getName());
            }else{
                brandName.setText("one-Q-shop");
            }
            priceText.setText("₹ ".concat(productDetails.getPrice()));
            Picasso.get().load(AppConstants.BACKEND_URL.concat(productDetails.getImagesDetails().get(0).getImage())).into(productImage);
        }


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

}