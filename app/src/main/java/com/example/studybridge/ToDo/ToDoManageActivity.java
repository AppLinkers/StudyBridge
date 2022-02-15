package com.example.studybridge.ToDo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.studybridge.R;

public class ToDoManageActivity extends AppCompatActivity {



    private RadioGroup radioGroup;
    ToDoInside td;
    String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_manage);

        Intent intent = getIntent();
        td = (ToDoInside) intent.getSerializableExtra("manageData");

        radioGroup = (RadioGroup) findViewById(R.id.todo_radioGroup);
        final TextView cancel = (TextView) findViewById(R.id.todo_add_cancel);
        final TextView confirm = (TextView)findViewById(R.id.todo_add_confirm);

        RadioButton readyBtn = findViewById(R.id.todo_add_ready);
        RadioButton progressBtn = findViewById(R.id.todo_add_progress);
        RadioButton doneBtn = findViewById(R.id.todo_add_done);

        String title = td.getTitle().toString();
        String due = td.getDue().toString();
        String category = td.getCatagory().toString();
        String status = td.getStatus().toString();

        if(status.equals("Ready")){
            readyBtn.setChecked(true);
        }else if(status.equals("Progress")){
            progressBtn.setChecked(true);
        }else if(status.equals("Done")){
            progressBtn.setChecked(true);
        }


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.todo_add_ready:
                        break;
                    case R.id.todo_add_progress:
                        break;
                    case R.id.todo_add_done:
                        break;
                }
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RadioButton rb =  findViewById(radioGroup.getCheckedRadioButtonId());
                data = rb.getText()+"";
                td.setStatus(data);
                Intent i = new Intent(getApplicationContext(),ToDoDetailActivity.class);
                i.putExtra("newD", td);
                startActivity(i);
            }
        });
    }
}