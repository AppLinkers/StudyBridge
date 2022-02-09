package com.example.studybridge.Subscribe;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.R;
import com.example.studybridge.ToDo.ToDo;
import com.example.studybridge.ToDo.ToDoAdapter;

import java.util.ArrayList;

public class PaymentActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PaymentAdapter adapter;
    ArrayList<Payment> paymentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        recyclerView = findViewById(R.id.payment_Rc);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new PaymentAdapter();
        Intent intent = getIntent();
        String newD = intent.getExtras().getString("payDetail");
        String newC = "총 "+intent.getExtras().getString("payCost")+"원 ";
        Payment newP = new Payment("rio319",newD,newC,"결제 확인중");
        adapter.addItem(newP);
        getData();
        recyclerView.setAdapter(adapter);
    }

    public void getData(){
        Payment p1 = new Payment("rio319","구독권 / 코인10개", "총 27000원", "결제 확인중 ");
        Payment p2 = new Payment("rio319","코인 20개", "총 4000원", "결제 확인완료");
        Payment p3 = new Payment("jsw7027","코인 20개", "총 4000원", "결제 확인완료");

        paymentList.add(p1);
        paymentList.add(p2);
        paymentList.add(p3);

        for(Payment p : paymentList){
            if(p.getUser().equals("rio319")){
                adapter.addItem(p);
            }
        }

    }
}