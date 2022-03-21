package com.example.studybridge.Study.StudyMenti.Detail;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.R;
import com.example.studybridge.Study.StudyMento.Detail.StudyMentoDetail;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class StudyMentiEnrollMentoAdapter extends RecyclerView.Adapter<StudyMentiEnrollMentoAdapter.StudyMentiEnrollMentoHolder>{

    private ArrayList<String> listData = new ArrayList<>();
    public static final int selectMento = 121;
    private Long studyId;
    private String managerId;



    @NonNull
    @NotNull
    @Override
    public StudyMentiEnrollMentoAdapter.StudyMentiEnrollMentoHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.study_menti_detail_enrollmento_item, parent, false);
        return new StudyMentiEnrollMentoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull StudyMentiEnrollMentoAdapter.StudyMentiEnrollMentoHolder holder, int position) {

        holder.enrollMentiId.setText(listData.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), StudyMentoDetail.class);
                intent.putExtra("mentoId",holder.enrollMentiId.getText());
                intent.putExtra("studyId",studyId.longValue());
                intent.putExtra("managerId",managerId);
                ((Activity) view.getContext()).startActivityForResult(intent,selectMento);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void addItem(String data) {
        listData.add(data);
    }

    public void setStudyId(Long studyId) {
        this.studyId = studyId;
    }
    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public class StudyMentiEnrollMentoHolder extends RecyclerView.ViewHolder{

        private ImageView enrollMentiImg;
        private TextView enrollMentiId;

        public StudyMentiEnrollMentoHolder(@NonNull View itemView) {
            super(itemView);
            this.enrollMentiImg = (ImageView) itemView.findViewById(R.id.enrollMento_img);
            this.enrollMentiId = (TextView) itemView.findViewById(R.id.enrollMento_tv);
        }
    }


}
