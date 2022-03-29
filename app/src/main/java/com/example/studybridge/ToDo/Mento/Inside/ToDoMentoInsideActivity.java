package com.example.studybridge.ToDo.Mento.Inside;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ToDoMentoInsideActivity extends AppCompatActivity {

    private TextView year,month,day;
    private TextView studyName;

    //리사이클러뷰
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;

    private FloatingActionButton todoAdd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_mento_inside_activity);

        //화면 위 데이터
        year = (TextView) findViewById(R.id.todo_year_tv);
        month = (TextView) findViewById(R.id.todo_month_tv);
        day = (TextView) findViewById(R.id.todo_day_tv);
        studyName = (TextView) findViewById(R.id.todo_mentor_inside_studyName);

        recyclerView = (RecyclerView) findViewById(R.id.todo_mentor_inside_RV);
        todoAdd = (FloatingActionButton) findViewById(R.id.todo_addBtn);



        setTime();
        setRecyclerView();
        setFloatingActionButton();
    }

    @SuppressLint("SimpleDateFormat")
    private void setTime() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        year.setText(new SimpleDateFormat("yyyy").format(date));
        month.setText(new SimpleDateFormat("MMM", Locale.ENGLISH).format(date));
        day.setText(new SimpleDateFormat("dd").format(date));
    }

    private void setRecyclerView() {

        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new ToDoMentoInsideAdapter());
    }
    private void setFloatingActionButton(){
        todoAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ToDoMentoAddActivity.class);
                startActivity(intent);
            }
        });
    }
}
