package com.example.studybridge.Mypage.MentoProfile.Edit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.R;
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.userMentor.Certificate;
import com.example.studybridge.http.dto.userMentor.ProfileRes;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPageMentoProfileEditActivity extends AppCompatActivity {

    //화면 위 데이터
    private Toolbar toolbar;
    private TextView name, school,goToCheck;
    private TextInputEditText intro,nickName,curi,experience,appeal;
    private ProfileRes mentoProfile;
    private ChipGroup subjectGroup,placeGroup;
    public String selectedSubject;
    public String selectedPlace;

    //자격증 이미지 add
    private MaterialCardView addImg;
    public static final int PICK_IMAGE = 101;
    public static final int SCHOOL_CHECK = 201;
    private MyPageMentoProfileAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private List<Certificate> certificates;

    //SharedPreference
    SharedPreferences sharedPreferences;
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_NAME = "user_name_key";
    public static final String USER_ID_KEY = "user_id_key";
    public static final String USER_PK_ID_KEY = "user_pk_id_key";

    private String userName;
    private String userLoginId;
    private Long userPkId;

    //학생증 이미지
    private String schoolDir;

    DataService dataService = new DataService();

    Gson gson = new Gson();



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_mentoprofile_edit_activity);

        toolbar = (Toolbar) findViewById(R.id.mypage_mentoProfile_bar);

        //sharedPrefer
        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userName = sharedPreferences.getString(USER_NAME, "사용자");
        userLoginId = sharedPreferences.getString(USER_ID_KEY, "userLoginId");
        userPkId = sharedPreferences.getLong(USER_PK_ID_KEY, 0);

        //화면 위 데이터
        name = (TextView) findViewById(R.id.mypage_mentoProfile_name); //기존 이름에서 받아와야함
        school = (TextView) findViewById(R.id.mypage_mentoProfile_school);
        intro = (TextInputEditText) findViewById(R.id.mypage_mentoProfile_intro);
        nickName = (TextInputEditText) findViewById(R.id.mypage_mentoProfile_nickName);
        curi = (TextInputEditText) findViewById(R.id.mypage_mentoProfile_curi);
        experience = (TextInputEditText) findViewById(R.id.mypage_mentoProfile_experience);
        appeal = (TextInputEditText) findViewById(R.id.mypage_mentoProfile_appeal);

        //지역, 과목 선택
        subjectGroup = (ChipGroup) findViewById(R.id.mypage_mentoProfile_subjectSelect);
        placeGroup = (ChipGroup) findViewById(R.id.mypage_mentoProfile_placeSelect);

        //자격증 이미지 추가하기
        addImg = (MaterialCardView) findViewById(R.id.mypage_mentoProfile_addImg);
        recyclerView = (RecyclerView) findViewById(R.id.mypage_mentoProfile_rcView);

        getIntentData();
        setToolbar();
        setRecyclerView();
        setButton();


    }


    private void setRecyclerView(){
        certificates = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new MyPageMentoProfileAdapter(certificates);
        recyclerView.setAdapter(adapter);
    }

    private void getIntentData(){
        //기존 입력값
        Intent intent = getIntent();
        mentoProfile = (ProfileRes) intent.getSerializableExtra("profile");
        name.setText(userName);
        school.setText(mentoProfile.getSchool());
        intro.setText(mentoProfile.getInfo());
        nickName.setText(mentoProfile.getNickName());
        curi.setText(mentoProfile.getCurriculum());
        experience.setText(mentoProfile.getExperience());
        appeal.setText(mentoProfile.getAppeal());

        Chip chipForSubject = (Chip) subjectGroup.getChildAt(checkChipForSubject(mentoProfile.getSubject()));
        Chip chipForPlace = (Chip) placeGroup.getChildAt(checkChipForPlace(mentoProfile.getLocation()));
        chipForSubject.setChecked(true);
        chipForPlace.setChecked(true);
    }

    private void setButton(){
        //학교 인증 가기
        goToCheck = (TextView) findViewById(R.id.mypage_mentoProfile_school_goToCheck);
        goToCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyPageMentoProfileEditActivity.this,MyPageMentoProfileSchoolCheck.class);
                intent.putExtra("schoolName",mentoProfile.getSchool());
                intent.putExtra("schoolImg",mentoProfile.getSchoolImg());
                startActivityForResult(intent,SCHOOL_CHECK);
            }
        });
        addImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);

            }
        });
    }

    //툴바 설정
    private void setToolbar(){

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    //toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mypage_mentoprofile_menu,menu);
        return true;
    }

    //메뉴 버튼 설정
    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("DefaultLocale")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
                //상단 완료버튼 눌렀을때
            case R.id.mentoProfile_complete_btn:

/*                certificatesImg = new ArrayList<>();
                certificatesName = new ArrayList<>();*/

/*                for(int i=0; i<arrayList.size();i++){
                    String dir = saveBitmapToJpg(arrayList.get(i).getQualiImg(), String.format("certificateImg%d", i));
                    File imgFile = new File(dir);
                    certificatesImg.add(imgFile);
                    Log.d("test", arrayList.get(i).toString());
                    certificatesName.add(arrayList.get(i).getQualiName());
                }*/

                List<Certificate> inputCerti = new ArrayList<>();

                for(int i=0; i<certificates.size(); i++){
                    inputCerti.add(new Certificate(
                                    certificates.get(i).getCertificate(),
                                    certificates.get(i).getImgUrl()));
                }

                Toast.makeText(this, inputCerti.get(0).getCertificate() + " " +inputCerti.get(0).getImgUrl(), Toast.LENGTH_SHORT).show();

                for(int i=0; i<placeGroup.getChildCount();i++){
                    Chip chip = (Chip) placeGroup.getChildAt(i);
                    if(chip.isChecked()){
                        selectedPlace = chip.getText().toString();
                    }
                }

                for(int i=0; i<subjectGroup.getChildCount();i++){
                    Chip chip = (Chip) subjectGroup.getChildAt(i);
                    if(chip.isChecked()){
                        selectedSubject = chip.getText().toString();
                    }
                }

                mentoProfile = new ProfileRes(
                        userPkId,
                        userName,
                        selectedPlace,
                        intro.getText().toString(),
                        nickName.getText().toString(),
                        school.getText().toString(),
                        schoolDir,
                        selectedSubject,
                        inputCerti,
                        experience.getText().toString(),
                        curi.getText().toString(),
                        appeal.getText().toString(),
                        false
                );

                Map<String, RequestBody> profileReq = new HashMap<>();

                profileReq.put("profileTextReq.userLoginId", RequestBody.create(MultipartBody.FORM, userLoginId));
                profileReq.put("profileTextReq.location", RequestBody.create(MultipartBody.FORM, mentoProfile.getLocation()));
                profileReq.put("profileTextReq.info", RequestBody.create(MultipartBody.FORM, mentoProfile.getInfo()));
                profileReq.put("profileTextReq.nickName", RequestBody.create(MultipartBody.FORM, mentoProfile.getNickName()));
                profileReq.put("profileTextReq.subject", RequestBody.create(MultipartBody.FORM, mentoProfile.getSubject()));
                profileReq.put("profileTextReq.school", RequestBody.create(MultipartBody.FORM, mentoProfile.getSchool()));
                profileReq.put("profileTextReq.experience", RequestBody.create(MultipartBody.FORM, mentoProfile.getExperience()));
                profileReq.put("profileTextReq.curriculum", RequestBody.create(MultipartBody.FORM, mentoProfile.getCurriculum()));
                profileReq.put("profileTextReq.appeal", RequestBody.create(MultipartBody.FORM, mentoProfile.getAppeal()));

                List<MultipartBody.Part> certificates = new ArrayList<>();
                List<MultipartBody.Part> certificatesImgReq = new ArrayList<>();
                if (mentoProfile.getCertificates().size() > 0) {
                    mentoProfile.getCertificates().forEach(cn -> {
                        Uri imgUri = Uri.parse(cn.getImgUrl());
                        certificates.add(MultipartBody.Part.createFormData("certificates", cn.getCertificate()));
                        RequestBody certificateImg = RequestBody.create(MediaType.parse("multipart/form-data"), new File(String.valueOf(imgUri)));
                        certificatesImgReq.add(MultipartBody.Part.createFormData("certificatesImg", cn.getCertificate(), certificateImg));
                    });
                }

                // school Img
                RequestBody schoolImg = RequestBody.create(MediaType.parse("multipart/form-data"), new File(mentoProfile.getSchoolImg()));
                MultipartBody.Part schoolImgReq = MultipartBody.Part.createFormData("schoolImg", mentoProfile.getSchool(), schoolImg);


                dataService.userMentor.profile(schoolImgReq,certificatesImgReq,certificates, profileReq).enqueue(new Callback<ProfileRes>() {
                    @Override
                    public void onResponse(Call<ProfileRes> call, Response<ProfileRes> response) {
                        if (response.isSuccessful()) {
                            Log.d("test", response.body().toString());
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<ProfileRes> call, Throwable t) {
                        Log.d("test", t.toString());
                    }
                });


                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==SCHOOL_CHECK){
            if(resultCode==RESULT_OK){
                String schoolName = data.getStringExtra("schoolResult");
                schoolDir = data.getStringExtra("schoolImg");
                school.setText(schoolName);
            }
        }
        else if(requestCode==PICK_IMAGE){
            if(resultCode==RESULT_OK) {
                String dir = String.valueOf(data.getData());
                certificates.add(new Certificate(dir));
                adapter.notifyDataSetChanged();
            }
        }
    }

      //사진
    public String makeDir(String imgUrl) {
        File storage = getCacheDir(); //  path = /data/user/0/YOUR_PACKAGE_NAME/cache
        String fileName = imgUrl + ".jpg";
        File imgFile = new File(storage, fileName);
        Log.d("imgPath" , getCacheDir() + "/" +fileName);
        return getCacheDir() + "/" +fileName;
    }


    //기존 과목&&지역 선택 함수
    public static int checkChipForSubject(String str){
        if(str!=null){
            switch (str)
            {
                case "영어" :
                    return 0;
                case "수학" :
                    return 1;
                case "개발" :
                    return 2;
                case "기타" :
                    return 3;
                default:
                    return 0;

            }
        } else {
            return 0;
        }
    }
    public static int checkChipForPlace(String str){

        if(str!=null){
            switch (str)
            {
                case "서울" :
                    return 0;
                case "경기" :
                    return 1;
                case "인천" :
                    return 2;
                case "기타" :
                    return 3;
                default:
                    return 0;

            }
        } else {
            return 0;
        }

    }

}
