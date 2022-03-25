package com.example.studybridge.ToDo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.studybridge.R;

public class ToDoDetailActivity extends AppCompatActivity {

    private TextView taskName,mentorId,menteeId,dueDate,taskInfo;
    private Spinner spinner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_detail_activity);

        //화면 위 데이터
        taskName = (TextView) findViewById(R.id.todo_detail_taskName);
        mentorId = (TextView) findViewById(R.id.todo_detail_mentorId);
        menteeId = (TextView) findViewById(R.id.todo_detail_menteeId);
        dueDate = (TextView) findViewById(R.id.todo_detail_dueDate);
        spinner = (Spinner) findViewById(R.id.todo_detail_spinner);
        taskInfo = (TextView) findViewById(R.id.todo_detail_taskInfo) ;

        setSpinner();

        setData();

    }

    private void setSpinner(){
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,R.array.todo_spinner,android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setData(){
        Intent intent = getIntent();
        ToDo toDo = (ToDo) intent.getSerializableExtra("toDo");

        if(toDo.getStatus().equals("READY")){
            spinner.setSelection(0);
        }else if(toDo.getStatus().equals("PORGRESS")){
            spinner.setSelection(1);
        }else if(toDo.getStatus().equals("DONE")){
            spinner.setSelection(2);
        }

        taskName.setText(toDo.getTaskName());
        dueDate.setText(toDo.getDueDate());
        mentorId.setText(toDo.getMentoId());
        menteeId.setText(toDo.getMentiId());
        taskInfo.setText(toDo.getTaskInfo());
    }

}
