package com.example.studybridge.Mypage.MentoProfile.Edit;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.studybridge.R;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.StringTokenizer;

public class MyPageMentoProfileSchoolCheck extends AppCompatActivity {

    private Toolbar toolbar;

    private TextInputEditText schoolName,schoolMajor;
    private TextView uploadImg;
    private ImageView schoolImg;
    private LinearLayout Btn;
    private File schoolFile;
    String dir;

    public static final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_mentoprofile_school_check_activity);

        //툴바 설정
        toolbar = (Toolbar) findViewById(R.id.mypage_mentoProfile_school_bar);
        //학교 학과 명
        schoolName = (TextInputEditText) findViewById(R.id.mypage_mentoProfile_school_name);
        schoolMajor = (TextInputEditText) findViewById(R.id.mypage_mentoProfile_school_major);
        //이미지 올리기
        uploadImg = (TextView) findViewById(R.id.mento_profile_school_uploadBtn);
        schoolImg = (ImageView) findViewById(R.id.mypage_mentoProfile_school_img);
        //인증하기 버튼(완료)
        Btn = (LinearLayout) findViewById(R.id.mypage_mentoProfile_school_btn);

        setToolbar();
        setIntent();
        setBtn();


    }

    private void setIntent(){
        Intent intent = getIntent();
        StringTokenizer st = new StringTokenizer(intent.getStringExtra("schoolName"),"-");
        schoolName.setText(st.nextToken());
        schoolMajor.setText(st.nextToken().trim());
        Glide.with(this).load(Uri.parse(intent.getStringExtra("schoolImg"))).into(schoolImg);
    }

    private void setBtn(){
        uploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });
        Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder sb = new StringBuilder();
                sb.append(schoolName.getText()).append(" - ").append(schoolMajor.getText());
                String schoolResult = sb.toString();
                Intent intent = new Intent();
                intent.putExtra("schoolResult",schoolResult);
                intent.putExtra("schoolImg",dir);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }
    private void setToolbar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    //뒤로 가기 설정
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //갤러리에서 이미지 받아오는 메서드
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            if (resultCode == RESULT_OK) {

                dir = uriPath(data.getData());
                Glide.with(this).load(dir).into(schoolImg);

            } else if (resultCode == RESULT_CANCELED) {
                //취소할 경우 case
            }
        }
    }

    private String uriPath(Uri uri){
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };

            assert uri != null;
            cursor = getContentResolver().query(uri, proj, null, null, null);

            assert cursor != null;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

            cursor.moveToFirst();

            return cursor.getString(column_index);

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


}
