package com.dominicsilveira.one_q_shop.ui.dashboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.dominicsilveira.one_q_shop.MainActivity;
import com.dominicsilveira.one_q_shop.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class DashboardFragment extends Fragment {
    FloatingActionButton allCategoriesBtn;
    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
//        TextView textView = root.findViewById(R.id.text);
//        textView.setText("Dashboard");
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("36");
        allCategoriesBtn=root.findViewById(R.id.allCategoriesBtn);
        allCategoriesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ProductCategoriesActivity.class));
            }
        });


        return root;
    }
}