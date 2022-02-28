package com.example.studybridge.Login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.studybridge.AtuhActivity;
import com.example.studybridge.MainActivity;
import com.example.studybridge.R;
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.ErrorMessage;
import com.example.studybridge.http.dto.UserLoginReq;
import com.example.studybridge.http.dto.UserLoginRes;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {


    DataService dataService = new DataService();
    Gson gson = new Gson();


    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_ID_KEY = "user_id_key";
    public static final String USER_NAME = "user_name_key";

    SharedPreferences sharedPreferences;
    String user_login_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        // initializing our shared preferences.
        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        // getting data from shared prefs and
        // storing it in our string variable.

        final TextInputEditText loginIdEditText = findViewById(R.id.id_edit_text);
        final TextInputLayout passwordTextInput = findViewById(R.id.password_text_input);
        final TextInputEditText passwordEditText = findViewById(R.id.password_edit_text);
        MaterialButton logInButton = findViewById(R.id.login_button);
        MaterialButton signUpButton = findViewById(R.id.signUp_button);

        // Set an error if the password is less than 8 characters.
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isPasswordValid(passwordEditText.getText())) {
                    passwordTextInput.setError(getString(R.string.error_password));
                } else {
                    passwordTextInput.setError(null); // Clear the error



                    UserLoginReq userLoginReq = new UserLoginReq(loginIdEditText.getText().toString(), passwordEditText.getText().toString());



                    // 로그인 서비스 실행
                    dataService.userAuth.login(userLoginReq).enqueue(new Callback<Object>() {
                        @Override
                        public void onResponse(Call<Object> call, Response<Object> response) {
                            if (response.isSuccessful()) {
                                UserLoginRes data = gson.fromJson(response.body().toString(), UserLoginRes.class);
                                SharedPreferences.Editor editor = sharedPreferences.edit();

                                editor.putString(USER_ID_KEY, data.getLoginId());
                                editor.putString(USER_NAME, data.getName());
                                editor.apply();

                                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(intent);
                            }else {
                                // 로그인 실패
                                Log.d("test", response.raw().message());
                            }

                        }

                        @Override
                        public void onFailure(Call<Object> call, Throwable t) {
                            Log.d("test", t.getMessage());
                        }
                    });


                }
            }
        });

        // Clear the error once more than 8 characters are typed.
        passwordEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (isPasswordValid(passwordEditText.getText())) {
                    passwordTextInput.setError(null); //Clear the error
                }
                return false;
            }
        });
    }


    /*
        In reality, this will have more complex logic including, but not limited to, actual
        authentication of the username and password.
     */
    //비밀번호 체크 함수
    private boolean isPasswordValid(@Nullable Editable text) {
        return text != null && text.length() >= 4;
    }

    public void confirm(View view) {
        Intent intent = new Intent(getApplicationContext(), AtuhActivity.class);
        startActivity(intent);
    }

    public void gotoSignup(View view) {
        Intent i = new Intent(getApplicationContext(),SignUpActivity.class);
        startActivity(i);
    }
}
