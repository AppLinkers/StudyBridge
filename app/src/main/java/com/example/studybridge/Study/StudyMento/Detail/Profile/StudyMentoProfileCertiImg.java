package com.example.studybridge.Study.StudyMento.Detail.Profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.studybridge.R;

public class StudyMentoProfileCertiImg extends AppCompatActivity {

    private ImageView imageView;
    private String imgUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.study_mento_profile_certi_img);

        imageView = (ImageView) findViewById(R.id.mento_profile_certi_img);

        Intent intent = getIntent();
        imgUrl = intent.getStringExtra("certiImg");

        Toast.makeText(this, imgUrl, Toast.LENGTH_SHORT).show();

        Glide.with(this).load(imgUrl).into(imageView);
    }
}
