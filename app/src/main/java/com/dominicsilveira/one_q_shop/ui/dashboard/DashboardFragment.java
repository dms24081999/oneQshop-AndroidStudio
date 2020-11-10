package com.dominicsilveira.one_q_shop.ui.dashboard;

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

import java.util.Objects;

public class DashboardFragment extends Fragment {
    Toolbar mToolbar;
    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
//        TextView textView = root.findViewById(R.id.text);
//        textView.setText("Dashboard");
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("36");
        return root;
    }
}