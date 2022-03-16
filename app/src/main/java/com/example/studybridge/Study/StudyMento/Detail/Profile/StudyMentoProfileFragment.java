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

    //null값 처리
    public static final String VALUE_NULL_STR = "내용이 없습니다";


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

            sb[0] = new StringBuilder();
            String introStr = sb[0].append("\"").append(checkNull(profile.getIntro())).append("\"").toString();
            intro.setText(introStr);
            school.setText(checkNull(profile.getSchool()));
            place.setText(checkNull(profile.getPlace()));
            subject.setText(checkNull(profile.getSubject()));
            appeal.setText(checkNull(profile.getAppeal()));

            if(profile.getCertificates().size()>0){
                for(int i=0; i<profile.getCertificates().size(); i++){
                    certificate = new Certificate(profile.getCertificates().get(i).getCertificate(),profile.getCertificates().get(i).getImgUrl());
                    arrayList.add(certificate);
                }
            } else {
                nullmsg.setVisibility(View.VISIBLE);
            }
            adapter = new StudyMentoProfileCertiAdapter(arrayList);
            recyclerView.setAdapter(adapter);


        }
        else { //신청한 멘토에서 불러온 것
            dataService.userMentor.getProfile(mentoId).enqueue(new Callback<ProfileRes>() {
                @Override
                public void onResponse(Call<ProfileRes> call, Response<ProfileRes> response) {
                    if (response.isSuccessful())
                    {
                        sb[1] = new StringBuilder();
                        String introStr = sb[1].append("\"").append(checkNull(response.body().getInfo())).append("\"").toString();
                        intro.setText(introStr);
                        school.setText(checkNull(response.body().getSchool()));
                        place.setText(checkNull(response.body().getLocation()));
                        subject.setText(checkNull(response.body().getSubject()));
                        appeal.setText(checkNull(response.body().getAppeal()));

                        if(response.body().getCertificates().size()>0){
                            for(int i=0; i<response.body().getCertificates().size(); i++){
                                certificate = new Certificate(
                                        response.body().getCertificates().get(i).getCertificate(),
                                        response.body().getCertificates().get(i).getImgUrl());
                                arrayList.add(certificate);
                            }
                        } else {
                            nullmsg.setVisibility(View.VISIBLE);
                        }
                        adapter = new StudyMentoProfileCertiAdapter(arrayList);
                        recyclerView.setAdapter(adapter);

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
