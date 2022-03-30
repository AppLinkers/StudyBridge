package com.example.studybridge.ToDo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.R;
import com.example.studybridge.ToDo.Menti.ToDoMentiAdapter;
import com.example.studybridge.ToDo.Mento.ToDoMentoAdapter;
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.study.StudyFindRes;
import com.example.studybridge.http.dto.toDo.FindToDoRes;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ToDoFragment extends Fragment {

    private int resource = 0;

    private TextView year, month, day, taskCount;
    //리사이클러뷰
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    private String userId;
    private boolean isMentee;

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

        return view;
    }


    private void setMenteeUI(View view) {
        //멘티 화면 위 데이터
        year = (TextView) view.findViewById(R.id.todo_year_tv);
        month = (TextView) view.findViewById(R.id.todo_month_tv);
        day = (TextView) view.findViewById(R.id.todo_day_tv);
        taskCount = (TextView) view.findViewById(R.id.todo_taskCount);
        recyclerView = (RecyclerView) view.findViewById(R.id.todo_menti_RV);


        //날짜 설정
        setTime();
        //Todolist 갯수확인
        setTaskCount();
        //리사이클러뷰 설정
        setMenteeRecyclerView();
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

    private void setTaskCount(){
        dataService.assignedToDo.countOfMentee(userIdPk).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.isSuccessful()){
                    taskCount.setText(response.body()+"");
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

    private void setMenteeRecyclerView() {

        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new ToDoMentiAdapter());
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

