package com.example.studybridge.ToDo.Mento.Inside;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.R;
import com.example.studybridge.ToDo.Menti.Inside.ToDoMentiInsideAdapter;
import com.example.studybridge.ToDo.Mento.Inside.Add.ToDoMentoAddActivity;
import com.example.studybridge.ToDo.Mento.ToDoMentoAdapter;
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.study.StudyFindRes;
import com.example.studybridge.http.dto.toDo.FindToDoRes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ToDoMentoInsideActivity extends AppCompatActivity {


    private TextView year,month,day,studyTitle;
    private DataService dataService;

    //리사이클러뷰
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    private ToDoMentoInsideAdapter adapter;

    private FloatingActionButton todoAdd;

    private StudyFindRes study;

    //sharedPref
    SharedPreferences sharedPreferences;
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_PK_ID_KEY = "user_pk_id_key";
    private Long userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_mento_inside_activity);

        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userId= sharedPreferences.getLong(USER_PK_ID_KEY, 0);

        Intent intent = getIntent();
        study = (StudyFindRes) intent.getSerializableExtra("study");


        year = (TextView) findViewById(R.id.todo_year_tv);
        month = (TextView) findViewById(R.id.todo_month_tv);
        day = (TextView) findViewById(R.id.todo_day_tv);


        recyclerView = (RecyclerView) findViewById(R.id.todo_mentor_inside_RV);
        todoAdd = (FloatingActionButton) findViewById(R.id.todo_addBtn);
        studyTitle = (TextView) findViewById(R.id.toDo_studyTitle);

        setHeader();
        setRecyclerView();
        setFloatingActionButton();
    }

    @SuppressLint("SimpleDateFormat")
    private void setHeader() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        studyTitle.setText(study.getName());
        year.setText(new SimpleDateFormat("yyyy").format(date));
        month.setText(new SimpleDateFormat("MMM", Locale.ENGLISH).format(date));
        day.setText(new SimpleDateFormat("dd").format(date));
    }

    private void setRecyclerView() {
        dataService = new DataService();
        adapter = new ToDoMentoInsideAdapter();
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        dataService.toDo.findOfStudy(study.getId(),userId).enqueue(new Callback<List<FindToDoRes>>() {
            @Override
            public void onResponse(Call<List<FindToDoRes>> call, Response<List<FindToDoRes>> response) {
                for(FindToDoRes todo : response.body()){
                    adapter.addItem(todo);
                }
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<FindToDoRes>> call, Throwable t) {

            }
        });

    }

    private void setFloatingActionButton(){
        todoAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ToDoMentoAddActivity.class);
                intent.putExtra("studyId", study.getId());
                intent.putExtra("mentorId", userId);
                startActivity(intent);
            }
        });
    }
}
