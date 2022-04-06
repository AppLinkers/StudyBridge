package com.example.studybridge.Study.StudyMento;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
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

import com.example.studybridge.R;
import com.example.studybridge.Study.StudyFilter;
import com.example.studybridge.Study.StudyFilterDialog;
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.userMentor.ProfileRes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;


public class StudyMentoFragment extends Fragment {


    //리사이클러
    private RecyclerView recyclerView;
    private StudyMentoAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private StudyFilter filter;

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
    public static final int MENTOFIND = 1;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.study_mento_fragment,container,false);
        dataService = new DataService();

        //filter
        filterFab = (FloatingActionButton) view.findViewById(R.id.mento_filterBtn);
        subjectFilter = (TextView) view.findViewById(R.id.mento_typeTv);
        placeFilter = (TextView) view.findViewById(R.id.mento_placeTv);

        //recycler
        recyclerView = (RecyclerView) view.findViewById(R.id.study_mento_RCView);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        //sharedPreference, 현재 이용자 아이디 불러옴
        sharedPreferences = view.getContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userId= sharedPreferences.getString(USER_ID_KEY, "사용자 아이디");

        setRecyclerView();
        setFilterFab();



        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.clearItem();
        getData();
        recyclerView.setAdapter(adapter);
    }

    private void setFilterFab(){
        filterFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StudyFilterDialog bottomSheet = StudyFilterDialog.getInstance(MENTOFIND);
                bottomSheet.show(getChildFragmentManager(), StudyFilterDialog.getInstance(MENTOFIND).getTag());
                bottomSheet.setDialogInterfacer(new StudyFilterDialog.DialogInterfacer() {
                    @Override
                    public void onFilterBtnClick(String status, String subject, String place) {
                        subjectFilter.setText(subject);
                        placeFilter.setText(place);
                    }
                });
                bottomSheet.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        adapter = new StudyMentoAdapter();
                        filter = new StudyFilter(
                                subjectFilter.getText().toString(),
                                placeFilter.getText().toString());
                        getData();
                        recyclerView.setAdapter(adapter);
                    }
                });
            }
        });
    }

    private void setRecyclerView(){
        adapter = new StudyMentoAdapter();
        filter = new StudyFilter(
                subjectFilter.getText().toString(),
                placeFilter.getText().toString());
        getData();
        recyclerView.setAdapter(adapter);
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

                                adapter.addItem(res,filter);
                            }
                        }
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
