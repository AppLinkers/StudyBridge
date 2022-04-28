package com.example.studybridge.Study.StudyMenti.Detail;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.studybridge.R;
import com.example.studybridge.Util.ItemClickListener;
import com.example.studybridge.Util.MentoSelectDialog;
import com.example.studybridge.databinding.StudyMembersBinding;
import com.example.studybridge.http.DataService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudyMembers extends AppCompatActivity {

    private StudyMembersBinding binding;
    private int path;
    private long studyId;
    private String makerId,userId;

    private MemberAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private ItemClickListener itemClickListener;
    private Activity activity = (Activity)this;

    //SharedPref
    SharedPreferences sharedPreferences;
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_ID_KEY = "user_id_key";

    private DataService dataService = new DataService();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = StudyMembersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userId= sharedPreferences.getString(USER_ID_KEY, "사용자 아이디");

        intentData();
        setUI();
        getData();
        setBackBtn();
    }

    private void intentData(){
        Intent intent = getIntent();
        path = intent.getIntExtra("path",0);
        studyId = intent.getLongExtra("studyId",studyId);
        makerId = intent.getStringExtra("makerId");
    }
    private void setUI(){
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        if(path==0){//멘티 리스트
            binding.title.setText("그룹원");
            binding.mentorList.setVisibility(View.GONE);
            binding.btnCon.setVisibility(View.GONE);
            binding.menteeList.setLayoutManager(linearLayoutManager);
        }
        else {//멘토 리스트
            binding.title.setText("멘토");
            binding.menteeList.setVisibility(View.GONE);
            binding.mentorList.setLayoutManager(linearLayoutManager);
            if(!userId.equals(makerId)){
                binding.btnCon.setVisibility(View.GONE);
            }
        }
    }
    private void getData(){
        if (path==0){
            dataService.study.menteeList(studyId).enqueue(new Callback<List<String>>() {
                @Override
                public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                    if(response.isSuccessful()){
                        adapter = new MemberAdapter(response.body(),path);
                    }
                    binding.menteeList.setAdapter(adapter);
                }

                @Override
                public void onFailure(Call<List<String>> call, Throwable t) {

                }
            });

        }
        else{
            if(!userId.equals(makerId)){
                itemClickListener = null;
            } else {
                itemClickListener = new ItemClickListener() {
                    @Override
                    public void onClick(String s) {
                        binding.mentorList.post(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });
                        binding.selectBtn.setEnabled(true);
                        binding.selectBtn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.palletRed));
                        selectMentor(s);
                    }
                };
            }

            dataService.study.mentorList(studyId).enqueue(new Callback<List<String>>() {
                @Override
                public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                    if(response.isSuccessful()){
                        adapter = new MemberAdapter(response.body(),path,itemClickListener,activity);

                    }
                    binding.mentorList.setAdapter(adapter);
                }

                @Override
                public void onFailure(Call<List<String>> call, Throwable t) {

                }
            });
        }
    }

    private void selectMentor(String s){
        binding.selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDialog(s);
            }
        });
    }
    private void selectDialog(String mentorId){
        MentoSelectDialog dialog = MentoSelectDialog.getInstance();
        FragmentManager fm = getSupportFragmentManager();

        Bundle bundle = new Bundle();
        bundle.putString("mentorId",mentorId);
        bundle.putLong("studyId",studyId);
        dialog.setArguments(bundle);
        dialog.show(fm,"selectDialog");

        dialog.setSelectInterface(new MentoSelectDialog.SelectInterface() {
            @Override
            public void okBtnClick() {
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    private void setBackBtn(){
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });
    }
}
