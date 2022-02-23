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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.R;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MyPageMentoProfileActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView name, school,goToCheck,imgCount;
    private TextInputEditText intro,nickName,curi,appeal;
    private MyPageMentoProfile mentoProfile;
    private Chip seoul,geongi,incheon,placeEtc;
    private Chip english,math,dev,subjectEtc;
    //지역 과목 선택 데이터
    private ArrayList<String> selectedPlace;
    private ArrayList<String> selectedSubject;
    //자격증 이미지 add
    private MaterialCardView addImg;
    public static final int PICK_IMAGE = 4;
    public static final int SCHOOL_CHECK = 201;
    private ArrayList<MyPageMentoProfile> arrayList;
    private MyPageMentoProfileAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    SharedPreferences sharedPreferences;
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_NAME = "user_name_key";
    private String userName;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_mentoprofile_activity);

        toolbar = (Toolbar) findViewById(R.id.mypage_mentoProfile_bar);

        //데이터
        name = (TextView) findViewById(R.id.mypage_mentoProfile_name); //기존 이름에서 받아와야함
        school = (TextView) findViewById(R.id.mypage_mentoProfile_school);
        intro = (TextInputEditText) findViewById(R.id.mypage_mentoProfile_intro);
        nickName = (TextInputEditText) findViewById(R.id.mypage_mentoProfile_nickName);
        curi = (TextInputEditText) findViewById(R.id.mypage_mentoProfile_curi);
        appeal = (TextInputEditText) findViewById(R.id.mypage_mentoProfile_appeal);

        //이름 데이터
        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userName = sharedPreferences.getString(USER_NAME, "사용자");

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
                Intent intent = new Intent(MyPageMentoProfileActivity.this,MyPageMentoProfileSchoolCheck.class);
                startActivityForResult(intent,SCHOOL_CHECK);
            }
        });

        //자격증 이미지 추가하기
        addImg = (MaterialCardView) findViewById(R.id.mypage_mentoProfile_addImg);
        imgCount = (TextView) findViewById(R.id.mypage_mentoProfile_imgCount);

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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
                //상단 완료버튼 눌렀을때
            case R.id.mentoProfile_complete_btn:
                //객체 생성
                mentoProfile = new MyPageMentoProfile(
                        userName,
                        selectedPlace.toString(),
                        selectedSubject.toString(),
                        school.getText().toString(),
                        intro.getText().toString(),
                        nickName.getText().toString(),
                        curi.getText().toString(),
                        appeal.getText().toString());

                Toast.makeText(getApplicationContext(),mentoProfile.getName()+mentoProfile.getSubject()+mentoProfile.getPlace()+mentoProfile.getIntro(),Toast.LENGTH_LONG).show();
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
                    school.setText(schoolResult);
                case PICK_IMAGE:
                    try {
                        InputStream in = getContentResolver().openInputStream(data.getData());


                        Bitmap img = BitmapFactory.decodeStream(in);

                        Bitmap rImg = rotateImage(data.getData(), img);
                        in.close();

                        MyPageMentoProfile profile = new MyPageMentoProfile();
                        profile.setQuliImg(rImg);
                        arrayList.add(profile);
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
}
