package com.example.studybridge.Study;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.studybridge.Util.StudyAddDialog;
import com.example.studybridge.Util.AddTextWatcher;
import com.example.studybridge.databinding.StudyAddActivityBinding;
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.study.StudyFindRes;
import com.example.studybridge.http.dto.study.StudyMakeReq;
import com.example.studybridge.http.dto.study.StudyMakeRes;
import com.example.studybridge.http.dto.study.StudyUpdateReq;
import com.example.studybridge.http.dto.study.StudyUpdateRes;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudyAddActivity extends AppCompatActivity {

    private StudyAddActivityBinding binding;

    private DataService dataService = new DataService();
    private StudyFindRes study;
    private Long userPkId;
    private String userId;
    private boolean isNull =false;

    public static final int SUBJECT = 0;
    public static final int PLACE = 1;

    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_ID_KEY = "user_id_key";
    public static final String USER_PK_ID_KEY = "user_pk_id_key";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = StudyAddActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userId= sharedPreferences.getString(USER_ID_KEY, "사용자 아이디");
        userPkId = sharedPreferences.getLong(USER_PK_ID_KEY, 0);

        intentData();
        setUI();

    }
    private void intentData(){
        Intent intent = getIntent();
        study = intent.getExtras().getParcelable("study");
    }

    private void setUI(){

        //toolbar
        setSupportActionBar(binding.appBar);
        binding.appBar.setTitle("스터디 만들기");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(study==null){ //추가하기로 들어왔을 때

        }
        else{ //수정하기로 들어왔을 때
            binding.studyName.setText(study.getName());
            binding.studyIntro.setText(study.getInfo());
            binding.studyNum.setText(String.valueOf(study.getMaxNum()));
            binding.studyExplain.setText(study.getExplain());
            binding.studySubject.setText(study.getType());
            binding.studyPlace.setText(study.getPlace());

        }

        EditText[] editList = {binding.studyName,binding.studySubject,binding.studyPlace,binding.studyIntro,binding.studyNum,binding.studyExplain};
        AddTextWatcher watcher = new AddTextWatcher(getApplicationContext(),binding.addBtn,editList);
        for(EditText editText: editList){
            editText.addTextChangedListener(watcher);
        }

        selectBtns();
        setBtn();
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

    private void selectBtns(){
        binding.studySubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(binding.studySubject,SUBJECT);
            }
        });


        binding.studyPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(binding.studyPlace,PLACE);
            }
        });
    }
    private void showDialog(TextView textView,int type){
        StudyAddDialog dialog = StudyAddDialog.getInstance(type);
        FragmentManager fm = getSupportFragmentManager();
        dialog.show(fm,"addDialog");

        dialog.setAddInterface(new StudyAddDialog.AddInterface() {
            @Override
            public void selectOne(String s) {
                textView.setText(s);
            }
        });

    }


    private void setBtn(){

        binding.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(study==null){
                    StudyMakeReq studyMakeReq = new StudyMakeReq(
                            userId,
                            binding.studyName.getText().toString()+"",
                            binding.studySubject.getText().toString()+"",
                            binding.studyIntro.getText().toString()+"",
                            binding.studyExplain.getText().toString()+"",
                            binding.studyPlace.getText().toString()+"",
                            Integer.parseInt(binding.studyNum.getText().toString()+""));

                    dataService.study.make(studyMakeReq).enqueue(new Callback<StudyMakeRes>() {
                        @Override
                        public void onResponse(Call<StudyMakeRes> call, Response<StudyMakeRes> response) {
                            if (response.isSuccessful()) {
/*                                Log.d("test", String.valueOf(response.raw()));*/
                                Toast.makeText(StudyAddActivity.this, "스터디 등록 완료!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<StudyMakeRes> call, Throwable t) {

                        }
                    });
                }
                else{
                    StudyUpdateReq studyUpdateReq = new StudyUpdateReq(
                            study.getId(),
                            userPkId,
                            binding.studyName.getText().toString()+"",
                            binding.studySubject.getText().toString()+"",
                            binding.studyIntro.getText().toString()+"",
                            binding.studyExplain.getText().toString()+"",
                            binding.studyPlace.getText().toString()+"",
                            Integer.parseInt(binding.studyNum.getText().toString()+""));
                    dataService.study.update(studyUpdateReq).enqueue(new Callback<StudyUpdateRes>() {
                        @Override
                        public void onResponse(Call<StudyUpdateRes> call, Response<StudyUpdateRes> response) {
                            if(response.isSuccessful()){
                                Toast.makeText(StudyAddActivity.this, "업데이트 완료!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<StudyUpdateRes> call, Throwable t) {

                        }
                    });
                }
            }
        });
    }


}
