package com.dominicsilveira.one_q_shop.ui.dashboard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.dominicsilveira.one_q_shop.R;

import com.dominicsilveira.one_q_shop.ui.MainActivity;
import com.dominicsilveira.one_q_shop.ui.RegisterLogin.LoginActivity;
import com.dominicsilveira.one_q_shop.ui.RegisterLogin.SplashScreen;
import com.dominicsilveira.one_q_shop.ui.product.ProductCategoriesActivity;
import com.dominicsilveira.one_q_shop.utils.AppConstants;
import com.dominicsilveira.oneqshoprestapi.api_calls.ApiListener;
import com.dominicsilveira.oneqshoprestapi.api_calls.ApiResponse;
import com.dominicsilveira.oneqshoprestapi.pojo_classes.User.User;
import com.dominicsilveira.oneqshoprestapi.rest_api.RestApiClient;
import com.dominicsilveira.oneqshoprestapi.rest_api.RestApiMethods;
import com.dominicsilveira.oneqshoprestapi.pojo_classes.ErrorMessage;
import com.dominicsilveira.oneqshoprestapi.pojo_classes.Product.CategoriesDetails;
import com.dominicsilveira.oneqshoprestapi.pojo_classes.Product.CategoriesListDetails;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class DashboardFragment extends Fragment implements ApiListener {
    private static String TAG = DashboardFragment.class.getSimpleName();
    FloatingActionButton allCategoriesBtn;
    LinearLayout categoryListView;
    RestApiMethods restMethods;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
//        TextView textView = root.findViewById(R.id.text);
//        textView.setText("Dashboard");
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("36");

        initComponents(root);
        attachListeners(root);
        return root;
    }

    private void initComponents(View root) {
        //Builds HTTP Client for API Calls
        restMethods = RestApiClient.buildHTTPClient();

        allCategoriesBtn=root.findViewById(R.id.allCategoriesBtn);
        categoryListView=root.findViewById(R.id.categoryListView);
    }

    private void attachListeners(View root) {
        allCategoriesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ProductCategoriesActivity.class));
            }
        });

        Map<String, String> data = new HashMap<>();
        Call<CategoriesListDetails> req = restMethods.getCategoriesListDetails(data);
        ApiResponse.callRetrofitApi(req, RestApiMethods.getCategoriesListDetailsRequest, this);
    }

    @Override
    public void onApiResponse(String strApiName, int status, Object data, String error) {
        if (strApiName.equals(RestApiMethods.getCategoriesListDetailsRequest)) {
            if(data!=null){
                CategoriesListDetails categoriesListDetails = (CategoriesListDetails) data;
                List<CategoriesDetails> categoriesDetailsList=categoriesListDetails.getResults();
                for (final CategoriesDetails temp : categoriesDetailsList) {
                    View categoryView = getLayoutInflater().inflate(R.layout.include_category_btn, null);
                    FloatingActionButton categoryBtn=categoryView.findViewById(R.id.categoryBtn);
                    Log.i(TAG,AppConstants.BACKEND_URL.concat(temp.getImage())+" "+temp.getId());
                    Picasso.get().load(temp.getImage()).into(categoryBtn);
                    categoryBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent=new Intent(getActivity(),ProductCategoriesActivity.class);
                            intent.putExtra("CATEGORY_ID",temp.getId());
                            intent.putExtra("CATEGORY_NAME",temp.getName());
                            startActivity(intent);
                        }
                    });
                    categoryListView.addView(categoryView);
                }
            }else{
                Toast.makeText(getActivity(), "Error "+error, Toast.LENGTH_SHORT).show();
            }
        }
    }
}