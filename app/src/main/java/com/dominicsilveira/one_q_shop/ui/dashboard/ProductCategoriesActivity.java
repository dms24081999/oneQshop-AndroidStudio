package com.dominicsilveira.one_q_shop.ui.dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.dominicsilveira.one_q_shop.R;
import com.dominicsilveira.one_q_shop.jsonschema2pojo_classes.Product.ProductDetails;
import com.dominicsilveira.one_q_shop.utils.AppConstants;
import com.dominicsilveira.one_q_shop.utils.adapters.ProductListAdapter;

import java.util.ArrayList;
import java.util.List;

public class ProductCategoriesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    List<ProductDetails> productDetailsArrayList=new ArrayList<ProductDetails>();
    AppConstants globalClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_categories);

        initComponents();
        attachListeners();
    }

    private void initComponents() {
        globalClass=(AppConstants)getApplicationContext();

        getSupportActionBar().setTitle("All Categories");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.productListRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(ProductCategoriesActivity.this);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void attachListeners() {
        productDetailsArrayList.add(new ProductDetails("112323","dms"));
        productDetailsArrayList.add(new ProductDetails("123","dms"));
        productDetailsArrayList.add(new ProductDetails("121233","dms"));
        productDetailsArrayList.add(new ProductDetails("123","dms"));
        productDetailsArrayList.add(new ProductDetails("12123","dms"));
        productDetailsArrayList.add(new ProductDetails("123","dms"));
        productDetailsArrayList.add(new ProductDetails("14523","dms"));
        productDetailsArrayList.add(new ProductDetails("12453","dms"));
        productDetailsArrayList.add(new ProductDetails("123","dms"));
        productDetailsArrayList.add(new ProductDetails("123","dms"));
        productDetailsArrayList.add(new ProductDetails("12783","dms"));
        productDetailsArrayList.add(new ProductDetails("127893","dms"));
        productDetailsArrayList.add(new ProductDetails("12893","dms"));
        productDetailsArrayList.add(new ProductDetails("1278","dms"));
        productDetailsArrayList.add(new ProductDetails("1893","dms"));
        productDetailsArrayList.add(new ProductDetails("1273","dms"));
        mAdapter = new ProductListAdapter(productDetailsArrayList);
        recyclerView.setAdapter(mAdapter);
    }
}