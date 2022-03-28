package com.example.studybridge.ToDo;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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

import com.example.studybridge.MainActivity;
import com.example.studybridge.R;
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.assignedToDo.ChangeToDoStatusReq;
import com.example.studybridge.http.dto.assignedToDo.ChangeToDoStatusRes;
import com.google.android.material.card.MaterialCardView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ToDoDetailActivity extends AppCompatActivity {

    private TextView taskName,mentorId,menteeId,dueDate,taskInfo;
    private Spinner spinner;
    private MaterialCardView editDate;
    private Toolbar toolbar;
    private Calendar calendar = Calendar.getInstance();
    private DataService dataService;
    ChangeToDoStatusReq changeToDoStatusReq;

    SharedPreferences sharedPreferences;
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_PK_ID_KEY = "user_pk_id_key";
    public static final String USER_ID_KEY = "user_id_key";
    Long userIdPk;
    long todoId;
    String userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_detail_activity);

        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userIdPk= sharedPreferences.getLong(USER_PK_ID_KEY, 0);
        userId = sharedPreferences.getString(USER_ID_KEY, "user");

        dataService = new DataService();
        //화면 위 데이터
        taskName = (TextView) findViewById(R.id.todo_detail_taskName);
        mentorId = (TextView) findViewById(R.id.todo_detail_mentorId);
        menteeId = (TextView) findViewById(R.id.todo_detail_menteeId);
        dueDate = (TextView) findViewById(R.id.todo_detail_dueDate);
        spinner = (Spinner) findViewById(R.id.todo_detail_spinner);
        taskInfo = (TextView) findViewById(R.id.todo_detail_taskInfo) ;

        editDate = (MaterialCardView) findViewById(R.id.todo_detail_editDate);
        toolbar = (Toolbar) findViewById(R.id.todo_toolbar);

        //툴바 설정
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        setData();

        dataService.userAuth.isMentee(userId).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.body()){
                    String[] array = getResources().getStringArray(R.array.todo_spinner);
                    String[] menteeArray = Arrays.copyOfRange(array,0,3);
                    setSpinner(menteeArray);
                }else{
                    //멘토 접근 수정 코드
                    String[] array = getResources().getStringArray(R.array.todo_spinner);
                    setSpinner(array);
                    setDatePicker();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });





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
                String statusReq = spinner.getSelectedItem().toString();
                changeToDoStatusReq = new ChangeToDoStatusReq(userIdPk, todoId, statusReq);
                dataService.assignedToDo.changeStatus(changeToDoStatusReq).enqueue(new Callback<ChangeToDoStatusRes>() {
                    @Override
                    public void onResponse(Call<ChangeToDoStatusRes> call, Response<ChangeToDoStatusRes> response) {
                        Toast.makeText(ToDoDetailActivity.this, "성공적으로 변경하였습니다 ", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<ChangeToDoStatusRes> call, Throwable t) {

                    }
                });
                Intent intent = new Intent(this.getApplicationContext(), MainActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //spinner
    private void setSpinner(String[] array){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,
                array);

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

        if(toDo.getStatus().equals("READY")){
            spinner.setSelection(0);
        }else if(toDo.getStatus().equals("PROGRESS")){
            spinner.setSelection(1);
        }else if(toDo.getStatus().equals("DONE")){
            spinner.setSelection(2);
        }else{
            spinner.setSelection(3);
        }

        StringBuilder date = new StringBuilder();
        date.append(toDo.getDueDate().substring(0,4))
                .append("/")
                .append(toDo.getDueDate().substring(5,7))
                .append("/")
                .append(toDo.getDueDate().substring(8,10));


        taskName.setText(toDo.getTaskName());
        dueDate.setText(date.toString());
        mentorId.setText(toDo.getMentoId());
        menteeId.setText(toDo.getMentiId());
        taskInfo.setText(toDo.getTaskInfo());
        todoId = toDo.getTodoId();

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


                DatePickerDialog datePickerDialog =
                new DatePickerDialog(
                        ToDoDetailActivity.this,datePicker,
                        Integer.parseInt(splitDate[0]),
                        Integer.parseInt(splitDate[1])-1,
                        Integer.parseInt(splitDate[2]));

                datePickerDialog.show();
                datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#FBB8AC"));
            }


        });
    }


}
