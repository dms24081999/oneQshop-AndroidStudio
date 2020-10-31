package com.dominicsilveira.one_q_shop;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.dominicsilveira.one_q_shop.R;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        BottomNavigationView navView = findViewById(R.id.nav_view);
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_dashboard,  R.id.navigation_search, R.id.navigation_scan,  R.id.navigation_cart, R.id.navigation_profile)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController(navView, navController);
//
//        Intent mIntent = getIntent();
//        int bottomInt= mIntent.getIntExtra("FRAGMENT_NO",0);
//        if(bottomInt==0){
//            navView.setSelectedItemId(R.id.navigation_dashboard);
//        }else if(bottomInt==1){
//            navView.setSelectedItemId(R.id.navigation_scan);
//        }else if(bottomInt==2){
//            navView.setSelectedItemId(R.id.navigation_profile);
//        }
//    }
    Toolbar mToolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    AppBarConfiguration mAppBarConfiguration;
    BottomNavigationView bottomNavigation;
    List<Integer> arr=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mToolbar = (Toolbar) findViewById ( R.id.toolbar );
        setSupportActionBar ( mToolbar );
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        drawerLayout = findViewById(R.id.navigation_layout);
        navigationView = findViewById ( R.id.navigation_view );
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_settings, R.id.nav_feedback,
                R.id.navigation_dashboard, R.id.navigation_search, R.id.navigation_scan,  R.id.navigation_cart, R.id.navigation_profile)
                .setDrawerLayout(drawerLayout)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        NavigationUI.setupWithNavController(bottomNavigation, navController);

//        View navView = navigationView.inflateHeaderView ( R.layout.navigation_header_layout);
//        NavProfileImage = navView.findViewById ( R.id.nav_profile_image );
//        NavProfileFullName =  navView.findViewById ( R.id.nav_user_full_name );
//        NavProfileEmail =  navView.findViewById ( R.id.nav_user_email );

        arr.addAll(Arrays.asList(R.id.nav_feedback,R.id.nav_settings));
        //Handle visibility of the application bottom navigation
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if(arr.contains(destination.getId())){
                    bottomNavigation.setVisibility(View.GONE);
                }
                else{
                    bottomNavigation.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

}
