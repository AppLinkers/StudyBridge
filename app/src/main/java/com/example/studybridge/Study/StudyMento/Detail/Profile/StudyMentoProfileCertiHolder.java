package com.example.studybridge.Study.StudyMento.Detail.Profile;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.R;
import com.example.studybridge.http.dto.userMentor.Certificate;


public class StudyMentoProfileCertiHolder extends RecyclerView.ViewHolder{

    private TextView certiName;
    private String imgUrl;

    public StudyMentoProfileCertiHolder(@NonNull View itemView) {
        super(itemView);

        certiName = (TextView) itemView.findViewById(R.id.mento_profile_certi_name);


        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),StudyMentoProfileCertiImg.class);

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
