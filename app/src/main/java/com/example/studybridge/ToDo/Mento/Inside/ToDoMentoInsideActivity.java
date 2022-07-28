package com.example.studybridge.ToDo.Mento.Inside;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.studybridge.Chat.ChatActivity;
import com.example.studybridge.R;
import com.example.studybridge.ToDo.Mento.ToDoAddActivity;
import com.example.studybridge.databinding.TodoMentoInsideActivityBinding;
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.message.FindRoomRes;
import com.example.studybridge.http.dto.study.StudyFindRes;
import com.example.studybridge.http.dto.toDo.FindToDoRes;
import com.example.studybridge.http.dto.userAuth.UserProfileRes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ToDoMentoInsideActivity extends AppCompatActivity {


    private TodoMentoInsideActivityBinding binding;
    private DataService dataService = new DataService();

    //리사이클러뷰
    private LinearLayoutManager linearLayoutManager;
    private ToDoMentoInsideAdapter adapter;
    private Activity activity;

    private StudyFindRes study;

    //sharedPref
    SharedPreferences sharedPreferences;
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_PK_ID_KEY = "user_pk_id_key";
    private Long userId;

    private Long roomId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TodoMentoInsideActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userId= sharedPreferences.getLong(USER_PK_ID_KEY, 0);

        intentData();
        getProfile(study.getId());
        getData();
        setFloatingActionButton();
        goChat();


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
        setImgCnt(study.getMenteeCnt());
        binding.num.setText(sb.toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void getData(){

        activity = (Activity) this;
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        binding.RCView.setLayoutManager(linearLayoutManager);
        dataService.toDo.findOfStudy(study.getId(),userId).enqueue(new Callback<List<FindToDoRes>>() {
            @Override
            public void onResponse(Call<List<FindToDoRes>> call, Response<List<FindToDoRes>> response) {
                assert response.body() != null;
                adapter = new ToDoMentoInsideAdapter(response.body(), activity);
                binding.RCView.setAdapter(adapter);
                StringBuilder sb = new StringBuilder();
                sb.append("(").append(adapter.getItemCount()).append(")");
                binding.listNum.setText(sb.toString());
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

/*    private void getProfile(){

        final int size = menteeArr.size();

        if(size<=1){
            binding.imgSecondCon.setVisibility(View.GONE);
            binding.imgThirdCon.setVisibility(View.GONE);
            setImg(menteeArr.get(0),binding.img1);

        }
        else if(size == 2){
            binding.imgThirdCon.setVisibility(View.GONE);
            setImg(menteeArr.get(0),binding.img1);
            setImg(menteeArr.get(1),binding.img2);
        }
        else {
            setImg(menteeArr.get(0),binding.img1);
            setImg(menteeArr.get(1),binding.img2);
            setImg(menteeArr.get(2),binding.img3);
        }
    }*/
    private void setImgCnt(int cnt){
        if(cnt<=1){
            binding.imgSecondCon.setVisibility(View.GONE);
            binding.imgThirdCon.setVisibility(View.GONE);
        }
        else if(cnt == 2){
            binding.imgThirdCon.setVisibility(View.GONE);
        }
    }
    private void getProfile(Long studyId){

        dataService.study.menteeList(studyId).enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if(response.isSuccessful()){

                    List<String> menteeList = response.body();
                    final int size = response.body().size();

                    if(size<=1){
                        setImg(menteeList.get(0),binding.img1);

                    }
                    else if(size == 2){
                        setImg(menteeList.get(0),binding.img1);
                        setImg(menteeList.get(1),binding.img2);
                    }
                    else {
                        setImg(menteeList.get(0),binding.img1);
                        setImg(menteeList.get(1),binding.img2);
                        setImg(menteeList.get(2),binding.img3);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {

            }
        });
    }

    private void setImg(String userId, ImageView imageView){

        dataService.userAuth.getProfile(userId).enqueue(new Callback<UserProfileRes>() {
            @Override
            public void onResponse(Call<UserProfileRes> call, Response<UserProfileRes> response) {
                if(response.isSuccessful()){
                    final String path = response.body().getProfileImg();
                    Glide.with(getApplicationContext()).load(path).into(imageView);
                }
            }

            @Override
            public void onFailure(Call<UserProfileRes> call, Throwable t) {

            }
        });
    }

    private void goChat(){
        getRoom();
        binding.goChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chatIntent = new Intent(getApplicationContext(), ChatActivity.class);
                chatIntent.putExtra("roomId", roomId);
                chatIntent.putExtra("study",study);
                view.getContext().startActivity(chatIntent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });
    }

    private void getRoom(){
        dataService.chat.getRoom(study.getId()).enqueue(new Callback<FindRoomRes>() {
            @Override
            public void onResponse(Call<FindRoomRes> call, Response<FindRoomRes> response) {
                if(response.isSuccessful()){
                    roomId = response.body().getRoomId();
                }
            }

            @Override
            public void onFailure(Call<FindRoomRes> call, Throwable t) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}
