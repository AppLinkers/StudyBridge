package com.example.studybridge.Study.StudyMento;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.studybridge.R;
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.ProfileRes;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudyMentoProfileFragment extends Fragment {

    private TextView school,intro,place,subject;

    DataService dataService = new DataService();


    @Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable  Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.study_mento_detail_profile_fragment,container,false);

        intro = (TextView) view.findViewById(R.id.mento_profile_intro);
        school = (TextView) view.findViewById(R.id.mento_profile_school);
        place = (TextView) view.findViewById(R.id.mento_profile_place);
        subject = (TextView) view.findViewById(R.id.mento_profile_subject);


        StringBuilder sb;
        Bundle extras = getArguments();





        //경로1 : 멘토 찾기에서 불러오는 것
        if(extras != null){
            sb = new StringBuilder();
            String introStr = sb.append("\"").append(extras.getString("intro")).append("\"").toString();
            intro.setText(introStr);
            school.setText(extras.getString("school"));
            place.setText(extras.getString("place"));
            subject.setText(extras.getString("subject"));
        }

        //경로 2 : 스터디 신청한 멘토를 클릭한 경우
        final String mentoId = extras.getString("mentoId");
        dataService.userMentor.getProfile(mentoId).enqueue(new Callback<ProfileRes>() {
            @Override
            public void onResponse(Call<ProfileRes> call, Response<ProfileRes> response) {
                if (response.isSuccessful())
                {
                    intro.setText(response.body().getInfo());
                    school.setText(response.body().getSchool());
                    place.setText(response.body().getLocation());
                    subject.setText(response.body().getSubject());
                }
            }

            @Override
            public void onFailure(Call<ProfileRes> call, Throwable t) {

            }
        });








        return view;
    }


}
