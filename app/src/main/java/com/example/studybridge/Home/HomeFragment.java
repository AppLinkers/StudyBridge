package com.example.studybridge.Home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.studybridge.R;

public class HomeFragment extends Fragment {

    TextView userNameTv;
    String userName;
    String userId;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        userNameTv = view.findViewById(R.id.home_name);

        Bundle bundle = getArguments();
        userId = bundle.getString("id");
        userName = bundle.getString("name");
        userNameTv.setText(userName);


        return view;
    }
}
