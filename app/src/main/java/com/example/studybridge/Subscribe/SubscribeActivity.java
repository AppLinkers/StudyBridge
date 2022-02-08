package com.example.studybridge.Subscribe;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.studybridge.R;
import com.google.android.material.textfield.TextInputEditText;

public class SubscribeActivity extends AppCompatActivity {

    TextInputEditText coinNum;
    CheckBox checkBox;
    TextView buyList,totalPrice;
    int n=0;
    int coinP=0;
    String buyStr;
    String subStr="구독권 결제 + ";
    private int sum=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subscribe_activity);

        coinNum = (TextInputEditText) findViewById(R.id.subscribe_coinNum);
        checkBox = (CheckBox) findViewById(R.id.subscribe_checkBox);
        buyList = (TextView) findViewById(R.id.subscribe_buyList);
        totalPrice = (TextView) findViewById(R.id.subscribe_totalPrice);



        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    sum = 25000;
                    subStr="구독권 결제 + ";
                } else {
                    sum = 0;
                    subStr="";
                }
                n = sum + coinP;
                buyStr += subStr;
                String s = String.format("%d",n);
                totalPrice.setText(s);
            }
        });



        coinNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                coinP = 0;
                if(coinNum.getText().toString() == null || coinNum.getText().toString().equals("")){
                    coinP = 0;
                    return;
                } else {
                    coinP += Integer.parseInt(coinNum.getText().toString())*200;
                    n = coinP + sum;
                    String s = String.format("%d",n);
                    totalPrice.setText(s);
                }

            }

        });

    }


}
