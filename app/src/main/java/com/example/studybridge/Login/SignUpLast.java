package com.example.studybridge.Login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.example.studybridge.R;
import com.example.studybridge.Util.BackDialog;
import com.example.studybridge.databinding.SignupLastBinding;
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.userAuth.UserSignUpReq;
import com.example.studybridge.http.dto.userAuth.UserSignUpRes;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpLast extends AppCompatActivity {

    private SignupLastBinding binding;
    private String role,name,number,id,password;
    private boolean idOk=false;
    private DataService dataService = new DataService();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SignupLastBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        intentData();

        setId();
        setPassword();
        setRePassword();

        setBackBtn();

    }

    private void intentData(){
        Intent intent = getIntent();
        role = intent.getStringExtra("role");
        name = intent.getStringExtra("name");
        number = intent.getStringExtra("number");
    }

    private void setId(){
        binding.signupId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().equals("")){
                    binding.signupIdFind.setEnabled(false);
                    binding.signupIdLine.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.viewUnderline));
                }
                else {
                    binding.signupIdFind.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.textColorPrimary70));
                    binding.signupIdLine.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.textColorPrimary70));
                    binding.signupIdCV.setStrokeColor(ContextCompat.getColor(getApplicationContext(), R.color.textColorPrimary70));
                    binding.signupIdFind.setEnabled(true);
                    idCheck(binding.signupIdFind,charSequence.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void idCheck(TextView textView,String id){

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataService.userAuth.valid(id).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(response.isSuccessful()){//중복검사 통과
                            textView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.viewUnderline));
                            textView.setEnabled(false);
                            textView.setText("확인완료");
                            binding.signupIdLine.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.viewUnderline));
                            binding.signupIdCV.setStrokeColor(ContextCompat.getColor(getApplicationContext(), R.color.viewUnderline));
                            binding.signupIdErr.setVisibility(View.INVISIBLE);
                            binding.signupId.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.viewUnderline));
                            binding.signupId.setEnabled(false);
                            idOk = true;
                        }
                        else { //중복
                            binding.signupIdLine.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.redDark));
                            binding.signupIdErr.setVisibility(View.VISIBLE);
                            idOk = false;
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
            }
        });

    }

    private void setPassword(){
        binding.signupPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().equals("")){
                    binding.signupPasswordLine.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.viewUnderline));
                }
                else {
                    if(isValidPassword(charSequence.toString())){
                        binding.signupPasswordLine.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.textColorPrimary70));
                        binding.signupPasswordErr.setVisibility(View.INVISIBLE);
                    }
                    else {
                        binding.signupPasswordLine.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.redDark));
                        binding.signupPasswordErr.setVisibility(View.VISIBLE);
                        binding.signupPasswordErr.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.redDark));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    // 비밀번호 정규식
    private boolean isValidPassword(String password){
        if(password.length()>=4){
            return true;
        }
        else return false;
    }

    private void setRePassword(){
        binding.signupPasswordRe.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().equals("")){
                    binding.signupPasswordReLine.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.viewUnderline));
                }
                else {
                    if(charSequence.toString().equals(binding.signupPassword.getText().toString())){ //비밀번호 일치
                        binding.signupPasswordReLine.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.textColorPrimary70));
                        if(isValidPassword(binding.signupPassword.getText().toString())){
                            binding.signupConfirmBtn.setEnabled(true);
                            binding.signupConfirmBtn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.palletRed));
                            setBtn();
                        } else {
                            binding.signupConfirmBtn.setEnabled(false);
                            binding.signupConfirmBtn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.viewUnderline));
                        }

                    }
                    else { //일치 안함
                        binding.signupPasswordReLine.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.redDark));
                        binding.signupConfirmBtn.setEnabled(false);
                        binding.signupConfirmBtn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.viewUnderline));
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setBtn(){

        binding.signupConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(idOk &&isValidPassword(binding.signupPassword.getText().toString())){
                    signUpOk();
                } else if(!isValidPassword(binding.signupPassword.getText().toString())){
                    binding.signupPassword.requestFocus();
                }
                else {
                    Toast.makeText(SignUpLast.this, "idNo", Toast.LENGTH_SHORT).show();
                    binding.signupId.requestFocus();
                }
//                signUpOk();
            }
        });
    }

    private void signUpOk(){

        id = binding.signupId.getText().toString().trim();
        password = binding.signupPassword.getText().toString().trim();

        Toast.makeText(this, id+" "+password, Toast.LENGTH_SHORT).show();
        UserSignUpReq signUpReq = new UserSignUpReq(name,id,password,role,number,"MALE","서울");
        dataService.userAuth.signUp(signUpReq).enqueue(new Callback<UserSignUpRes>() {
            @Override
            public void onResponse(Call<UserSignUpRes> call, Response<UserSignUpRes> response) {
                if (response.isSuccessful()) {

                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "회원가입 완료!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserSignUpRes> call, Throwable t) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        backDialog();
    }
    private void setBackBtn(){
        binding.signupBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backDialog();
            }
        });
    }
    private void backDialog(){
        BackDialog dialog = BackDialog.getInstance(0);
        FragmentManager fm = getSupportFragmentManager();
        dialog.show(fm,"backDialog");

        dialog.setBackInterface(new BackDialog.BackInterface() {
            @Override
            public void okBtnClick() {
                finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });
    }
}
