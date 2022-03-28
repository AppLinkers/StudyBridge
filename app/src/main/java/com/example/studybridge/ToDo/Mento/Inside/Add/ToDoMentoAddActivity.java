package com.example.studybridge.ToDo.Mento.Inside.Add;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
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

public class ToDoMentoAddActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Calendar calendar = Calendar.getInstance();
    private MaterialCardView editDate;
    private TextView dueDate;
    private LinearLayout addBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_add_activity);

        toolbar = (Toolbar) findViewById(R.id.todo_add_toolbar);
        editDate = (MaterialCardView) findViewById(R.id.todo_add_editDate);
        dueDate = (TextView) findViewById(R.id.todo_add_dueDate);
        addBtn = (LinearLayout) findViewById(R.id.todo_add_btn);

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
                        ,calendar.get(Calendar.YEAR)
                        ,calendar.get(Calendar.MONTH)
                        ,calendar.get(Calendar.DAY_OF_MONTH)
                        );
                datePickerDialog.show();
                datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#FBB8AC"));

            }


        });
    }
    //추가하기 버튼설정
    private void setButton(){
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //클릭시 함수 작성
                Toast.makeText(ToDoMentoAddActivity.this, "추가되었습니다", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
