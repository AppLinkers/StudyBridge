package com.example.studybridge.Mypage.MentoProfile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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

import com.example.studybridge.R;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.io.InputStream;

public class MyPageMentoProfileSchoolCheck extends AppCompatActivity {

    private Toolbar toolbar;

    private TextInputEditText schoolName,schoolMajor;
    private TextView uploadImg;
    private ImageView schoolImg;
    private LinearLayout Btn;

    public static final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_mentoprofile_school_check_activity);

        //툴바 설정
        toolbar = (Toolbar) findViewById(R.id.mypage_mentoProfile_school_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //학교 학과 명
        schoolName = (TextInputEditText) findViewById(R.id.mypage_mentoProfile_school_name);
        schoolMajor = (TextInputEditText) findViewById(R.id.mypage_mentoProfile_school_major);

        //이미지 올리기
        uploadImg = (TextView) findViewById(R.id.mento_profile_school_uploadBtn);
        schoolImg = (ImageView) findViewById(R.id.mypage_mentoProfile_school_img);

        uploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });

        //인증하기 버튼(완료)
        Btn = (LinearLayout) findViewById(R.id.mypage_mentoProfile_school_btn);



        Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder sb = new StringBuilder();
                sb.append(schoolName.getText()).append(" - ").append(schoolMajor.getText());
                String schoolResult = sb.toString();
                Intent intent = new Intent();
                intent.putExtra("schoolResult",schoolResult);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
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
                try {
                    InputStream in = getContentResolver().openInputStream(data.getData());


                    Bitmap img = BitmapFactory.decodeStream(in);

                    Bitmap rImg = rotateImage(data.getData(), img);
                    in.close();
                    schoolImg.setImageBitmap(rImg);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (resultCode == RESULT_CANCELED) {
                //취소할 경우 case
            }
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
