package com.example.studybridge.Login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.example.studybridge.R;
import com.example.studybridge.Util.BackDialog;
import com.example.studybridge.databinding.SignupSecondBinding;
import com.example.studybridge.http.DataService;
import com.google.android.material.card.MaterialCardView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpSecond extends AppCompatActivity {

    private SignupSecondBinding binding;
    private String role;
    private DataService dataService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SignupSecondBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        intentData();
        editData(binding.signupName,binding.signupNameLine,null,null);
        editData(binding.signupPhone,binding.signupPhoneLine,binding.signupPhoneAuthCV,binding.signupPhoneAuth);
        editSms(binding.smsCheck,binding.smsLine,binding.smsCheckCV,binding.smsCheckBtn);
//        editName(binding.signupName,binding.signupNameLine);

        setBackBtn();
    }

    private void intentData(){
        Intent intent = getIntent();
        role = intent.getStringExtra("role");
    }

/*    private void editName(EditText editText, View view){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().equals("")){
                    view.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.viewUnderline));
                    binding.signupNextBtn.setEnabled(true);
                    binding.signupNextBtn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.viewUnderline));
                }
                else {
                    view.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.textColorPrimary70));
//                    setNext();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }*/
    private void editData(EditText editText, View view, MaterialCardView cardView, TextView textView){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().equals("")){
                    view.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.viewUnderline));
                    if(cardView !=null && textView != null){
                        textView.setEnabled(false);
                    }
                }
                else {
                    view.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.textColorPrimary70));
                    if(cardView !=null && textView != null){
                        setAuthBtn(textView,cardView,charSequence.toString());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    private void editSms(EditText editText, View view, MaterialCardView cardView, TextView textView){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().equals("")){
                    view.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.viewUnderline));
                    if(cardView !=null && textView != null){
                        textView.setEnabled(false);
                    }
                }
                else {
                    view.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.textColorPrimary70));
                    if(cardView !=null && textView != null){
                        setSmsBtn(textView,cardView,charSequence.toString());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setAuthBtn(TextView textView,MaterialCardView cardView,String phoneNum){

        if(isValidCellPhoneNumber(phoneNum)){
            textView.setEnabled(true); // 번호형식 조건
            cardView.setStrokeColor(ContextCompat.getColor(getApplicationContext(),R.color.textColorPrimary70));
            textView.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.textColorPrimary70));
        } else {
            textView.setEnabled(false);
            cardView.setStrokeColor(ContextCompat.getColor(getApplicationContext(),R.color.viewUnderline));
            textView.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.viewUnderline));
        }

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNext();
            }
        });

    }
    private void setSmsBtn(TextView textView,MaterialCardView cardView,String smsNum){

        //인증번호 형식 '4자리'
        if(smsNum.length() == 4){
            textView.setEnabled(true);
            cardView.setStrokeColor(ContextCompat.getColor(getApplicationContext(),R.color.textColorPrimary70));
            textView.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.textColorPrimary70));
        } else {
            textView.setEnabled(false);
            cardView.setStrokeColor(ContextCompat.getColor(getApplicationContext(),R.color.viewUnderline));
            textView.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.viewUnderline));
        }

        ////////////////인증 번호와 동일한지 여기서 확인
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextBtn();
            }
        });

    }
    private void setNext(){

        dataService = new DataService();
        dataService.sms.sendSMS(binding.signupPhone.getText().toString()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    Toast.makeText(SignUpSecond.this, "send success", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

        binding.signupPhone.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.viewUnderline));
        binding.signupPhoneLine.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.viewUnderline));
        binding.signupPhoneAuthCV.setStrokeColor(ContextCompat.getColor(getApplicationContext(),R.color.viewUnderline));
        binding.signupPhoneAuth.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.viewUnderline));
        binding.signupPhoneAuth.setText("재전송");

        binding.smsCheckCon.setVisibility(View.VISIBLE);

    }
    private void nextBtn(){

        binding.signupPhone.setEnabled(false);
        binding.signupPhoneAuth.setEnabled(false);

        binding.smsCheck.setEnabled(false);
        binding.smsCheckBtn.setEnabled(false);
        binding.smsCheckBtn.setText("확인 완료");
        binding.smsCheckCV.setStrokeColor(ContextCompat.getColor(getApplicationContext(),R.color.viewUnderline));
        binding.smsLine.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.viewUnderline));
        binding.smsCheckBtn.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.viewUnderline));

        binding.signupNextBtn.setEnabled(true);
        binding.signupNextBtn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.palletRed));
        binding.signupNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!binding.signupName.getText().toString().equals("")){
                    Intent intent = new Intent(getApplicationContext(),SignUpLast.class);
                    intent.putExtra("role",role);
                    intent.putExtra("name",binding.signupName.getText().toString().trim());
                    intent.putExtra("number","01000000000");
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                } else{
                    binding.signupName.requestFocus();
                }
            }
        });
    }
    public static boolean isValidCellPhoneNumber(String cellphoneNumber) {

        boolean returnValue = false;
        try {
            String regex = "^\\s*(010|011|016|017|018|019)(-|\\)|\\s)*(\\d{3,4})(-|\\s)*(\\d{4})\\s*$";

            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(cellphoneNumber);
            if (m.matches()) {
                returnValue = true;
            }

            if (returnValue && cellphoneNumber != null
                    && cellphoneNumber.length() > 0
                    && cellphoneNumber.startsWith("010")) {
                cellphoneNumber = cellphoneNumber.replace("-", "");
                if (cellphoneNumber.length() != 11) {
                    returnValue = false;
                }
            }
            return returnValue;
        } catch (Exception e) {
            return false;
        }

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
