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
    private int n=0;
    private int coinP=0;
    private int sum=0;

    String subStr="";
    String coinStr="";
    String allStr="";

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
                    subStr = "구독권/";
                } else {
                    sum = 0;
                    subStr = "";
                }
                n = sum + coinP;
                String s = String.format("%d",n);
                totalPrice.setText(s);

                allStr = subStr + coinStr;
                buyList.setText(allStr);

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
                coinStr = "";
                if(coinNum.getText() == null || coinNum.getText().toString().equals("")){
                    coinP = 0;
                    coinStr="";
                } else {
                    coinP += Integer.parseInt(coinNum.getText().toString())*200;
                    n = coinP + sum;
                    coinStr= "코인 " + (coinP/200) + "개/";

                    String s = String.format("%d",n);
                    totalPrice.setText(s);

                }

                allStr = subStr + coinStr;
                buyList.setText(allStr);
            }

        });

    }


}
