package com.example.studybridge.Study.StudyMento.Detail.Experience;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.studybridge.Mypage.MentoProfile.MyPageMentoProfile;
import com.example.studybridge.R;
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.ProfileRes;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudyMentoExperienceFragment extends Fragment {

    private TextView experience;

    DataService dataService = new DataService();

    @Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.study_mento_detail_experience_fragment,container,false);

        experience = (TextView) view.findViewById(R.id.mento_experience_tv);

        Bundle extras = getArguments();

        MyPageMentoProfile profile = (MyPageMentoProfile) extras.getSerializable("profile");
        final String mentoId = extras.getString("mentoId");


        if(mentoId == null || mentoId.equals("")) {
            if (extras != null) {
                experience.setText(profile.getExpeience());
            }
        }
        else {
            dataService.userMentor.getProfile(mentoId).enqueue(new Callback<ProfileRes>() {
                @Override
                public void onResponse(Call<ProfileRes> call, Response<ProfileRes> response) {
                    if (response.isSuccessful())
                    {
                        experience.setText(response.body().getExperience());
                    }
                }

                @Override
                public void onFailure(Call<ProfileRes> call, Throwable t) {

                }
            });
        }


        return view;
    }


}

