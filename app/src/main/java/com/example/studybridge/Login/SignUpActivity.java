package com.example.studybridge.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studybridge.R;
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.UserSignUpReq;
import com.example.studybridge.http.dto.UserSignUpRes;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    EditText signupName;
    EditText signupId;
    EditText signupPw;
    EditText signupPwCheck;
    EditText signupPhone;

    TextView pwCheck;
    RadioGroup signupMentRg;
    RadioGroup signupGender;

    String signupPwStr;
    String id;
    String name;
    String phone;
    String region;
    String checkedId = "";

    Spinner regionSpnr;

    boolean idCheck =  false;

    private DataService dataService;



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
        signupName = findViewById(R.id.signup_name);


        signupMentRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.signup_mentor:
                        break;
                    case R.id.signup_menti:
                        break;
                }
            }
        });

        signupGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.signup_male:
                        break;
                    case R.id.signup_female:
                        break;
                }
            }
        });


    }

    public void signup(View view) {


        if(!signupPw.getText().toString().equals(signupPwCheck.getText().toString())) {
            pwCheck.setText("비밀번호가 다릅니다 ");
            return;
        }else{
            pwCheck.setText(" ");
            signupPwStr = signupPw.getText().toString();
        }
        RadioButton mentRad =  findViewById(signupMentRg.getCheckedRadioButtonId());
        RadioButton genderRad = findViewById(signupGender.getCheckedRadioButtonId());


        id = signupId.getText().toString();
        name = signupName.getText().toString();
        phone = signupPhone.getText().toString();
        region = regionSpnr.getSelectedItem().toString();
        String role= mentRad.getText().toString().toUpperCase();
        String gender = genderRad.getText().toString().toUpperCase();

        String total = "type: "+ role + " Name: "+ name  +" id: "+id+" pw: "+signupPwStr+" phone: "+phone+" region: "+region+ " gender: "+gender;

        UserSignUpReq userSignUpReq = new UserSignUpReq(name, id, signupPwStr, role, phone,gender,region);

        if (checkedId.equals(id)) {
            dataService.userAuth.signUp(userSignUpReq).enqueue(new Callback<UserSignUpRes>() {
                @Override
                public void onResponse(Call<UserSignUpRes> call, Response<UserSignUpRes> response) {
                    if (response.isSuccessful()) {
                        // 회원가입 성공
                    } else {
                        // 실패
                    }
                }

                @Override
                public void onFailure(Call<UserSignUpRes> call, Throwable t) {

                }
            });
        } else {
            Toast.makeText(view.getContext(), "아이디 중복 확인을 해 주세요", Toast.LENGTH_SHORT).show();
        }
    }

    public void idCheck(View view) {
        //중복확인
        dataService.userAuth.valid(signupId.getText().toString()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    idCheck = true;
                    checkedId = signupId.getText().toString();
                } else {
                    idCheck = false;
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });


    }

    public void phoneCheck(View view) {

    }
}