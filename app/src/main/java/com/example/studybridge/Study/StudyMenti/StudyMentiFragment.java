package com.example.studybridge.Study.StudyMenti;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.studybridge.R;
import com.example.studybridge.Study.StudyAddActivity;
import com.example.studybridge.Study.StudyFilter;
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.study.StudyFindRes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;


public class StudyMentiFragment extends Fragment{

    //리사이클러
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private SwipeRefreshLayout refreshLayout;
    private StudyMentiAdapter adapter;
    private FloatingActionButton mentiFab,filterFab;
    private TextView subjectFilter, placeFilter;

    private StudyFilter filter;

    private DataService dataService = new DataService();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.study_menti_fragment,container,false);


        //study add btn
        mentiFab = (FloatingActionButton) view.findViewById(R.id.menti_addBtn);
        mentiFab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), StudyAddActivity.class);
                startActivity(intent);
            }
        });

        //필터 버튼 & 텍스트
        filterFab = (FloatingActionButton) view.findViewById(R.id.menti_filterBtn);
        subjectFilter = (TextView) view.findViewById(R.id.menti_subjectFilter);
        placeFilter = (TextView) view.findViewById(R.id.menti_placeFilter);

        //recycler
        recyclerView = (RecyclerView) view.findViewById(R.id.study_menti_RCView);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.study_menti_swipeRC);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);



        setRecyclerView();


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
                bottomSheet.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        adapter = new StudyMentiAdapter();
                        filter = new StudyFilter("전체",subjectFilter.getText().toString(),placeFilter.getText().toString());
                        getData();
                        adapter.notifyDataSetChanged();
                        recyclerView.setAdapter(adapter);
                    }
                });

            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.postDelayed(new Runnable() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        recyclerView.setAdapter(adapter);
                        refreshLayout.setRefreshing(false);
                    }
                },500);

            }
        });
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimaryDark));



        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.clearItem();
        filter = new StudyFilter("전체",subjectFilter.getText().toString(),placeFilter.getText().toString());
        getData();
        recyclerView.setAdapter(adapter);
    }

    private void setRecyclerView(){

        adapter = new StudyMentiAdapter();
        filter = new StudyFilter("전체",subjectFilter.getText().toString(),placeFilter.getText().toString());
        getData();
        recyclerView.setAdapter(adapter);
    }


    @SuppressLint({"StaticFieldLeak", "NewApi"})
    private void getData() {
        AsyncTask<Void, Void, List<StudyFindRes>> listAPI = new AsyncTask<Void, Void, List<StudyFindRes>>() {
            @Override
            protected List<StudyFindRes> doInBackground(Void... params) {
                Call<List<StudyFindRes>> call = dataService.study.find();
                try {
                    return call.execute().body();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<StudyFindRes> s) {
                super.onPostExecute(s);
            }
        }.execute();


        List<StudyFindRes> result = null;

        try {
            result = listAPI.get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (result != null) {

                result.forEach(s -> {

                    adapter.addItem(s,filter);

                });
        }

    }

}
