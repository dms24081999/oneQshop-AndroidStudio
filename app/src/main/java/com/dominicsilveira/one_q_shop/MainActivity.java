package com.dominicsilveira.one_q_shop;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.dominicsilveira.one_q_shop.jsonschema2pojo_classes.User;
import com.dominicsilveira.one_q_shop.utils.AppConstants;
import com.dominicsilveira.one_q_shop.utils.api.RestClient;
import com.dominicsilveira.one_q_shop.utils.api.RestMethods;
import com.dominicsilveira.one_q_shop.utils.api.CallbackUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.mikhaellopez.circularimageview.CircularImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CallbackUtils.AsyncResponse{

    Toolbar mToolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    AppBarConfiguration mAppBarConfiguration;
    BottomNavigationView bottomNavigation;
    CircularImageView userAvatar;
    RestMethods restMethods;
    List<Integer> arr=new ArrayList<>();
    User userObj;
    AppConstants globalClass;
    TextView userName,userEmail;
    CallbackUtils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Builds HTTP Client for API Calls
        restMethods = RestClient.buildHTTPClient();
        globalClass=(AppConstants)getApplicationContext();
        userObj=globalClass.getUserObj();
        utils=new CallbackUtils(getApplicationContext(),MainActivity.this);

        // Passing each menu ID as a set of Ids because each menu should be considered as top level destinations.
        mToolbar = (Toolbar) findViewById ( R.id.toolbar );
        setSupportActionBar ( mToolbar );
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerLayout = findViewById(R.id.navigation_layout);
        navigationView = findViewById ( R.id.navigation_view );

        // Top Nav
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_settings, R.id.nav_feedback,R.id.nav_profile,
                R.id.navigation_dashboard, R.id.navigation_search, R.id.navigation_scan,  R.id.navigation_cart)
                .setOpenableLayout(drawerLayout)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Bottom Nav
        bottomNavigation = findViewById(R.id.bottom_navigation);
        NavigationUI.setupWithNavController(bottomNavigation, navController);

        View navView = navigationView.getHeaderView(0);
//        View navView = navigationView.inflateHeaderView ( R.layout.include_drawer_header);
        userAvatar=navView.findViewById(R.id.userAvatar);
        userName=navView.findViewById(R.id.userName);
        userEmail=navView.findViewById(R.id.userEmail);

        if(globalClass.getUserProfilePic()==null){
            utils.setBitmapFromURL(AppConstants.BACKEND_URL.concat(userObj.getPicturePath()));
        }else{
            userAvatar.setImageBitmap(globalClass.getUserProfilePic());
        }
        userName.setText(userObj.getFirstName().concat(" ").concat(userObj.getLastName()));
        userEmail.setText(userObj.getEmail());

        //Handle visibility of the application bottom navigation
        arr.addAll(Arrays.asList(R.id.nav_feedback,R.id.nav_settings,R.id.nav_profile));
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

//        int bottomInt= 1;
//        if(bottomInt==0){
//            navigationView.setCheckedItem(R.id.navigation_dashboard);
//            navigationView.getMenu().performIdentifierAction(R.id.navigation_dashboard, 0);
//            bottomNavigation.setSelectedItemId(R.id.navigation_scan);
//        }else if(bottomInt==1){
//            navigationView.setCheckedItem(R.id.nav_profile);
//            navigationView.getMenu().performIdentifierAction(R.id.nav_profile, 0);
//        }
    }



    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void callbackMethod(Bitmap output) {
        Log.e("MainActivity","Callback from utils");
        userAvatar.setImageBitmap(output);
    }
}
