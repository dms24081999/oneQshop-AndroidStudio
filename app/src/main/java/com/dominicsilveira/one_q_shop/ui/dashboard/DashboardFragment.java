package com.dominicsilveira.one_q_shop.ui.dashboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.dominicsilveira.one_q_shop.MainActivity;
import com.dominicsilveira.one_q_shop.R;
import com.dominicsilveira.one_q_shop.jsonschema2pojo_classes.ErrorMessage;
import com.dominicsilveira.one_q_shop.jsonschema2pojo_classes.Product.CategoriesDetails;
import com.dominicsilveira.one_q_shop.jsonschema2pojo_classes.Product.CategoriesListDetails;
import com.dominicsilveira.one_q_shop.jsonschema2pojo_classes.Product.ProductListDetails;
import com.dominicsilveira.one_q_shop.utils.AppConstants;
import com.dominicsilveira.one_q_shop.utils.adapters.ProductListAdapter;
import com.dominicsilveira.one_q_shop.utils.api.RestClient;
import com.dominicsilveira.one_q_shop.utils.api.RestMethods;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment {
    FloatingActionButton allCategoriesBtn;
    LinearLayout categoryListView;
    RestMethods restMethods;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
//        TextView textView = root.findViewById(R.id.text);
//        textView.setText("Dashboard");
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("36");

        //Builds HTTP Client for API Calls
        restMethods = RestClient.buildHTTPClient();

        allCategoriesBtn=root.findViewById(R.id.allCategoriesBtn);
        categoryListView=root.findViewById(R.id.categoryListView);

        allCategoriesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ProductCategoriesActivity.class));
            }
        });

        Map<String, String> data = new HashMap<>();
        Call<CategoriesListDetails> req = restMethods.getCategoriesListDetails(data);
        req.enqueue(new Callback<CategoriesListDetails>() {
            @Override
            public void onResponse(Call<CategoriesListDetails> call, Response<CategoriesListDetails> response) {
                Toast.makeText(getActivity(), response.code() + " ", Toast.LENGTH_SHORT).show();
                if (response.isSuccessful()) {
                    Log.i(String.valueOf(getActivity().getComponentName().getClassName()), String.valueOf(response.code()));
                    List<CategoriesDetails> categoriesDetailsList=response.body().getResults();
                    for (final CategoriesDetails temp : categoriesDetailsList) {
                        View categoryView = getLayoutInflater().inflate(R.layout.include_category_btn, null);
                        FloatingActionButton categoryBtn=categoryView.findViewById(R.id.categoryBtn);
                        Log.i("DashboardFragment",AppConstants.BACKEND_URL.concat(temp.getImage())+" "+temp.getId());
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
                } else {
                    Toast.makeText(getActivity(), "Request failed!", Toast.LENGTH_SHORT).show();
                    Gson gson = new Gson();
                    ErrorMessage error=gson.fromJson(response.errorBody().charStream(),ErrorMessage.class);
                    Log.i(String.valueOf(getActivity().getComponentName().getClassName()), String.valueOf(error.getMessage()));
                }
            }
            @Override
            public void onFailure(Call<CategoriesListDetails> call, Throwable t) {
                Toast.makeText(getActivity(), "Request failed", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
        return root;
    }
}