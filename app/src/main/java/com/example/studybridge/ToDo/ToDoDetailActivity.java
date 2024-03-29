package com.example.studybridge.ToDo;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.studybridge.MainActivity;
import com.example.studybridge.R;
import com.example.studybridge.ToDo.feedback.CommentAdapter;
import com.example.studybridge.databinding.TodoDetailActivityBinding;
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.assignedToDo.ChangeToDoStatusReq;
import com.example.studybridge.http.dto.assignedToDo.ChangeToDoStatusRes;
import com.example.studybridge.http.dto.assignedToDo.FindAssignedToDoRes;
import com.example.studybridge.http.dto.feedBack.FindFeedBackRes;
import com.example.studybridge.http.dto.feedBack.WriteFeedBackReq;
import com.example.studybridge.http.dto.feedBack.WriteFeedBackRes;
import com.example.studybridge.http.dto.toDo.UpdateToDoDueDateReq;
import com.example.studybridge.http.dto.userAuth.UserProfileRes;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
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

    private TodoDetailActivityBinding binding;

    private Calendar calendar = Calendar.getInstance();
    private Calendar now = Calendar.getInstance();
    private DataService dataService;
    ChangeToDoStatusReq changeToDoStatusReq;

    //comment recycler
    private CommentAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

    SharedPreferences sharedPreferences;
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_PK_ID_KEY = "user_pk_id_key";
    public static final String USER_ID_KEY = "user_id_key";
    public static final String USER_ISMENTEE = "user_mentee_key";
    private Boolean isMentee;

    Long userIdPk,menteePKId;
    long dayResult;
    String userId;
    Intent gIntent;
    FindAssignedToDoRes toDo;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TodoDetailActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //sharedPref
        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userId = sharedPreferences.getString(USER_ID_KEY, "user");
        userIdPk= sharedPreferences.getLong(USER_PK_ID_KEY, 0);
        isMentee = sharedPreferences.getBoolean(USER_ISMENTEE,false);

        gIntent = getIntent();
        toDo = gIntent.getExtras().getParcelable("toDo");
        dayResult = gIntent.getLongExtra("dayResult",0);


        dataService = new DataService();

        //툴바 설정
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setPath();
        setRecyclerView();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setPath(){

        if(isMentee){
            //멘티 접근 수정 코드
            menteePKId = sharedPreferences.getLong(USER_PK_ID_KEY, 0);
            String[] array = getResources().getStringArray(R.array.todo_spinner);
            String[] menteeArray = Arrays.copyOfRange(array,0,3);
            setSpinner(menteeArray);
            setUI();
            setComment();
        } else {
            menteePKId = gIntent.getLongExtra("menteePKId",0);
            String[] array = getResources().getStringArray(R.array.todo_spinner);
            setSpinner(array);
            setUI();
            setDatePicker();
            setComment();
        }
    }



    //툴바 설정
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.todo_toolbar_menu,menu);
        return true;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                return true;

            case R.id.todo_save:

                String dueDate = binding.dueDate.getText().toString();

                if(!dueDate.equals(dateFormat(toDo.getDueDate()))){

                    String[] splitDate = dueDate.split("/"); // 현재 날짜
                    LocalDateTime dateTime = LocalDateTime.of(
                            Integer.parseInt(splitDate[0]),
                            Integer.parseInt(splitDate[1]),
                            Integer.parseInt(splitDate[2]),0,0);

                    String localDateTime = dateTime+":00";

                    UpdateToDoDueDateReq dateReq = new UpdateToDoDueDateReq(toDo.getToDoId(),localDateTime);

                    dataService.toDo.updateDueDate(dateReq).enqueue(new Callback<Integer>() {
                        @Override
                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                            if(response.isSuccessful()){

                            }
                        }

                        @Override
                        public void onFailure(Call<Integer> call, Throwable t) {

                        }
                    });
                }

                if(binding.spinner.getVisibility() == View.VISIBLE) {
                    String statusReq = binding.spinner.getSelectedItem().toString().trim();
                    changeToDoStatusReq = new ChangeToDoStatusReq(menteePKId, toDo.getId(), statusReq); //멘토로 들어왔을때 멘티 아이디가 되어야함
                    dataService.assignedToDo.changeStatus(changeToDoStatusReq).enqueue(new Callback<ChangeToDoStatusRes>() {
                        @Override
                        public void onResponse(Call<ChangeToDoStatusRes> call, Response<ChangeToDoStatusRes> response) {
                            if(response.isSuccessful()){
                                finish();
                                Toast.makeText(ToDoDetailActivity.this, "변경 완료", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ChangeToDoStatusRes> call, Throwable t) {
                            System.out.println("실패");
                        }
                    });
                }



                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //spinner
    private void setSpinner(String[] array){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,
                array);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinner.setAdapter(adapter);

        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    //데이터

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setUI(){

        setSpinnerText();

        binding.taskName.setText(toDo.getTask());
        binding.dueDate.setText(dateFormat(toDo.getDueDate()));
        binding.mentorId.setText(toDo.getMentorName());
        binding.menteeId.setText(toDo.getMenteeName());
        binding.taskInfo.setText(toDo.getExplain());
        Glide.with(this).load(toDo.getMenteeProfileImg()).into(binding.menteeImg);
        Glide.with(this).load(toDo.getMentorProfileImg()).into(binding.mentorImg);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String dateFormat(String dueDate){

        LocalDateTime localDateTime = LocalDateTime.parse(dueDate);
        StringBuilder sb = new StringBuilder();
        sb.append(localDateTime.getYear())
                .append("/").append(overTenInt(localDateTime.getMonthValue()))
                .append("/").append(overTenInt(localDateTime.getDayOfMonth()));

        return sb.toString();
    }

    private String overTenInt(int month){
        if(month<10){
            StringBuilder sb = new StringBuilder();
            sb.append("0").append(month);
            return sb.toString();
        }
        else return String.valueOf(month);
    }



    private void setSpinnerText(){

        if(isMentee){
            if(dayResult<0){
                if(statusNum(toDo.getStatus())==3){
                    binding.spinner.setVisibility(View.GONE);
                    binding.confMent.setVisibility(View.VISIBLE);
                } else {
                    binding.spinner.setVisibility(View.GONE);
                    binding.afterDateMent.setVisibility(View.VISIBLE);
                }
            } else{
                if(statusNum(toDo.getStatus())<3){
                    binding.spinner.setSelection(statusNum(toDo.getStatus()));
                } else {
                    binding.spinner.setVisibility(View.GONE);
                    binding.confMent.setVisibility(View.VISIBLE);
                }
            }
        }
        else {
            binding.spinner.setSelection(statusNum(toDo.getStatus()));
        }

    }

    private int statusNum(String status){
        switch (status){
            case "READY":
                return 0;
            case "PROGRESS":
                return 1;
            case "DONE":
                return 2;
            default:
                return 3;
        }
    }

    //데이트 피커
    private void setDatePicker(){

        String[] splitDate = binding.dueDate.getText().toString().split("/"); // 현재 날짜

        DatePickerDialog.OnDateSetListener datePicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                calendar.set(year,month,day);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd",Locale.KOREA);

                binding.dueDate.setText(sdf.format(calendar.getTime()));

            }
        };

        binding.editDate.setOnClickListener(new View.OnClickListener() {
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
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.commentRv.setLayoutManager(linearLayoutManager);


        dataService.feedBack.findByAssignedToDo(toDo.getId()).enqueue(new Callback<List<FindFeedBackRes>>() {
            @Override
            public void onResponse(Call<List<FindFeedBackRes>> call, Response<List<FindFeedBackRes>> response) {
                if(response.isSuccessful()){

                    for(FindFeedBackRes data : response.body()){
                        adapter.addItem(data);
                    }
                    binding.commentRv.setAdapter(adapter);
                    binding.commentRv.scrollToPosition(adapter.getItemCount()-1);
                }
            }
            @Override
            public void onFailure(Call<List<FindFeedBackRes>> call, Throwable t) {

            }
        });

    }


    //댓글 추가 버튼
    private void setComment(){

        binding.comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(charSequence.toString().equals("")){
                    binding.sendImg.setImageResource(R.drawable.ic_send_gray);
                    binding.send.setClickable(false);
                }
                else {

                    binding.sendImg.setImageResource(R.drawable.ic_send);
                    binding.send.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //comment 다는 메서드 작성
                            WriteFeedBackReq com = new WriteFeedBackReq(toDo.getId(),userIdPk,binding.comment.getText()+"");
                            dataService.feedBack.write(com).enqueue(new Callback<WriteFeedBackRes>() {
                                @Override
                                public void onResponse(Call<WriteFeedBackRes> call, Response<WriteFeedBackRes> response) {
                                    if(response.isSuccessful()){
                                        binding.comment.setText("");
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

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }
}
