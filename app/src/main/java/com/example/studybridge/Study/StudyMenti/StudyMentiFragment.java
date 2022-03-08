package com.example.studybridge.Study.StudyMenti;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.loader.content.AsyncTaskLoader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.studybridge.R;
import com.example.studybridge.Study.StudyMenti.Detail.DialogInterfaces;
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.StudyFindRes;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class StudyMentiFragment extends Fragment{
    //리사이클러
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private StudyMentiAdapter adapter;
    private FloatingActionButton mentiFab,filterFab;
    private TextView subjectFilter, placeFilter;



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
                ActivityOptions options = ActivityOptions
                        .makeSceneTransitionAnimation(getActivity(), mentiFab, "transition_fab");
                startActivity(intent,options.toBundle());
            }
        });

        //필터 버튼 & 텍스트
        filterFab = (FloatingActionButton) view.findViewById(R.id.menti_filterBtn);
        subjectFilter = (TextView) view.findViewById(R.id.menti_subjectFilter);
        placeFilter = (TextView) view.findViewById(R.id.menti_placeFilter);




        //recycler
        recyclerView = (RecyclerView) view.findViewById(R.id.study_menti_RCView);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.study_menti_swipeRC);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new StudyMentiAdapter();
        getData();
        recyclerView.setAdapter(adapter);

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

//                Snackbar.make(getContext(),"새로고침 되었습니다",Snackbar.LENGTH_SHORT).show();
            }
        });
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimaryDark));



        return view;
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

            if(subjectFilter.getText().equals("전체")&&placeFilter.getText().equals("전체")) {

                result.forEach(s -> {

                    String status = s.getStatus();

                    StudyMenti studyMenti = new StudyMenti(s.getId(), statusVal(status), s.getType(), s.getPlace(), s.getName(), s.getInfo(), s.getMaxNum());
                    Log.d("test", studyMenti.toString());
                    adapter.addItem(studyMenti);


                });
            }

            else if(!subjectFilter.getText().toString().equals("전체")&&placeFilter.getText().toString().equals("전체")){

                result.stream().filter(s -> s.getType().equals(subjectFilter.getText())).forEach(s -> {
                    String status = s.getStatus();

                    StudyMenti studyMenti = new StudyMenti(s.getId(), statusVal(status), s.getType(), s.getPlace(), s.getName(), s.getInfo(), s.getMaxNum());
                    Log.d("test", studyMenti.toString());
                    adapter.addItem(studyMenti);
                });
            }
            else if(subjectFilter.getText().toString().equals("전체")&&!placeFilter.getText().toString().equals("전체")){

                result.stream().filter(s -> s.getType().equals(placeFilter.getText())).forEach(s -> {
                    String status = s.getStatus();

                    StudyMenti studyMenti = new StudyMenti(s.getId(), statusVal(status), s.getType(), s.getPlace(), s.getName(), s.getInfo(), s.getMaxNum());
                    Log.d("test", studyMenti.toString());
                    adapter.addItem(studyMenti);
                });
            }
            else {

                result.stream().filter(s -> s.getType().equals(placeFilter.getText())).filter(s -> s.getType().equals(subjectFilter.getText())).forEach(s -> {
                    String status = s.getStatus();

                    StudyMenti studyMenti = new StudyMenti(s.getId(), statusVal(status), s.getType(), s.getPlace(), s.getName(), s.getInfo(), s.getMaxNum());
                    Log.d("test", studyMenti.toString());
                    adapter.addItem(studyMenti);
                });
            }





        }


    }




    public int statusVal(String s){
        if (s.equals("APPLY")) {
            return  0;
        } else if (s.equals("WAIT")) {
            return  1;
        } else if (s.equals("MATCHED")) {
            return 2;
        } else {
            return  3;
        }
    }


}
