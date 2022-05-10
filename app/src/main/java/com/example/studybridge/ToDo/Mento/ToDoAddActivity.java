package com.example.studybridge.ToDo.Mento;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.studybridge.R;
import com.example.studybridge.Util.AddTextWatcher;
import com.example.studybridge.databinding.TodoAddActivityBinding;
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.toDo.AssignToDoReq;
import com.example.studybridge.http.dto.toDo.AssignToDoRes;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ToDoAddActivity extends AppCompatActivity {

    private TodoAddActivityBinding binding;

    private Calendar calendar = Calendar.getInstance();
    private Calendar now = Calendar.getInstance();

    private DataService dataService = new DataService();

    private String localDateTime;
    Long study_id;
    Long mento_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TodoAddActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        intentData();
        setUI();

    }

    private void intentData(){
        Intent intent = getIntent();
        study_id = intent.getLongExtra("studyId",0);
        mento_id = intent.getLongExtra("mentorId",0);
    }

    private void setUI(){
        //toolbar
        setSupportActionBar(binding.appBar);
        binding.appBar.setTitle("TODO 만들기");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        EditText[] editList = {binding.todoName,binding.todoExplain};
        AddTextWatcher textWatcher = new AddTextWatcher(getApplicationContext(),binding.addBtn,editList);
        for(EditText editText : editList){
            editText.addTextChangedListener(textWatcher);
        }

        setDatePicker(binding.dueDate);
        setButton();

    }

    //툴바 뒤로가기 설정
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //데이트 피커
    private void setDatePicker(TextView dueDate){

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

        dueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog datePickerDialog =
                new DatePickerDialog(
                        ToDoAddActivity.this,datePicker
                        ,now.get(Calendar.YEAR)
                        ,now.get(Calendar.MONTH)
                        ,now.get(Calendar.DAY_OF_MONTH)
                        );

                datePickerDialog.show();
                datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.palletRed));
                datePickerDialog.getDatePicker().setMinDate(now.getTime().getTime());


            }


        });
    }
    //추가하기 버튼설정
    private void setButton(){
        binding.addBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

                String[] splitDate = binding.dueDate.getText().toString().split("/"); // 현재 날짜
                LocalDateTime dateTime = LocalDateTime.of(
                        Integer.parseInt(splitDate[0]),
                        Integer.parseInt(splitDate[1]),
                        Integer.parseInt(splitDate[2]),0,0);
                localDateTime = dateTime+":00";

                AssignToDoReq assignToDoReq = new AssignToDoReq(
                        study_id,
                        mento_id,
                        binding.todoName.getText().toString().trim()+"",
                        binding.todoExplain.getText().toString().trim()+"",
                        localDateTime);


                dataService.toDo.assign(assignToDoReq).enqueue(new Callback<AssignToDoRes>() {
                    @Override
                    public void onResponse(Call<AssignToDoRes> call, Response<AssignToDoRes> response) {
                        if(response.isSuccessful()){
                            System.out.println("성공");
                            Toast.makeText(getApplicationContext(),"추가 완료!",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                    @Override
                    public void onFailure(Call<AssignToDoRes> call, Throwable t) {

                    }
                });
            }
        });
    }
}
