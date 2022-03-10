package com.example.studybridge.Mypage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.studybridge.Mypage.MentoProfile.MyPageMentoProfileEditActivity;
import com.example.studybridge.Mypage.MentoProfile.MyPageMentoProfileShowActivity;
import com.example.studybridge.R;
import com.example.studybridge.Subscribe.SubscribeActivity;
import com.example.studybridge.http.DataService;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPageMyFragment extends Fragment {

    private LinearLayout goToSubscribe, goToMentoProfile;

    private DataService dataService;
    // creating constant keys for shared preferences.
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_ID_KEY = "user_id_key";
    SharedPreferences sharedPreferences;
    String userId;


    @Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mypage_my_fragment,container,false);

        goToSubscribe = (LinearLayout) view.findViewById(R.id.myPage_goToSubscibe);
        goToMentoProfile = (LinearLayout) view.findViewById(R.id.myPage_goToMentoProfile);
        dataService = new DataService();

        sharedPreferences = view.getContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userId= sharedPreferences.getString(USER_ID_KEY, "사용자 아이디");

        dataService.userAuth.isMentee(userId).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.body()){

                }else{
                    goToMentoProfile.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });


        //정기권 결제 클릭
        goToSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SubscribeActivity.class);
                startActivity(intent);
            }
        });



        //멘토 프로필 클릭
        goToMentoProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MyPageMentoProfileShowActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}
