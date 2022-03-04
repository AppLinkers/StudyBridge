package com.example.studybridge.Study.StudyMenti.Detail;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.R;
import com.example.studybridge.Study.StudyMento.Detail.StudyMentoDetail;

public class StudyMentiEnrollMentoHolder extends RecyclerView.ViewHolder{

    private ImageView enrollMentiImg;
    private TextView enrollMentiId;


    public StudyMentiEnrollMentoHolder(@NonNull View itemView) {
        super(itemView);

        enrollMentiImg = (ImageView) itemView.findViewById(R.id.enrollMento_img);
        enrollMentiId = (TextView) itemView.findViewById(R.id.enrollMento_tv);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), StudyMentoDetail.class);
                intent.putExtra("mentoId",enrollMentiId.getText());
                view.getContext().startActivity(intent);
            }
        });
    }

    public void onBind(String data) {

        enrollMentiId.setText(data);
    }
}
