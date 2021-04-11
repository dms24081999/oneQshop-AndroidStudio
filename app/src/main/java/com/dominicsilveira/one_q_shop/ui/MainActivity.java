package com.dominicsilveira.one_q_shop.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.dominicsilveira.one_q_shop.R;

import com.dominicsilveira.one_q_shop.utils.AppConstants;

import com.dominicsilveira.oneqshoprestapi.rest_api.RestApiClient;
import com.dominicsilveira.oneqshoprestapi.rest_api.RestApiMethods;
import com.dominicsilveira.oneqshoprestapi.pojo_classes.User.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    Toolbar mToolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    AppBarConfiguration mAppBarConfiguration;
    BottomNavigationView bottomNavigation;
    CircularImageView userAvatar;
    RestApiMethods restMethods;
    List<Integer> arr=new ArrayList<>();
    User userObj;
    AppConstants globalClass;
    TextView userName,userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Builds HTTP Client for API Calls
        restMethods = RestApiClient.buildHTTPClient();
        globalClass=(AppConstants)getApplicationContext();
        userObj=globalClass.getUserObj();

        // Passing each menu ID as a set of Ids because each menu should be considered as top level destinations.
        mToolbar = (Toolbar) findViewById ( R.id.toolbar );
        setSupportActionBar ( mToolbar );
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Bottom Nav
        BottomNavigationView navView = findViewById(R.id.bottom_navigation);
        // Passing each menu ID as a set of Ids because each menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_dashboard, R.id.navigation_search,R.id.navigation_scan,R.id.navigation_cart,R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        Intent mIntent = getIntent();
        int bottomInt= mIntent.getIntExtra("FRAGMENT_NO",0);
        if(bottomInt==0){
            navView.setSelectedItemId(R.id.navigation_dashboard);
        }else if(bottomInt==1){
            navView.setSelectedItemId(R.id.navigation_scan);
        }else if(bottomInt==2){
            navView.setSelectedItemId(R.id.navigation_search);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
