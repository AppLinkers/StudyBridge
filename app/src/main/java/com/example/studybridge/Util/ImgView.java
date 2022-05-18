package com.example.studybridge.Util;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.studybridge.R;
import com.example.studybridge.databinding.ImgActivityBinding;

public class ImgView extends AppCompatActivity {

    private ImgActivityBinding binding;

    private String imgUrl;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ImgActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.black30));

        Intent intent = getIntent();
        imgUrl = intent.getStringExtra("img");

        Glide.with(this).load(imgUrl).into(binding.img);
    }
}
