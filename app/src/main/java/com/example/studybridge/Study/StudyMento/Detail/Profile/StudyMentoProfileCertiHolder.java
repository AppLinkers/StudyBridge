package com.example.studybridge.Study.StudyMento.Detail.Profile;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.R;


public class StudyMentoProfileCertiHolder extends RecyclerView.ViewHolder{

    private TextView certiName;

    public StudyMentoProfileCertiHolder(@NonNull View itemView) {
        super(itemView);

        certiName = (TextView) itemView.findViewById(R.id.mento_profile_certi_name);

    }

    public void onBind(String data) {

        certiName.setText(data);
    }

}
