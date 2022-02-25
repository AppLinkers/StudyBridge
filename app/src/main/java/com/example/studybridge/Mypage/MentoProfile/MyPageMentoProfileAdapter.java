package com.example.studybridge.Mypage.MentoProfile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.R;
import com.example.studybridge.Study.StudyMenti.StudyMenti;
import com.example.studybridge.Study.StudyMenti.StudyMentiHolder;
import com.google.android.material.card.MaterialCardView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MyPageMentoProfileAdapter extends RecyclerView.Adapter<MyPageMentoProfileAdapter.MyPageMentoProfileHolder>{

    private ArrayList<MyPageMentoProfile> listData;

    public MyPageMentoProfileAdapter(ArrayList<MyPageMentoProfile> listData){
        this.listData = listData;
    }
    @NonNull
    @NotNull
    @Override
    public MyPageMentoProfileAdapter.MyPageMentoProfileHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mypage_mentoprofile_quali_item, parent, false);
        MyPageMentoProfileHolder holder = new MyPageMentoProfileHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyPageMentoProfileAdapter.MyPageMentoProfileHolder holder, int position) {
        holder.qauliImg.setImageBitmap(listData.get(position).getQuliImg());

        holder.deleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                remove(holder.getAdapterPosition());

            }
        });
    }

    @Override
    public int getItemCount() {
        return (listData != null ? listData.size() : 0);
    }

    public void addItem(MyPageMentoProfile data) {
        listData.add(data);
    }

    ///// 삭제 함수
    public void remove(int position) {
        try {
            listData.remove(position);
            notifyItemRemoved(position);
        } catch (IndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }
    }

    public class MyPageMentoProfileHolder extends RecyclerView.ViewHolder {

        protected ImageView qauliImg;
        protected MaterialCardView deleteImg;



        public MyPageMentoProfileHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            this.qauliImg = (ImageView) itemView.findViewById(R.id.quali_img);
            this.deleteImg = (MaterialCardView) itemView.findViewById(R.id.quali_delete);

        }


//
//        public void onBind(MyPageMentoProfile data) {
//            qauliImg.setImageBitmap(data.getQuliImg());
//        }

    }

}
