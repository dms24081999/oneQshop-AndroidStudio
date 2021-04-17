package com.dominicsilveira.one_q_shop.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import com.dominicsilveira.one_q_shop.R;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_cart:
                intent=new Intent(MainActivity.this, CartActivity.class);
                startActivity(intent);
                break;
            case R.id.action_search:
                intent=new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.action_settings:
                Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return true;
    }
}
