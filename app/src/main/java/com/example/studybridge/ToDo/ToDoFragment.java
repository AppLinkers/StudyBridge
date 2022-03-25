package com.example.studybridge.ToDo;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.R;
import com.example.studybridge.ToDo.Menti.ToDoMentiAdapter;
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.study.StudyFindRes;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ToDoFragment extends Fragment {

    private int resource=0;

    private TextView year,month,day,taskCount;
    //리사이클러뷰
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    //dataservice
    private DataService dataService;

    private ArrayList<ToDo> data;

    private String userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.todo_menti_fragment,container,false);

        defineMentee();

        Bundle bundle = getArguments();
        userId = bundle.getString("id");

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

        return view;
    }


    @SuppressLint("SimpleDateFormat")
    private void setTime(){
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        year.setText(new SimpleDateFormat("yyyy").format(date));
        month.setText(new SimpleDateFormat("MMM", Locale.ENGLISH).format(date));
        day.setText(new SimpleDateFormat("dd").format(date));
    }

    private void setRecyclerView(){
        setData();

        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new ToDoMentiAdapter(data));
    }

    private void defineMentee(){
        dataService = new DataService();
        AsyncTask<Void, Void, Boolean> listAPI = new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                Call<Boolean> call = dataService.userAuth.isMentee(userId);
                try {
                    return call.execute().body();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Boolean s) {
                super.onPostExecute(s);
            }
        }.execute();
        Boolean result = null;
        try {
            result = listAPI.get();
        } catch (Exception e) {
            e.printStackTrace();
        }



    }


    private void setData(){
        ToDo toDo1 = new ToDo(null,1,"mentor",null,"문제집 5페이지 풀기",null,"2022/03/28",null);
//        ToDo toDo2 = new ToDo(null,1,null,null,"알고리즘 문제 복습",null,"2022/03/26",null);
        ToDo toDo3 = new ToDo(null,0,"mentor",null,"턱걸이 15회.",null,"2022/04/28",null);
        ToDo toDo4 = new ToDo(null,2,"mentor",null,"벤치프레스 10회",null,"2022/03/23",null);

        data = new ArrayList<>();
        data.add(toDo1);
//        data.add(toDo2);
        data.add(toDo3);
        data.add(toDo4);
    }

}
