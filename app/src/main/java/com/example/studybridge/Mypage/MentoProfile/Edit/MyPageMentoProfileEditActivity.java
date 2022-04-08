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

/*    private List<String> certificatesImg;
    private List<String> certificatesName;*/

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
/*    private File schoolImg;*/
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
        setButton();

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


                //객체 생성
/*                mentoProfile = new MyPageMentoProfile(
                        userName,
                        selectedPlace,
                        selectedSubject,
                        school.getText().toString(),
                        intro.getText().toString(),
                        nickName.getText().toString(),
                        curi.getText().toString(),
                        experience.getText().toString(),
                        appeal.getText().toString(),
                        schoolImg,
                        certificatesImg,
                        certificatesName,
                        null
                );*/
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
                if (mentoProfile.getCertificates().size() > 0) {
                    mentoProfile.getCertificates().forEach(cn -> {
                        certificates.add(MultipartBody.Part.createFormData("certificates", cn.getCertificate()));
                    });
                }

                // school Img
                RequestBody schoolImg = RequestBody.create(MediaType.parse("multipart/form-data"), mentoProfile.getSchoolImg());
                MultipartBody.Part schoolImgReq = MultipartBody.Part.createFormData("schoolImg", mentoProfile.getSchool(), schoolImg);

                List<MultipartBody.Part> certificatesImgReq = new ArrayList<>();
                if (mentoProfile.getCertificates().size() > 0) {
                    mentoProfile.getCertificates().forEach(
                            certificatesImg -> {
                                RequestBody certificateImg = RequestBody.create(MediaType.parse("multipart/form-data"), certificatesImg.getImgUrl());
                                certificatesImgReq.add(MultipartBody.Part.createFormData("certificatesImg", certificatesImg.getCertificate(), certificateImg));
                            }
                    );
                }

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

    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            switch (requestCode){
                case SCHOOL_CHECK:
                    String schoolName = data.getStringExtra("schoolResult");
                    schoolDir = data.getStringExtra("schoolImg");
/*                    schoolImg = new File(dir);*/
                    school.setText(schoolName);

                case PICK_IMAGE:

                    String dir = String.valueOf(data.getData());

                    certificates.add(new Certificate(dir));

/*                    adapter.notifyDataSetChanged();//새로 고침*/

/*                    try {
                        InputStream in = getContentResolver().openInputStream(data.getData());

                        Bitmap img = BitmapFactory.decodeStream(in);

                        Bitmap rImg = rotateImage(data.getData(), img);
                        in.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }*/
            }
        } else if (resultCode == RESULT_CANCELED) {
            //취소할 경우 case
        }

    }


/*    //사진 돌아감 방지 메서드
    @RequiresApi(api = Build.VERSION_CODES.N)
    private Bitmap rotateImage(Uri uri, Bitmap bitmap) throws IOException {
        InputStream in = getContentResolver().openInputStream(uri);
        ExifInterface exif = new ExifInterface(in);
        in.close();

        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL);
        Matrix matrix = new Matrix();

        if (orientation == ExifInterface.ORIENTATION_ROTATE_90){
            matrix.postRotate(90);
        }
        else if (orientation == ExifInterface.ORIENTATION_ROTATE_180){
            matrix.postRotate(180);
        }
        else if (orientation == ExifInterface.ORIENTATION_ROTATE_270){
            matrix.postRotate(270);
        }

        return Bitmap.createBitmap(bitmap, 0,0,bitmap.getWidth(), bitmap.getHeight(), matrix, true);


    }*/
/*    //사진
    public String saveBitmapToJpg(Bitmap bitmap , String name) {

        File storage = getCacheDir(); //  path = /data/user/0/YOUR_PACKAGE_NAME/cache
        String fileName = name + ".jpg";
        File imgFile = new File(storage, fileName);
        try {
            imgFile.createNewFile();
            FileOutputStream out = new FileOutputStream(imgFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();
        } catch (FileNotFoundException e) {
            Log.e("saveBitmapToJpg","FileNotFoundException : " + e.getMessage());
        } catch (IOException e) {
            Log.e("saveBitmapToJpg","IOException : " + e.getMessage());
        }
        Log.d("imgPath" , getCacheDir() + "/" +fileName);
        return getCacheDir() + "/" +fileName;
    }*/

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
