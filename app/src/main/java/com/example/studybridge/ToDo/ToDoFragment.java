package com.example.studybridge.ToDo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.MainActivity;
import com.example.studybridge.R;
import com.example.studybridge.ToDo.Menti.FilterSpinner;
import com.example.studybridge.ToDo.Menti.ToDoMentiAdapter;
import com.example.studybridge.ToDo.Mento.ToDoMentoAdapter;
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.study.StudyFindRes;
import com.example.studybridge.http.dto.toDo.ToDoStatus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ToDoFragment extends Fragment {

    private int resource = 0;
    private int totalTask= 0;
    private int confirmedTask =0;
    private double confPerc=0.0;

    private TextView year, month, day, taskCount, taskPerc;
    private Spinner filterSpinner;
    //리사이클러뷰
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    private String userId;
    private boolean isMentee;
    Long filter;
    Long key;

    private DataService dataService;
    SharedPreferences sharedPreferences;
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_PK_ID_KEY = "user_pk_id_key";
    Long userIdPk;
    ToDoMentoAdapter toDoMentoAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view;
        dataService = new DataService();

        Bundle bundle = getArguments();
        userId = bundle.getString("id");
        isMentee = bundle.getBoolean("isMentee");
        filter = bundle.getLong("studyId");
        sharedPreferences = getContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userIdPk= sharedPreferences.getLong(USER_PK_ID_KEY, 0);

        if (isMentee == true) {
            resource = R.layout.todo_menti_fragment;
            view = inflater.inflate(resource, container, false);
            setMenteeUI(view);
        } else {
            resource = R.layout.todo_mentor_fragment;
            view = inflater.inflate(resource, container, false);
            setMentorUI(view);
        }

        ((MainActivity)getActivity()).navigationBlink(R.id.navigation_toDo);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(isMentee){
            setSpinnerData();
        }

    }

    private void setMenteeUI(View view) {
        //멘티 화면 위 데이터
        year = (TextView) view.findViewById(R.id.todo_year_tv);
        month = (TextView) view.findViewById(R.id.todo_month_tv);
        day = (TextView) view.findViewById(R.id.todo_day_tv);
        taskCount = (TextView) view.findViewById(R.id.todo_taskCount);
        taskPerc = (TextView) view.findViewById(R.id.toDo_perc) ;
        filterSpinner = (Spinner) view.findViewById(R.id.todo_filter);
        recyclerView = (RecyclerView) view.findViewById(R.id.todo_menti_RV);
        setTime();
        setSpinnerData();
    }

    private void setMentorUI(View view){
        //멘토 화면 위 데이터
        year = (TextView) view.findViewById(R.id.todo_year_tv);
        month = (TextView) view.findViewById(R.id.todo_month_tv);
        day = (TextView) view.findViewById(R.id.todo_day_tv);
        recyclerView = (RecyclerView) view.findViewById(R.id.todo_mentor_RV);

        setTime();
        setMentorRecyclerView();
    }

    private void setSpinnerData(){
        dataService.study.findByUserId(userIdPk).enqueue(new Callback<List<StudyFindRes>>() {
            @Override
            public void onResponse(Call<List<StudyFindRes>> call, Response<List<StudyFindRes>> response) {
                if(response.isSuccessful()){
                    Long a = Long.valueOf(0);
                    ArrayList<FilterSpinner> filtedList = new ArrayList<FilterSpinner>();
                    filtedList.add(new FilterSpinner(a,"전체"));
                    for(StudyFindRes data : response.body()){
                        if(data.getStatus().equals("MATCHED")){
                            filtedList.add(new FilterSpinner(data.getId(),data.getName()));
                        }
                    }
                    setSpinner(filtedList);
                }
            }

            @Override
            public void onFailure(Call<List<StudyFindRes>> call, Throwable t) {

            }
        });
    }

    int pos = 1;
    private void setSpinner(ArrayList<FilterSpinner> filtList){
        ArrayAdapter<FilterSpinner> categoryAdapter = new ArrayAdapter<FilterSpinner>(getContext(), android.R.layout.simple_spinner_item , filtList);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        for(int j=0; j<filtList.size();j++){
            if(filtList.get(j).getKey()==filter){
                pos = j;
            }
        }
        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(pos !=0){
                    filterSpinner.setSelection(pos);
                }

                FilterSpinner spinnerItem  = (FilterSpinner)adapterView.getItemAtPosition(i);
                key = spinnerItem.getKey();
                if(isMentee==true){
                    //Todolist 갯수확인
                    //setTaskCount();
                    setEachStudyCount(spinnerItem.getKey());
                    //리사이클러뷰 설정
                    setMenteeRecyclerView(spinnerItem.getKey());

                } else {}
                pos=0;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        filterSpinner.setAdapter(categoryAdapter);

    }

    private void setTaskCount(){
        dataService.assignedToDo.countByMentee(userIdPk).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.isSuccessful()){
                    totalTask = response.body();
                    setConfirmedCount(totalTask);
                }
            }
            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });
    }


    private void setEachStudyCount(Long filt){
        Long a = Long.valueOf(0);
        if(filt == a){
            setTaskCount();
        }else{
            dataService.assignedToDo.countByMenteeAndStudy(userIdPk,filt).enqueue(new Callback<Map<String, Integer>>() {
                @Override
                public void onResponse(Call<Map<String, Integer>> call, Response<Map<String, Integer>> response) {
                    if(response.isSuccessful()){
                        int total = response.body().get("total");
                        int confirmed = response.body().get("confirmed");
                        taskCount.setText(total-confirmed+"");
                        taskPerc.setText(((double)confirmed/(double)total) * 100 +"%");
                    }
                }

                @Override
                public void onFailure(Call<Map<String, Integer>> call, Throwable t) {

                }
            });
        }

    }

    private void setConfirmedCount(int totalTask){

        dataService.assignedToDo.countByMenteeAndStatus(userIdPk, ToDoStatus.CONFIRMED).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.isSuccessful()){
                    confirmedTask = response.body();
                    confPerc = ((double)confirmedTask / (double)totalTask ) * 100;
                    taskPerc.setText(String.format("%.2f", confPerc)+"%");
                    taskCount.setText(totalTask-confirmedTask+"");
                }
            }
            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });
    }


    @SuppressLint("SimpleDateFormat")
    private void setTime() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        year.setText(new SimpleDateFormat("yyyy").format(date));
        month.setText(new SimpleDateFormat("MMM", Locale.ENGLISH).format(date));
        day.setText(new SimpleDateFormat("dd").format(date));
    }

    private void setMenteeRecyclerView(Long filter) {
        ToDoMentiAdapter adapter = new ToDoMentiAdapter();
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter.setFilter(filter);
        recyclerView.setAdapter(adapter);
    }





    private void setMentorRecyclerView() {
        toDoMentoAdapter = new ToDoMentoAdapter();
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        dataService.study.findByUserId(userIdPk).enqueue(new Callback<List<StudyFindRes>>() {
            @Override
            public void onResponse(Call<List<StudyFindRes>> call, Response<List<StudyFindRes>> response) {
                if(response.isSuccessful()){
                    for(StudyFindRes study : response.body()){
                        toDoMentoAdapter.addItem(study);
                    }
                    recyclerView.setAdapter(toDoMentoAdapter);
                }

            }

            @Override
            public void onFailure(Call<List<StudyFindRes>> call, Throwable t) {

            }
        });



    }

}

