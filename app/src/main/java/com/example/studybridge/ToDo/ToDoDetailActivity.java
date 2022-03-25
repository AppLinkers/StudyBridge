package com.example.studybridge.ToDo;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.studybridge.R;
import com.google.android.material.card.MaterialCardView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ToDoDetailActivity extends AppCompatActivity {

    private TextView taskName,mentorId,menteeId,dueDate;
    private Spinner spinner;
    private MaterialCardView editDate;
    private Toolbar toolbar;
    private Calendar calendar = Calendar.getInstance();



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
        editDate = (MaterialCardView) findViewById(R.id.todo_detail_editDate);
        toolbar = (Toolbar) findViewById(R.id.todo_toolbar);

        //툴바 설정
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        setSpinner();

        setData();

        setDatePicker();



    }

    //툴바 설정
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.todo_toolbar_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            case R.id.todo_save:
                //저장 클릭시 실행
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //spinner
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

    //데이터
    private void setData(){
        Intent intent = getIntent();
        ToDo toDo = (ToDo) intent.getSerializableExtra("toDo");
        taskName.setText(toDo.getTaskName());
        dueDate.setText(toDo.getDueDate());
        spinner.setSelection(toDo.getStatus());
        mentorId.setText(toDo.getMentoId());
    }


    //데이트 피커
    private void setDatePicker(){

        String[] splitDate = dueDate.getText().toString().split("/"); // 현재 날짜

        DatePickerDialog.OnDateSetListener datePicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                calendar.set(year,month,day);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd",Locale.KOREA);

                dueDate.setText(sdf.format(calendar.getTime()));

            }
        };

        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                new DatePickerDialog(
                        ToDoDetailActivity.this,datePicker,
                        Integer.parseInt(splitDate[0]),
                        Integer.parseInt(splitDate[1])-1,
                        Integer.parseInt(splitDate[2])).show();

            }


        });
    }


}
