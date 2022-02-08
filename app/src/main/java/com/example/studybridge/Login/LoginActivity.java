package com.example.studybridge.Login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.studybridge.AtuhActivity;
import com.example.studybridge.MainActivity;
import com.example.studybridge.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

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
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
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
}
