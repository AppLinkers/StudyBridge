package com.example.studybridge.Subscribe;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.R;

public class PaymentHolder extends RecyclerView.ViewHolder {

    TextView paymentDetail;
    TextView paymentCost;
    TextView paymentStatus;

    public PaymentHolder(@NonNull View itemView) {
        super(itemView);

        paymentDetail = itemView.findViewById(R.id.paymentDetail);
        paymentCost = itemView.findViewById(R.id.paymentCost);
        paymentStatus = itemView.findViewById(R.id.paymentStatus);

    }

    public void onBind(Payment p){
        paymentDetail.setText(p.getPaymentDetail());
        paymentStatus.setText(p.getPaymentStatus());
        paymentCost.setText(p.getPaymentCost());
    }
}
