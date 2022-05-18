package com.example.studybridge.Study.StudyMento.Detail;

import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.Util.ImgView;
import com.example.studybridge.databinding.MentorCertiItemBinding;
import com.example.studybridge.http.dto.userMentor.Certificate;


public class CertiHolder extends RecyclerView.ViewHolder{

    private MentorCertiItemBinding binding;
    private String imgUrl;

    public CertiHolder(@NonNull View itemView) {
        super(itemView);
        binding = MentorCertiItemBinding.bind(itemView);


        binding.showCerti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ImgView.class);

                intent.putExtra("img",imgUrl);

                view.getContext().startActivity(intent);
            }
        });

    }

    public void onBind(Certificate data) {
        binding.certiName.setText(data.getCertificate());
        imgUrl = data.getImgUrl();
    }

}
