package com.example.studybridge.ToDo;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.MainActivity;
import com.example.studybridge.R;
import com.example.studybridge.ToDo.feedback.CommentAdapter;
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.assignedToDo.ChangeToDoStatusReq;
import com.example.studybridge.http.dto.assignedToDo.ChangeToDoStatusRes;
import com.example.studybridge.http.dto.assignedToDo.FindAssignedToDoRes;
import com.example.studybridge.http.dto.feedBack.FindFeedBackRes;
import com.example.studybridge.http.dto.feedBack.WriteFeedBackReq;
import com.example.studybridge.http.dto.feedBack.WriteFeedBackRes;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.HEAD;

public class ToDoDetailActivity extends AppCompatActivity {

    private TextView mentorId,menteeId,dueDate;
    private LinearLayoutManager linearLayoutManager;
    private TextInputEditText taskName,taskInfo,comment;
    private TextInputLayout commentLayout;
    private Spinner spinner;
    private MaterialCardView editDate;
    private Toolbar toolbar;
    private Calendar calendar = Calendar.getInstance();
    private Calendar now = Calendar.getInstance();
    private DataService dataService;
    ChangeToDoStatusReq changeToDoStatusReq;

    //comment recycler
    private RecyclerView commentRv;
    private CommentAdapter adapter;

    SharedPreferences sharedPreferences;
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_PK_ID_KEY = "user_pk_id_key";
    public static final String USER_ID_KEY = "user_id_key";
    Long userIdPk,menteePKId;
    long todoId;
    String userId;
    Intent gIntent;
    ToDo toDo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_detail_activity);

        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        userId = sharedPreferences.getString(USER_ID_KEY, "user");
        userIdPk= sharedPreferences.getLong(USER_PK_ID_KEY, 0);
        gIntent = getIntent();
        toDo = (ToDo) gIntent.getSerializableExtra("toDo");


        dataService = new DataService();
        //화면 위 데이터
        taskName = (TextInputEditText) findViewById(R.id.todo_detail_taskName);
        mentorId = (TextView) findViewById(R.id.todo_detail_mentorId);
        menteeId = (TextView) findViewById(R.id.todo_detail_menteeId);
        dueDate = (TextView) findViewById(R.id.todo_detail_dueDate);
        spinner = (Spinner) findViewById(R.id.todo_detail_spinner);
        taskInfo = (TextInputEditText) findViewById(R.id.todo_detail_taskInfo) ;
        comment = (TextInputEditText) findViewById(R.id.todo_comment);
        commentLayout = (TextInputLayout) findViewById(R.id.todo_detail_comment_layout);
        commentRv = (RecyclerView) findViewById(R.id.comment_rv);

        editDate = (MaterialCardView) findViewById(R.id.todo_detail_editDate);
        toolbar = (Toolbar) findViewById(R.id.todo_toolbar);

        //툴바 설정
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //editText UI 안 가리도록
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);



        dataService.userAuth.isMentee(userId).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                if(response.body()){
                    //멘티 접근 수정 코드
                    menteePKId = sharedPreferences.getLong(USER_PK_ID_KEY, 0);
                    String[] array = getResources().getStringArray(R.array.todo_spinner);
                    String[] menteeArray = Arrays.copyOfRange(array,0,3);
                    taskName.setEnabled(false);
                    taskInfo.setEnabled(false);
                    setSpinner(menteeArray);
                    setData();
                    setComment();
                }else{
                    //멘토 접근 수정 코드
                    menteePKId = gIntent.getLongExtra("menteePKId",0);
                    String[] array = getResources().getStringArray(R.array.todo_spinner);
                    setSpinner(array);
                    setData();
                    setDatePicker();
                    setComment();
                }
                setRecyclerView();
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

                changeToDoStatusReq = new ChangeToDoStatusReq(menteePKId, todoId, statusReq); //멘토로 들어왔을때 멘티 아이디가 되어야함

                dataService.assignedToDo.changeStatus(changeToDoStatusReq).enqueue(new Callback<ChangeToDoStatusRes>() {
                    @Override
                    public void onResponse(Call<ChangeToDoStatusRes> call, Response<ChangeToDoStatusRes> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(ToDoDetailActivity.this, "성공적으로 변경하였습니다 ", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ChangeToDoStatusRes> call, Throwable t) {

                    }
                });
                finish();
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
                datePickerDialog.getDatePicker().setMinDate(now.getTime().getTime());

            }


        });
    }

    public void setRecyclerView(){

        adapter = new CommentAdapter();
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        commentRv.setLayoutManager(linearLayoutManager);

        dataService.feedBack.findByAssignedToDo(toDo.getTodoId()).enqueue(new Callback<List<FindFeedBackRes>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<List<FindFeedBackRes>> call, Response<List<FindFeedBackRes>> response) {
                if(response.isSuccessful()){

                    for(FindFeedBackRes data : response.body()){
                        adapter.addItem(data);
                    }
                    commentRv.setAdapter(adapter);
                }
            }
            @Override
            public void onFailure(Call<List<FindFeedBackRes>> call, Throwable t) {

            }
        });
    }


    //댓글 추가 버튼
    private void setComment(){

        comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                commentLayout.setEndIconMode(TextInputLayout.END_ICON_NONE);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                commentLayout.setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
                commentLayout.setEndIconDrawable(R.drawable.ic_send);

                commentLayout.setEndIconOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //comment 다는 메서드 작성
                        WriteFeedBackReq com = new WriteFeedBackReq(toDo.getTodoId(),userIdPk,comment.getText()+"");
                        dataService.feedBack.write(com).enqueue(new Callback<WriteFeedBackRes>() {
                            @Override
                            public void onResponse(Call<WriteFeedBackRes> call, Response<WriteFeedBackRes> response) {
                                if(response.isSuccessful()){
                                    comment.setText("");
                                    setRecyclerView();
                                }
                            }

                            @Override
                            public void onFailure(Call<WriteFeedBackRes> call, Throwable t) {

                            }
                        });

                    }
                });
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

}
