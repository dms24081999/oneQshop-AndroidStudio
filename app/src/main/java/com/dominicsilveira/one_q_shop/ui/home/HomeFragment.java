package com.dominicsilveira.one_q_shop.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.dominicsilveira.one_q_shop.R;
import com.dominicsilveira.one_q_shop.utils.adapters.CategoryListAdapter;
import com.dominicsilveira.oneqshoprestapi.api_calls.ApiListener;
import com.dominicsilveira.oneqshoprestapi.api_calls.ApiResponse;
import com.dominicsilveira.oneqshoprestapi.rest_api.RestApiClient;
import com.dominicsilveira.oneqshoprestapi.rest_api.RestApiMethods;
import com.dominicsilveira.oneqshoprestapi.pojo_classes.Product.CategoriesListDetails;
import java.util.HashMap;
import java.util.Map;
import retrofit2.Call;

public class HomeFragment extends Fragment implements ApiListener {
    static String TAG = HomeFragment.class.getSimpleName();
    RestApiMethods restMethods;
    RecyclerView recyclerView;
    CategoryListAdapter mAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        initComponents(root);
        attachListeners(root);
        return root;
    }

    private void initComponents(View root) {
        restMethods = RestApiClient.buildHTTPClient();//Builds HTTP Client for API Calls
        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
    }

    private void attachListeners(View root) {
        Map<String, String> data = new HashMap<>();
        Call<CategoriesListDetails> req = restMethods.getCategoriesListDetails(data);
        ApiResponse.callRetrofitApi(req, RestApiMethods.getCategoriesListDetailsRequest, this);
    }

    @Override
    public void onApiResponse(String strApiName, int status, Object data, String error) {
        if (strApiName.equals(RestApiMethods.getCategoriesListDetailsRequest)) {
            if(data!=null){
                CategoriesListDetails categoriesListDetails = (CategoriesListDetails) data;
                mAdapter = new CategoryListAdapter(getActivity(), categoriesListDetails.getResults());  //set data and list adapter
                recyclerView.setAdapter(mAdapter);
            }else{
                Toast.makeText(getActivity(), "Error "+error, Toast.LENGTH_SHORT).show();
            }
        }
    }
}

