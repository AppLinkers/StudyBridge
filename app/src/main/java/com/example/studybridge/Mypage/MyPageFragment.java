package com.example.studybridge.Mypage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.studybridge.databinding.MypageFragmentBinding;


public class MyPageFragment extends Fragment {

    private MypageFragmentBinding binding;

    SharedPreferences sharedPreferences;
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_PK_ID_KEY = "user_pk_id_key";
    public static final String USER_ID_KEY = "user_id_key";
    public static final String USER_NAME = "user_name_key";
    public static final String USER_ISMENTEE = "user_mentee_key";

    String userName;
    boolean isMentee;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = MypageFragmentBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        //sharedPref
        sharedPreferences = getContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
/*        userIdPk= sharedPreferences.getLong(USER_PK_ID_KEY, 0);
        userId= sharedPreferences.getString(USER_ID_KEY, "사용자 아이디");*/
        userName = sharedPreferences.getString(USER_NAME, "사용자 이름");
        isMentee = sharedPreferences.getBoolean(USER_ISMENTEE,false);

        setUI();


        return view;
    }

    private void setUI(){
        binding.name.setText(userName);


        setBtns();
    }

    private void setBtns(){
        //프로필 수정
        binding.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),MyPageEditActivity.class);
                startActivity(intent);
            }
        });



    }
}
