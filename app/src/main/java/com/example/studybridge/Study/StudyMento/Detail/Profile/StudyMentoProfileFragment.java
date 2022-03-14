package com.example.studybridge.Study.StudyMento.Detail.Profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.Mypage.MentoProfile.MyPageMentoProfile;
import com.example.studybridge.R;
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.Certificate;
import com.example.studybridge.http.dto.ProfileRes;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudyMentoProfileFragment extends Fragment {

    private TextView school,intro,place,subject,appeal,nullmsg;

    private RecyclerView recyclerView;
    private StudyMentoProfileCertiAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<Certificate> arrayList;
    private Certificate certificate;

    DataService dataService = new DataService();


    @Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable  Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.study_mento_detail_profile_fragment,container,false);

        intro = (TextView) view.findViewById(R.id.mento_profile_intro);
        school = (TextView) view.findViewById(R.id.mento_profile_school);
        place = (TextView) view.findViewById(R.id.mento_profile_place);
        subject = (TextView) view.findViewById(R.id.mento_profile_subject);
        appeal = (TextView) view.findViewById(R.id.mento_profile_appeal);
        nullmsg = (TextView) view.findViewById(R.id.mento_profile_noCerti_msg);



        final StringBuilder[] sb = new StringBuilder[2];
        Bundle extras = getArguments();

        MyPageMentoProfile profile = (MyPageMentoProfile) extras.getSerializable("profile");

        //리사이클러뷰
        recyclerView = (RecyclerView) view.findViewById(R.id.mento_profile_certi_RV);
        arrayList = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);


        final String mentoId = extras.getString("mentoId");


        if(mentoId == null || mentoId.equals("")) {
            //경로1 : 멘토 찾기에서 불러오는 것
            if(extras != null){

                sb[0] = new StringBuilder();
                String introStr = sb[0].append("\"").append(profile.getIntro()).append("\"").toString();
                intro.setText(introStr);
                school.setText(profile.getSchool());
                place.setText(profile.getPlace());
                subject.setText(profile.getSubject());
                appeal.setText(profile.getAppeal());
                if(profile.getCertificates().size()>0){
                    for(int i=0; i<profile.getCertificates().size(); i++){
                        certificate = new Certificate(profile.getCertificates().get(i).getCertificate(),profile.getCertificates().get(i).getImgUrl());
                        arrayList.add(certificate);
                    }
                } else {
                    nullmsg.setVisibility(View.VISIBLE);
                }



            }
        }
        else {
            dataService.userMentor.getProfile(mentoId).enqueue(new Callback<ProfileRes>() {
                @Override
                public void onResponse(Call<ProfileRes> call, Response<ProfileRes> response) {
                    if (response.isSuccessful())
                    {
                        sb[1] = new StringBuilder();
                        String introStr = sb[1].append("\"").append(response.body().getInfo()).append("\"").toString();
                        intro.setText(introStr);
                        school.setText(response.body().getSchool());
                        place.setText(response.body().getLocation());
                        subject.setText(response.body().getSubject());
                        appeal.setText(response.body().getAppeal());

                    }
                }

                @Override
                public void onFailure(Call<ProfileRes> call, Throwable t) {

                }
            });
        }


        adapter = new StudyMentoProfileCertiAdapter(arrayList);
        recyclerView.setAdapter(adapter);




        return view;
    }


}
