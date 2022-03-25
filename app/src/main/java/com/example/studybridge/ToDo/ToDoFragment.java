package com.example.studybridge.ToDo;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.R;
import com.example.studybridge.ToDo.Menti.ToDoMentiAdapter;
import com.example.studybridge.http.DataService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;

public class ToDoFragment extends Fragment {

    private int resource = 0;

    private TextView year, month, day, taskCount;
    //리사이클러뷰
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;



    private String userId;
    private boolean isMentee;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view;

        Bundle bundle = getArguments();
        userId = bundle.getString("id");
        isMentee = bundle.getBoolean("isMentee");
        if (isMentee == true) {
            resource = R.layout.todo_menti_fragment;
            view = inflater.inflate(resource, container, false);
            setMenteeUI(view);
        } else {
            resource = R.layout.todo_mentor_fragment;
            view = inflater.inflate(resource, container, false);
        }

        return view;
    }

    private void setMenteeUI(View view) {
        //화면 위 데이터
        year = (TextView) view.findViewById(R.id.todo_year_tv);
        month = (TextView) view.findViewById(R.id.todo_month_tv);
        day = (TextView) view.findViewById(R.id.todo_day_tv);
        taskCount = (TextView) view.findViewById(R.id.todo_taskCount);
        recyclerView = (RecyclerView) view.findViewById(R.id.todo_menti_RV);

        //날짜 설정
        setTime();

        //리사이클러뷰 설정
        setRecyclerView();
    }
    @SuppressLint("SimpleDateFormat")
    private void setTime() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        year.setText(new SimpleDateFormat("yyyy").format(date));
        month.setText(new SimpleDateFormat("MMM", Locale.ENGLISH).format(date));
        day.setText(new SimpleDateFormat("dd").format(date));
    }

    private void setRecyclerView() {

        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new ToDoMentiAdapter());
    }

<<<<<<< HEAD

=======
>>>>>>> 58ee4734fd07cd7671c26a1da4a02ccfffa2c711
}

