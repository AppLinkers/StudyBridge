package com.example.studybridge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class AtuhActivity extends AppCompatActivity {

    WebView authView;
    JHandler jHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atuh2);
        authView = findViewById(R.id.activity_auth_webView);

        authView.getSettings().setJavaScriptEnabled(true);
        authView.getSettings().setBuiltInZoomControls(true);
        authView.getSettings().setLoadWithOverviewMode(true);
        authView.getSettings().setDefaultTextEncodingName("UTF-8");

        authView.setWebChromeClient(new WebChromeClient());
        authView.setWebViewClient(new WebViewClient());
        authView.addJavascriptInterface(jHandler,"Android");
        authView.loadUrl("file:///android_asset/test.html");


    }

    public void backToMain(View view) {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }
}