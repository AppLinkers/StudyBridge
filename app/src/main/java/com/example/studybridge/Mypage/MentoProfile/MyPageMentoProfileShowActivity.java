package com.example.studybridge.Mypage.MentoProfile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.studybridge.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MyPageMentoProfileShowActivity extends AppCompatActivity {

    private FloatingActionButton editBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_mentoprofile_show_activity);

        editBtn = (FloatingActionButton) findViewById(R.id.mypage_mentoShow_editBtn);

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MyPageMentoProfileEditActivity.class);
                startActivity(intent);
            }
        });
    }
}
