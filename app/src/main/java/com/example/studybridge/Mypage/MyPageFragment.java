package com.example.studybridge.Mypage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.studybridge.R;
import com.example.studybridge.Util.SharedPrefKey;
import com.example.studybridge.databinding.MypageFragmentBinding;
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.study.StudyFindRes;
import com.example.studybridge.http.dto.userAuth.UserProfileRes;
import com.example.studybridge.http.dto.userMentee.LikeMentorRes;
import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyPageFragment extends Fragment {

    private MypageFragmentBinding binding;

    private DataService dataService;

    SharedPreferences sharedPreferences;
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_PK_ID_KEY = "user_pk_id_key";
    public static final String USER_ID_KEY = "user_id_key";
    public static final String USER_NAME = "user_name_key";
    public static final String USER_ISMENTEE = "user_mentee_key";

    Long userIdPk;
    String userId;
    String userName;
    String imgPath;
    boolean isMentee;

    public static final int APPLY_STUDY = 0;
    public static final int LIKE_MENTOR = 1;

    private NativeAd nativeAd;


    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = MypageFragmentBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        //sharedPref
        sharedPreferences = getContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userIdPk= sharedPreferences.getLong(USER_PK_ID_KEY, 0);
        userId= sharedPreferences.getString(USER_ID_KEY, "사용자 아이디");
        userName = sharedPreferences.getString(USER_NAME, "사용자 이름");
        isMentee = sharedPreferences.getBoolean(USER_ISMENTEE,false);
        imgPath = sharedPreferences.getString(SharedPrefKey.USER_PROFILE,"img");

        setUI();


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        imgPath = sharedPreferences.getString(SharedPrefKey.USER_PROFILE,"img");
        Glide.with(this).load(imgPath).into(binding.img);
        getData();
    }

    @Override
    public void onDestroyView() {
        if(nativeAd !=null){
            nativeAd.destroy();
        }
        super.onDestroyView();
        binding = null;
    }

    private void setUI(){
        binding.name.setText(userName);
        Glide.with(getContext()).load(imgPath).into(binding.img);
        getData();
        setBtns();

        setAd();
    }

    private void setAd() {
        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });

    }




    private void setBtns(){

        //프로필 수정
        binding.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),MyPageEditActivity.class);
                isFinish.launch(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });

        binding.applyStudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MyPageInfoActivity.class);
                intent.putExtra("path",APPLY_STUDY);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });

        if(isMentee){
            binding.likeMentor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), MyPageInfoActivity.class);
                    intent.putExtra("path",LIKE_MENTOR);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                }
            });
        }
        else{
            binding.likeMentor.setEnabled(false);
        }

    }

    private void getData(){
        dataService = new DataService();

        dataService.study.findByUserId(userIdPk).enqueue(new Callback<List<StudyFindRes>>() {
            @Override
            public void onResponse(Call<List<StudyFindRes>> call, Response<List<StudyFindRes>> response) {
                if(response.isSuccessful()){
                    final int num = response.body().size();
                    StringBuilder sb = new StringBuilder();
                    sb.append(num).append(" 개");
                    binding.studyNum.setText(sb.toString());
                }
            }

            @Override
            public void onFailure(Call<List<StudyFindRes>> call, Throwable t) {

            }
        });

        if(isMentee){
            dataService.userMentee.findLikedMentors(userIdPk).enqueue(new Callback<List<LikeMentorRes>>() {
                @Override
                public void onResponse(Call<List<LikeMentorRes>> call, Response<List<LikeMentorRes>> response) {
                    if(response.isSuccessful()){
                        final int num = response.body().size();
                        StringBuilder sb = new StringBuilder();
                        sb.append(num).append(" 명");
                        binding.mentorNum.setText(sb.toString());
                    }
                }

                @Override
                public void onFailure(Call<List<LikeMentorRes>> call, Throwable t) {

                }
            });
        }
        else{
            binding.mentorNum.setText("멘티 전용");
        }

    }
    ActivityResultLauncher<Intent> isFinish = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        getActivity().finish();
                    }
                }
            }
    );

}
