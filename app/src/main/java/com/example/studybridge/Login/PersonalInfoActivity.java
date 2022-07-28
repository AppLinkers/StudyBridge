package com.example.studybridge.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebViewClient;


import com.example.studybridge.R;
import com.example.studybridge.databinding.ActivityPersonalInfoBinding;

public class PersonalInfoActivity extends AppCompatActivity {

    private ActivityPersonalInfoBinding binding;
    public static final String PERSONAL_PDF = "https://study-bridge.s3.us-east-2.amazonaws.com/personalInfo.pdf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPersonalInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setUI();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setUI() {

        //pdf viewer
        binding.pdfView.setWebViewClient(new WebViewClient());
        binding.pdfView.getSettings().setJavaScriptEnabled(true);
        binding.pdfView.loadUrl("https://drive.google.com/viewerng/viewer?embedded=true&url="+PERSONAL_PDF);
        //뒤로가기
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}