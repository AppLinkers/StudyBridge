package com.example.studybridge.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studybridge.R;

public class SignUpActivity extends AppCompatActivity {

    EditText signupId;
    EditText signupPw;
    EditText signupPwCheck;
    EditText signupPhone;

    TextView pwCheck;
    RadioGroup signupMentRg;
    RadioGroup signupGender;

    String signupPwStr;
    String id;
    String phone;
    String region;

    Spinner regionSpnr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        String[] items = {"서울","경기남부","경기북부","인천","기타"};
        regionSpnr = findViewById(R.id.region_spnr);


        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        regionSpnr.setAdapter(spinnerArrayAdapter);

        signupMentRg = findViewById(R.id.signup_type);
        signupGender = findViewById(R.id.signup_gender);

        pwCheck = findViewById(R.id.pw_check_str);

        signupId = findViewById(R.id.signup_id);
        signupPhone = findViewById(R.id.signup_phone);
        signupPw = findViewById(R.id.signup_pw);
        signupPwCheck = findViewById(R.id.signup_pw_check);

        signupPwStr = signupPw.getText().toString();

        if(!signupPwStr.equals(signupPwCheck.getText())) {
            pwCheck.setText("비밀번호가 다릅니다 ");
        }else{
            pwCheck.setText(" ");
        }

        id = signupId.toString();
        phone = signupPhone.toString();
        region = regionSpnr.getSelectedItem().toString();





    }

    public void signup(View view) {
        Toast.makeText(getApplicationContext(), region, Toast.LENGTH_SHORT).show();

    }
}