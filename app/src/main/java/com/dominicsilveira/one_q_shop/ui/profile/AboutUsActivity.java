package com.dominicsilveira.one_q_shop.ui.profile;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import com.dominicsilveira.one_q_shop.R;
import com.dominicsilveira.one_q_shop.ui.cart.CartActivity;
import com.dominicsilveira.one_q_shop.ui.cart.CartHistoryActivity;
import com.dominicsilveira.one_q_shop.utils.BasicUtils;

public class AboutUsActivity extends AppCompatActivity {
    static String TAG = AboutUsActivity.class.getSimpleName();
    LinearLayout dominicBtn,joelBtn,leninBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        initComponents();
        attachListeners();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);  //slide from left to right
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);  //slide from left to right
    }

    private void initComponents() {
        BasicUtils.setActionBar(AboutUsActivity.this,"About Me");
        dominicBtn=findViewById(R.id.dominicBtn);
        joelBtn=findViewById(R.id.joelBtn);
        leninBtn=findViewById(R.id.leninBtn);
    }

    private void attachListeners() {
        dominicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://www.linkedin.com/in/dms24081999/"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        joelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://www.linkedin.com/in/joel-monis-146b6016b/"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        leninBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://www.linkedin.com/in/lenin-bardeskar/"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }
}