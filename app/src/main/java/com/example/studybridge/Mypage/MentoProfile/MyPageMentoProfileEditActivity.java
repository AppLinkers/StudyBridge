package com.example.studybridge.Mypage.MentoProfile;

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
import android.widget.CompoundButton;
import android.widget.ImageView;
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
import com.example.studybridge.http.dto.ProfileRes;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
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

    private Toolbar toolbar;
    private TextView name, school,goToCheck;
    private TextInputEditText intro,nickName,curi,experience,appeal;
    private MyPageMentoProfile tempProfile,mentoProfile;
    private Chip seoul,geongi,incheon,placeEtc;
    private Chip english,math,dev,subjectEtc;
    //지역 과목 선택 데이터
    private ArrayList<String> selectedPlace;
    private ArrayList<String> selectedSubject;
    //자격증 이미지 add
    private MaterialCardView addImg;
    public static final int PICK_IMAGE = 101;
    public static final int SCHOOL_CHECK = 201;
    private ArrayList<MyPageMentoProfile> arrayList;
    private MyPageMentoProfileAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<Bitmap> imgArr;

    SharedPreferences sharedPreferences;
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_NAME = "user_name_key";
    public static final String USER_ID_KEY = "user_id_key";

    private String userName;
    private String userLoginId;

    private File schoolImg;
    private List<File> certificatesImg;
    private List<String> certificatesName;

    DataService dataService = new DataService();

    Gson gson = new Gson();

    ImageView iv;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_mentoprofile_edit_activity);

        toolbar = (Toolbar) findViewById(R.id.mypage_mentoProfile_bar);

        //데이터
        name = (TextView) findViewById(R.id.mypage_mentoProfile_name); //기존 이름에서 받아와야함
        school = (TextView) findViewById(R.id.mypage_mentoProfile_school);
        intro = (TextInputEditText) findViewById(R.id.mypage_mentoProfile_intro);
        nickName = (TextInputEditText) findViewById(R.id.mypage_mentoProfile_nickName);
        curi = (TextInputEditText) findViewById(R.id.mypage_mentoProfile_curi);
        experience = (TextInputEditText) findViewById(R.id.mypage_mentoProfile_experience);
        appeal = (TextInputEditText) findViewById(R.id.mypage_mentoProfile_appeal);

        //이름 데이터
        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userName = sharedPreferences.getString(USER_NAME, "사용자");
        userLoginId = sharedPreferences.getString(USER_ID_KEY, "userLoginId");

        name.setText(userName);


        //지역, 과목 선택

        //지역 chip
        seoul = (Chip) findViewById(R.id.mypage_mentoProfile_seoul);
        geongi = (Chip) findViewById(R.id.mypage_mentoProfile_geongi);
        incheon = (Chip) findViewById(R.id.mypage_mentoProfile_incheon);
        placeEtc = (Chip) findViewById(R.id.mypage_mentoProfile_placeEtc);

        //과목 chip
        english = (Chip) findViewById(R.id.mypage_mentoProfile_english);
        math = (Chip) findViewById(R.id.mypage_mentoProfile_math);
        dev = (Chip) findViewById(R.id.mypage_mentoProfile_dev);
        subjectEtc = (Chip) findViewById(R.id.mypage_mentoProfile_subjectEtc);

        selectedSubject = new ArrayList<>();
        selectedPlace = new ArrayList<>();

        CompoundButton.OnCheckedChangeListener checkedChangeListenerForSubject = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selectedSubject.add(buttonView.getText().toString());
                } else {
                    selectedSubject.remove(buttonView.getText().toString());
                }
            }
        };
        CompoundButton.OnCheckedChangeListener checkedChangeListenerForPlace = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selectedPlace.add(buttonView.getText().toString());
                } else {
                    selectedPlace.remove(buttonView.getText().toString());
                }
            }
        };

        english.setOnCheckedChangeListener(checkedChangeListenerForSubject);
        math.setOnCheckedChangeListener(checkedChangeListenerForSubject);
        dev.setOnCheckedChangeListener(checkedChangeListenerForSubject);
        subjectEtc.setOnCheckedChangeListener(checkedChangeListenerForSubject);
        seoul.setOnCheckedChangeListener(checkedChangeListenerForPlace);
        geongi.setOnCheckedChangeListener(checkedChangeListenerForPlace);
        incheon.setOnCheckedChangeListener(checkedChangeListenerForPlace);
        placeEtc.setOnCheckedChangeListener(checkedChangeListenerForPlace);


        //툴바 설정
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //학교 인증 가기
        goToCheck = (TextView) findViewById(R.id.mypage_mentoProfile_school_goToCheck);
        goToCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyPageMentoProfileEditActivity.this,MyPageMentoProfileSchoolCheck.class);
                startActivityForResult(intent,SCHOOL_CHECK);
            }
        });

        //자격증 이미지 추가하기
        addImg = (MaterialCardView) findViewById(R.id.mypage_mentoProfile_addImg);

        recyclerView = (RecyclerView) findViewById(R.id.mypage_mentoProfile_rcView);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        arrayList = new ArrayList<>();
        adapter = new MyPageMentoProfileAdapter(arrayList);
        recyclerView.setAdapter(adapter);

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

                certificatesImg = new ArrayList<>();
                certificatesName = new ArrayList<>();
                File test = new File("/data/user/0/com.example.studybridge/cache/certificateImg1.jpg");
                for(int i=0; i<arrayList.size();i++){
                    String dir = saveBitmapToJpg(arrayList.get(i).getQualiImg(), String.format("certificateImg%d", i));
                    File imgFile = new File(dir);
                    certificatesImg.add(imgFile);
                    certificatesName.add(arrayList.get(i).getQualiName());
                }

                //객체 생성
                mentoProfile = new MyPageMentoProfile(
                        userName,
                        selectedPlace.toString(),
                        selectedSubject.toString(),
                        school.getText().toString(),
                        intro.getText().toString(),
                        nickName.getText().toString(),
                        curi.getText().toString(),
                        experience.getText().toString(),
                        appeal.getText().toString(),
                        schoolImg,
                        certificatesImg,
                        certificatesName
                );

                Map<String, RequestBody> profileReq = new HashMap<>();

                profileReq.put("profileTextReq.userLoginId", RequestBody.create(MultipartBody.FORM, userLoginId));
                profileReq.put("profileTextReq.location", RequestBody.create(MultipartBody.FORM, mentoProfile.getPlace()));
                profileReq.put("profileTextReq.info", RequestBody.create(MultipartBody.FORM, mentoProfile.getIntro()));
                profileReq.put("profileTextReq.nickName", RequestBody.create(MultipartBody.FORM, mentoProfile.getNickName()));
                profileReq.put("profileTextReq.subject", RequestBody.create(MultipartBody.FORM, mentoProfile.getSubject()));
                profileReq.put("profileTextReq.school", RequestBody.create(MultipartBody.FORM, mentoProfile.getSchool()));
                profileReq.put("profileTextReq.experience", RequestBody.create(MultipartBody.FORM, mentoProfile.getExpeience()));
                profileReq.put("profileTextReq.curriculum", RequestBody.create(MultipartBody.FORM, mentoProfile.getCuri()));
                profileReq.put("profileTextReq.appeal", RequestBody.create(MultipartBody.FORM, mentoProfile.getAppeal()));

                // school Img
                RequestBody schoolImg = RequestBody.create(MediaType.parse("multipart/form-data"), mentoProfile.getSchoolImg());
                MultipartBody.Part schoolImgReq = MultipartBody.Part.createFormData("schoolImg", mentoProfile.getSchoolImg().getName(), schoolImg);

                List<MultipartBody.Part> certificatesImgReq = new ArrayList<>();
                if (mentoProfile.getCertificateImg().size() > 0) {
                    mentoProfile.getCertificateImg().forEach(
                            certificatesImg -> {
                                RequestBody certificateImg = RequestBody.create(MediaType.parse("multipart/form-data"), certificatesImg);
                                certificatesImgReq.add(MultipartBody.Part.createFormData("certificatesImg", certificatesImg.getName(), certificateImg));
                            }
                    );
                }

                dataService.userMentor.profile(schoolImgReq,certificatesImgReq,profileReq).enqueue(new Callback<ProfileRes>() {
                    @Override
                    public void onResponse(Call<ProfileRes> call, Response<ProfileRes> response) {
                        if (response.isSuccessful()) {
                            Log.d("test", response.body().toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<ProfileRes> call, Throwable t) {
                        Log.d("test", t.toString());
                    }
                });


                Toast.makeText(getApplicationContext(),certificatesName.get(0),Toast.LENGTH_LONG).show();
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
                    String schoolResult = data.getStringExtra("schoolResult");
                    String dir = data.getStringExtra("schoolImg");
                    schoolImg = new File(dir);
                    school.setText(schoolResult);
                case PICK_IMAGE:
                    try {
                        InputStream in = getContentResolver().openInputStream(data.getData());


                        Bitmap img = BitmapFactory.decodeStream(in);

                        Bitmap rImg = rotateImage(data.getData(), img);
                        in.close();

                        tempProfile = new MyPageMentoProfile();
                        tempProfile.setQualiImg(rImg);
                        arrayList.add(tempProfile);
                        adapter.notifyDataSetChanged();//새로 고침




                    } catch (Exception e) {
                        e.printStackTrace();
                    }

            }
        } else if (resultCode == RESULT_CANCELED) {
            //취소할 경우 case
        }

    }


    //사진 돌아감 방지 메서드
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


    }


    public String saveBitmapToJpg(Bitmap bitmap , String name) {
        /**
         * 캐시 디렉토리에 비트맵을 이미지파일로 저장하는 코드입니다.
         *
         * @version target API 28 ★ API29이상은 테스트 하지않았습니다.★
         * @param Bitmap bitmap - 저장하고자 하는 이미지의 비트맵
         * @param String fileName - 저장하고자 하는 이미지의 비트맵
         *
         * File storage = 저장이 될 저장소 위치
         *
         * return = 저장된 이미지의 경로
         *
         * 비트맵에 사용될 스토리지와 이름을 지정하고 이미지파일을 생성합니다.
         * FileOutputStream으로 이미지파일에 비트맵을 추가해줍니다.
         */

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
    }

}
