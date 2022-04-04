package com.example.studybridge.Study.StudyMento;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.Mypage.MentoProfile.MyPageMentoProfile;
import com.example.studybridge.R;
import com.example.studybridge.Study.StudyMenti.StudyMentiFilterDialog;
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.userMentor.ProfileRes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;


public class StudyMentoFragment extends Fragment {
    //리사이클러
    private RecyclerView recyclerView;
    private StudyMentoAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

    //화면 위 데이터
    private FloatingActionButton filterFab;
    private TextView subjectFilter, placeFilter;
    DataService dataService;

    //SharedPref
    SharedPreferences sharedPreferences;
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_ID_KEY = "user_id_key";
    public static final String USER_PK_ID_KEY = "user_pk_id_key";
    private String userId;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.study_mento_fragment,container,false);
        dataService = new DataService();

        //filter
        filterFab = (FloatingActionButton) view.findViewById(R.id.mento_filterBtn);
        subjectFilter = (TextView) view.findViewById(R.id.mento_subjectFilter);
        placeFilter = (TextView) view.findViewById(R.id.mento_placeFilter);
        recyclerView = (RecyclerView) view.findViewById(R.id.study_mento_RCView);

        //sharedPreference, 현재 이용자 아이디 불러옴
        sharedPreferences = view.getContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userId= sharedPreferences.getString(USER_ID_KEY, "사용자 아이디");

        setFilterFab();

        setRecyclerView();




        return view;
    }

    private void setFilterFab(){
        filterFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StudyMentiFilterDialog bottomSheet = StudyMentiFilterDialog.getInstance();
                bottomSheet.show(getChildFragmentManager(),StudyMentiFilterDialog.getInstance().getTag());
                bottomSheet.setDialogInterfacer(new StudyMentiFilterDialog.DialogInterfacer() {
                    @Override
                    public void onFilterBtnClick(String subject, String place) {
                        subjectFilter.setText(subject);
                        placeFilter.setText(place);
                    }
                });
            }
        });
    }

    private void setRecyclerView(){
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new StudyMentoAdapter();
        getData();
    }

    @SuppressLint({"StaticFieldLeak", "NewApi"})
    private void getData() {
        AsyncTask<Void, Void, List<ProfileRes>> listAPI = new AsyncTask<Void, Void, List<ProfileRes>>() {
            @Override
            protected List<ProfileRes> doInBackground(Void... params) {
                Call<List<ProfileRes>> call = dataService.userMentor.getAllProfile(userId);
                try {
                        for (ProfileRes res : call.execute().body()) {
                            if (res.getNickName() != null) {  //임시 조건
                                ProfileRes mentoProfile = new ProfileRes(
                                        res.getUserId(),
                                        res.getUserName(),
                                        res.getLocation(),
                                        res.getInfo(),
                                        res.getNickName(),
                                        res.getSchool(),
                                        res.getSchoolImg(),
                                        res.getSubject(),
                                        res.getCertificates(),
                                        res.getExperience(),
                                        res.getCurriculum(),
                                        res.getAppeal(),
                                        res.getLiked());
                                adapter.addItem(mentoProfile);
                            }
                        }
                    recyclerView.setAdapter(adapter);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<ProfileRes> s) {super.onPostExecute(s);}
        }.execute();


        List<ProfileRes> result = null;

        try {
            result = listAPI.get();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
