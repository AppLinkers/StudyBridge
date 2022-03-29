package com.example.studybridge.ToDo.Mento.Inside.Add;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.studybridge.R;
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.toDo.AssignToDoReq;
import com.example.studybridge.http.dto.toDo.AssignToDoRes;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ToDoMentoAddActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Calendar calendar = Calendar.getInstance();
    private Calendar now = Calendar.getInstance();
    private MaterialCardView editDate;
    private TextView dueDate;
    private LinearLayout addBtn;
    private TextInputEditText taskName,explain;

    private DataService dataService = new DataService();

    private String localDateTime;
    Long study_id;
    Long mento_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_add_activity);
        Intent intent = getIntent();
        study_id = intent.getLongExtra("studyId",0);
        mento_id = intent.getLongExtra("mentorId",0);

        toolbar = (Toolbar) findViewById(R.id.todo_add_toolbar);
        editDate = (MaterialCardView) findViewById(R.id.todo_add_editDate);
        dueDate = (TextView) findViewById(R.id.todo_add_dueDate);
        addBtn = (LinearLayout) findViewById(R.id.todo_add_btn);
        taskName = (TextInputEditText) findViewById(R.id.todo_add_taskName);
        explain = (TextInputEditText) findViewById(R.id.todo_add_explain);

        //툴바 설정
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setDatePicker();

        setButton();



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    //데이트 피커
    private void setDatePicker(){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.KOREA);

        dueDate.setText(sdf.format(calendar.getTime()));

        DatePickerDialog.OnDateSetListener datePicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,day);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.KOREA);
                dueDate.setText(sdf.format(calendar.getTime()));
            }
        };

        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog datePickerDialog =
                new DatePickerDialog(
                        ToDoMentoAddActivity.this,datePicker
                        ,now.get(Calendar.YEAR)
                        ,now.get(Calendar.MONTH)
                        ,now.get(Calendar.DAY_OF_MONTH)
                        );

                datePickerDialog.show();
                datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#FBB8AC"));
                datePickerDialog.getDatePicker().setMinDate(now.getTime().getTime());


            }


        });
    }
    //추가하기 버튼설정
    private void setButton(){
        addBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

/*                String[] splitDate = dueDate.getText().toString().split("/"); // 현재 날짜*/
                //클릭시 함수 작성
                localDateTime = "2019-01-02T12:34:00";


                AssignToDoReq assignToDoReq = new AssignToDoReq(
                        study_id,
                        mento_id,
                        taskName.getText().toString()+"",
                        explain.getText().toString()+"",
                        localDateTime);



                dataService.toDo.assign(assignToDoReq).enqueue(new Callback<AssignToDoRes>() {
                    @Override
                    public void onResponse(Call<AssignToDoRes> call, Response<AssignToDoRes> response) {
                        if(response.isSuccessful()){
                            System.out.println("성공");
                            Toast.makeText(ToDoMentoAddActivity.this, "추가 완료", Toast.LENGTH_SHORT).show();
                            finish();
                        } else
                            System.out.println("실패");
                    }

                    @Override
                    public void onFailure(Call<AssignToDoRes> call, Throwable t) {

                    }
                });
            }
        });
    }
}
