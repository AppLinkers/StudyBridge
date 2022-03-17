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
import com.example.studybridge.http.dto.userMentor.ProfileRes;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudyMentoExperienceFragment extends Fragment {

    private TextView curi,experience;

    DataService dataService = new DataService();

    //null값 처리
    public static final String VALUE_NULL_STR = "내용이 없습니다";

    @Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.study_mento_detail_experience_fragment,container,false);

        curi = (TextView) view.findViewById(R.id.mento_curi_tv);
        experience = (TextView) view.findViewById(R.id.mento_experience_tv);

        Bundle extras = getArguments();

        MyPageMentoProfile profile = (MyPageMentoProfile) extras.getSerializable("profile");
        final String mentoId = extras.getString("mentoId");


        if(mentoId == null || mentoId.equals("")) {
            if (extras != null) {
                curi.setText(checkNull(profile.getCuri()));
                experience.setText(checkNull(profile.getExpeience()));
            }
        }
        else {
            dataService.userMentor.getProfile(mentoId).enqueue(new Callback<ProfileRes>() {
                @Override
                public void onResponse(Call<ProfileRes> call, Response<ProfileRes> response) {
                    if (response.isSuccessful())
                    {
                        curi.setText(checkNull(response.body().getCurriculum()));
                        experience.setText(checkNull(response.body().getExperience()));
                    }
                }

                @Override
                public void onFailure(Call<ProfileRes> call, Throwable t) {

                }
            });
        }


        return view;
    }

    public static String checkNull(String str){
        if(str==null){
            return VALUE_NULL_STR;
        }
        else
            return str;
    }


}

