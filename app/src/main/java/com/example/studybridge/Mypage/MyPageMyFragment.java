package com.example.studybridge.Mypage;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.studybridge.MainActivity;
import com.example.studybridge.R;
import com.example.studybridge.Subscribe.SubscribeActivity;

import org.jetbrains.annotations.NotNull;

public class MyPageMyFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mypage_my_fragment,container,false);

        LinearLayout goToSubscribe = (LinearLayout) view.findViewById(R.id.myPage_goToSubscibe);

        goToSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SubscribeActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}
