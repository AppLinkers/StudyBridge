package com.example.studybridge.Mypage.MentoProfile.Show;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.R;
import com.example.studybridge.Util.ImgView;
import com.example.studybridge.http.dto.userMentor.Certificate;


public class MyPageMentoShowHolder extends RecyclerView.ViewHolder{

    private TextView certiName;
    private String imgUrl;

    public MyPageMentoShowHolder(@NonNull View itemView) {
        super(itemView);


        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), ImgView.class);

                intent.putExtra("certiImg",imgUrl);

                view.getContext().startActivity(intent);
            }
        });

    }

    public void onBind(Certificate data) {
        certiName.setText(data.getCertificate());
        imgUrl = data.getImgUrl();
    }

}
