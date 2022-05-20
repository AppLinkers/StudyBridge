package com.example.studybridge.Login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.studybridge.AtuhActivity;
import com.example.studybridge.MainActivity;
import com.example.studybridge.R;
import com.example.studybridge.databinding.LoginActivityBinding;
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.userAuth.UserLoginReq;
import com.example.studybridge.http.dto.userAuth.UserLoginRes;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private LoginActivityBinding binding;

    DataService dataService = new DataService();
    Gson gson = new Gson();

    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_PK_ID_KEY = "user_pk_id_key";
    public static final String USER_ID_KEY = "user_id_key";
    public static final String USER_NAME = "user_name_key";

    SharedPreferences sharedPreferences;

    private long backBtnTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = LoginActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // initializing our shared preferences.
        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        // getting data from shared prefs and
        // storing it in our string variable.

        setUI();

    }

    private void setUI(){
        setBtn();
        setEditUI(binding.id,binding.idText,binding.idLine);
        setEditUI(binding.pw,binding.pwText,binding.pwLine);
    }

    private void setEditUI(EditText editText, TextView textView, View view){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().equals("")) {
                    view.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.viewUnderline));
                    textView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black30));
                }
                else{
                    view.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.textColorPrimary70));
                    textView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.textColorPrimary));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setBtn(){

        //로그인
        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        //회원가입
        binding.goSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoSignup();
            }
        });
    }

    private void signIn(){

        UserLoginReq userLoginReq = new UserLoginReq(binding.id.getText().toString().trim(), binding.pw.getText().toString().trim());

        // 로그인 서비스 실행
        dataService.userAuth.login(userLoginReq).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    UserLoginRes data = gson.fromJson(response.body().toString(), UserLoginRes.class);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putLong(USER_PK_ID_KEY, data.getId());
                    editor.putString(USER_ID_KEY, data.getLoginId());
                    editor.putString(USER_NAME, data.getName());
                    editor.apply();

                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                }else {
                    // 로그인 실패
                    Log.d("test", response.raw().message());
                    Toast.makeText(LoginActivity.this, "로그인 정보를 확인해주세요", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d("test", t.getMessage());
            }
        });
    }

    private void gotoSignup() {
        Intent i = new Intent(getApplicationContext(),SignUpFirst.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }

    @Override
    public void onBackPressed() {
        long curTime = System.currentTimeMillis();
        long gapTime = curTime - backBtnTime;

        if(0 <= gapTime && 2000 >= gapTime) {
            super.onBackPressed();
        }
        else {
            backBtnTime = curTime;
            Toast.makeText(this, "한번 더 누르면 종료됩니다.",Toast.LENGTH_SHORT).show();
        }

    }
}
