package com.dominicsilveira.one_q_shop.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import com.dominicsilveira.one_q_shop.R;
import com.dominicsilveira.one_q_shop.ui.RegisterLogin.LoginActivity;
import com.dominicsilveira.one_q_shop.ui.cart.CartActivity;
import com.dominicsilveira.one_q_shop.ui.search.SearchActivity;
import com.dominicsilveira.oneqshoprestapi.rest_api.RestApiClient;
import com.dominicsilveira.oneqshoprestapi.rest_api.RestApiMethods;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity{
    static String TAG = MainActivity.class.getSimpleName();
    Toolbar mToolbar;
    RestApiMethods restMethods;
    BottomNavigationView navView;
//    int mCartItemCount = 3;
//    TextView textCartItemCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();
        initBottomNavigation();
        initPrevIntent();
    }

    private void initPrevIntent() {
        Intent mIntent = getIntent();
        int bottomInt= mIntent.getIntExtra("FRAGMENT_NO",0);
        if(bottomInt==0){
            navView.setSelectedItemId(R.id.navigation_home);
        }else if(bottomInt==1){
            navView.setSelectedItemId(R.id.navigation_scan);
        }else if(bottomInt==2){
            navView.setSelectedItemId(R.id.navigation_profile);
        }
    }

    private void initBottomNavigation() {
        // Passing each menu ID as a set of Ids because each menu should be considered as top level destinations.
        mToolbar = (Toolbar) findViewById ( R.id.toolbar );
        setSupportActionBar ( mToolbar );

        // Passing each menu ID as a set of Ids because each menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_scan, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    private void initComponents() {
        restMethods = RestApiClient.buildHTTPClient();//Builds HTTP Client for API Calls
        navView = findViewById(R.id.bottom_navigation);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_cart:
                intent=new Intent(MainActivity.this, CartActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.action_search:
                intent=new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.action_logout:
                SharedPreferences preferences = getSharedPreferences("TokenAuth", MODE_PRIVATE);
                preferences.edit().remove("token").apply();
                Toast.makeText(MainActivity.this, "Logout Success", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
//        final MenuItem menuItem = menu.findItem(R.id.action_cart);
//        View actionView = menuItem.getActionView();
//        textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);
//        setupBadge();
//        actionView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onOptionsItemSelected(menuItem);
//            }
//        });
        return true;
    }

//    private void setupBadge() {
//        if (textCartItemCount != null) {
//            if (mCartItemCount == 0) {
//                if (textCartItemCount.getVisibility() != View.GONE) {
//                    textCartItemCount.setVisibility(View.GONE);
//                }
//            } else {
//                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
//                if (textCartItemCount.getVisibility() != View.VISIBLE) {
//                    textCartItemCount.setVisibility(View.VISIBLE);
//                }
//            }
//        }
//    }
}
