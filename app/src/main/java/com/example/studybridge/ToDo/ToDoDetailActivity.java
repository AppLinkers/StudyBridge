package com.example.studybridge.ToDo;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ToDoDetailActivity extends AppCompatActivity {

    public static String format_yyyyMMdd = "yyyy년 MM월 dd일";
    public static String format_weekDay = "EE요일";
    private TextView todayDate;
    private TextView todayDay;
    private TextView todo_detail_num;
    private TextView todo_remain;

    private RecyclerView recyclerView;
    private ToDoDetialAdapter adapter;
    private ToDoInsideAdapter assignAdapter;
    private ToDoDetailHolder holder;
    private ArrayList<ToDoDetail> arrayList;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_detail_activity);

        todayDate = (TextView) findViewById(R.id.todyDate);
        todayDay = (TextView) findViewById(R.id.todayDay);
        todo_detail_num = (TextView) findViewById(R.id.todo_detail_num);
        recyclerView = findViewById(R.id.todo_detail_RCView);
        toolbar = findViewById(R.id.todo_bar);
        todo_remain = findViewById(R.id.todo_remain);


        Intent intent = getIntent();

        //툴바 설정
        toolbar.setTitle(intent.getExtras().getString("name"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //오늘 날짜 설정
        todayDate.setText(getCurrentDate_yyyyMMdd());
        todayDay.setText(getCurrentDate_EE());

        assignAdapter = new ToDoInsideAdapter();

        //리사이클러뷰
        arrayList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ToDoDetialAdapter();
        getData();
        recyclerView.setAdapter(adapter);

        getAssignData();
        addData(intent);

        //남은 할 일
        int leftCount = adapter.getAssignCount();
        double wholeCount = adapter.getWholeCount();
        double perc = Math.round((double) leftCount/wholeCount*100);

        String remainP = "총 "+perc+"% 완료";

        StringBuilder sb = new StringBuilder();
        sb.append("할 일 ").append(String.valueOf(leftCount)).append("개 남음");

        todo_remain.setText(remainP);
        todo_detail_num.setText(sb.toString());
    }


    private void getData() {
        ToDoDetail d1 = new ToDoDetail("Ready");
        ToDoDetail d2 = new ToDoDetail("Progress");
        ToDoDetail d3 = new ToDoDetail("Done");

        adapter.addItem(d1);
        adapter.addItem(d2);
        adapter.addItem(d3);
    }

    private void addData(Intent intent){
        ToDoInside newD = (ToDoInside) intent.getSerializableExtra("newD");
        if(newD != null) {
            adapter.addAssign(newD);
        }
    }



    private void getAssignData(){
        ToDoInside l = new ToDoInside("운전과제","3.1","Done","운");
        ToDoInside l1 = new ToDoInside("운전과제","3.1","Progress","운");
        ToDoInside l2 = new ToDoInside("물리과제 ","5.1","Ready","운");
        ToDoInside l4 = new ToDoInside("물리과제 ","5.1","Ready","운");
        ToDoInside l3 = new ToDoInside("화학과제 ","7.1","Ready","운");
        adapter.addAssign(l);
        adapter.addAssign(l1);
        adapter.addAssign(l2);
        adapter.addAssign(l3);
        adapter.addAssign(l4);
    }


    public static String getCurrentDate_yyyyMMdd() {
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat(format_yyyyMMdd, Locale.getDefault());
        return format.format(currentTime);
    }
    public static String getCurrentDate_EE() {
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat(format_weekDay, Locale.getDefault());
        return format.format(currentTime);
    }

    //toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.todo_toolbar_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            case R.id.toolbar_add:
                ToDoAddDialog dialog = new ToDoAddDialog(ToDoDetailActivity.this);
                dialog.callFunction();
                return true;
            case R.id.toolbar_menu:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
