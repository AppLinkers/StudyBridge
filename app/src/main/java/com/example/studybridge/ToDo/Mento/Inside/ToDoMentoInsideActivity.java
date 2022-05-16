package com.example.studybridge.ToDo.Mento.Inside;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.R;
import com.example.studybridge.ToDo.Mento.ToDoAddActivity;
import com.example.studybridge.databinding.TodoMentoInsideActivityBinding;
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.study.StudyFindRes;
import com.example.studybridge.http.dto.toDo.FindToDoRes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ToDoMentoInsideActivity extends AppCompatActivity {


    private TodoMentoInsideActivityBinding binding;
    private DataService dataService;

    //리사이클러뷰
    private LinearLayoutManager linearLayoutManager;
    private ToDoMentoInsideAdapter adapter;

    private StudyFindRes study;

    //sharedPref
    SharedPreferences sharedPreferences;
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_PK_ID_KEY = "user_pk_id_key";
    private Long userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TodoMentoInsideActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userId= sharedPreferences.getLong(USER_PK_ID_KEY, 0);

        intentData();

        getData();
        setFloatingActionButton();

    }

    private void intentData(){
        Intent intent = getIntent();
        study = intent.getExtras().getParcelable("study");
        binding.title.setText(study.getName());
        binding.intro.setText(study.getInfo());
        binding.subject.setText(study.getType());
        binding.place.setText(study.getPlace());
        StringBuilder sb = new StringBuilder();
        sb.append(study.getMenteeCnt()).append("명 참여중..");
        binding.num.setText(sb.toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void setRecyclerView(ArrayList<FindToDoRes> findToDoRes) {

        adapter = new ToDoMentoInsideAdapter();
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.RCView.setLayoutManager(linearLayoutManager);
        for(FindToDoRes data : findToDoRes){
            adapter.addItem(data);
        }
        binding.RCView.setAdapter(adapter);

        StringBuilder sb = new StringBuilder();
        sb.append("(").append(adapter.getItemCount()).append(")");
        binding.listNum.setText(sb.toString());

    }
    private void getData(){
        dataService = new DataService();
        dataService.toDo.findOfStudy(study.getId(),userId).enqueue(new Callback<List<FindToDoRes>>() {
            final ArrayList<FindToDoRes> findToDoRes = new ArrayList<>();
            @Override
            public void onResponse(Call<List<FindToDoRes>> call, Response<List<FindToDoRes>> response) {
                assert response.body() != null;
                findToDoRes.addAll(response.body());
                setRecyclerView(findToDoRes);
            }

            @Override
            public void onFailure(Call<List<FindToDoRes>> call, Throwable t) {

            }
        });
    }

    private void setFloatingActionButton(){
        binding.todoAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ToDoAddActivity.class);
                intent.putExtra("studyId", study.getId());
                intent.putExtra("mentorId", userId);
                startActivity(intent);
            }

        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}
