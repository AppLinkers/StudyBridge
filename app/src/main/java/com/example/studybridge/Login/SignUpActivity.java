package com.example.studybridge.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studybridge.R;
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.UserLoginReq;
import com.example.studybridge.http.dto.UserSignUpReq;
import com.example.studybridge.http.dto.UserSignUpRes;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.sql.SQLOutput;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity implements TextView.OnEditorActionListener {

    private TextInputEditText signupName,signupId,signupPw,signupPwCheck,signupPhone;
    private TextInputLayout pwCheckM;
    private ChipGroup signupPlace;
    private RadioGroup signupMentRg,signupGender;
    TextView pwCheck;

    //data
    String role;
    String name;
    String phone;
    String gender;
    String region;
    String id;

    String signupPwStr;

    String checkedId = "";
    RadioButton genderRad;
    RadioButton mentRad;


    boolean idCheck =  true;

    private DataService dataService;
    Gson gson;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        dataService = new DataService();
        gson = new Gson();



        //input 데이터
        signupName = (TextInputEditText) findViewById(R.id.signup_name);
        signupPhone = (TextInputEditText)findViewById(R.id.signup_phone);
        signupId = (TextInputEditText)findViewById(R.id.signup_id);
        signupPw = (TextInputEditText)findViewById(R.id.signup_pw);
        signupPwCheck = (TextInputEditText)findViewById(R.id.signup_pw_check);




        //비밀번호 체크 레이아웃 (에러메세지 용)
        pwCheckM = (TextInputLayout) findViewById(R.id.signup_pw_check_layout);





        //라디오 그룹 (멘토/멘티, 성별)
        signupMentRg = findViewById(R.id.signup_type);
        signupGender = findViewById(R.id.signup_gender);


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







        //지역 선택 chip
        signupPlace = (ChipGroup) findViewById(R.id.signup_place);
        for(int i=0; i<signupPlace.getChildCount();i++){
            Chip chip = (Chip) signupPlace.getChildAt(i);
            if(chip.isChecked()){
                region = chip.getText().toString();
            }
        }




    }




    public void signup(View view) {


//         비밀번호 8자리
//        if(signupPw.getText().toString().length()<8) {
//            pwCheckM.setError(getString(R.string.error_password));
//            return;
//        } else{
//            pwCheckM.setError(null);
//            signupPwStr = signupPw.getText().toString();
//        }

        mentRad =  findViewById(signupMentRg.getCheckedRadioButtonId());
        genderRad = findViewById(signupGender.getCheckedRadioButtonId());



        id = signupId.getText().toString();
        name = signupName.getText().toString();
        phone = signupPhone.getText().toString();
        role= mentRad.getText()+"".toUpperCase();
        gender = genderRad.getText()+"".toUpperCase();

        String total = "type: "+ role + " Name: "+ name  +" id: "+id+" pw: "+signupPwStr+" phone: "+phone+" region: "+region+ " gender: "+gender;
        System.out.println(total);

        UserSignUpReq userSignUpReq = new UserSignUpReq(name, id, signupPwStr, role, phone,gender,region);
        UserLoginReq usl = new UserLoginReq(id,role);
        Toast.makeText(getApplicationContext(),usl.toString(),Toast.LENGTH_SHORT).show();



//            dataService.userAuth.signUp(userSignUpReq).enqueue(new Callback<UserSignUpRes>() {
//                @Override
//                public void onResponse(Call<UserSignUpRes> call, Response<UserSignUpRes> response) {
//                    if (response.isSuccessful()) {
//
//                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                        startActivity(intent);
//                    } else {
//                        Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<UserSignUpRes> call, Throwable t) {
//                    Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
//                }
//            });

    }

    public void idCheck(View view) {
        //중복확인

        dataService.userAuth.valid(signupId.getText().toString()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    idCheck = true;
                    Toast.makeText(getApplicationContext(), "중복확인완료"+response.body().toString(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "중복된 아이디입니다", Toast.LENGTH_SHORT).show();
                    idCheck = false;
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {


            }
        });


    }

    public void phoneCheck(View view) {
        //본인인증

    }

    //완료버튼 클릭시 이벤트
    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if(textView.getId() == R.id.signup_pw_check && i == EditorInfo.IME_ACTION_DONE){

            if(!signupPw.getText().toString().equals(signupPwCheck.getText().toString())) {
                pwCheckM.setError(getString(R.string.error_password));
            } else{
                pwCheckM.setError(null);
                signupPwStr = signupPw.getText().toString();
            }
        }
        return false;
    }




}