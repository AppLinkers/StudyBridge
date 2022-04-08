package com.example.studybridge.Mypage.MentoProfile.Edit;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.studybridge.R;
import com.example.studybridge.http.dto.userMentor.Certificate;
import com.google.android.material.card.MaterialCardView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyPageMentoProfileAdapter extends RecyclerView.Adapter<MyPageMentoProfileAdapter.MyPageMentoProfileHolder>{

    private List<Certificate> listData;

    public MyPageMentoProfileAdapter(List<Certificate> listData) {
        this.listData = listData;
    }

    @NonNull
    @NotNull
    @Override
    public MyPageMentoProfileAdapter.MyPageMentoProfileHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mypage_mentoprofile_quali_item, parent, false);
        MyPageMentoProfileHolder holder = new MyPageMentoProfileHolder(view,new MyCustomEditTextListener());
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyPageMentoProfileAdapter.MyPageMentoProfileHolder holder, int position) {

        Glide.with(holder.itemView).load(Uri.parse(listData.get(position).getImgUrl())).into(holder.qualiImg);
        holder.qualiImg.setImageURI(Uri.parse(listData.get(position).getImgUrl()));
        holder.myCustomEditTextListener.updatePosition(holder.getAdapterPosition());
        holder.qualiName.setText(listData.get(holder.getAdapterPosition()).getCertificate());


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

    ///// 삭제 함수
    public void remove(int position) {
        try {
            listData.remove(position);
            notifyItemRemoved(position);
        } catch (IndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onViewAttachedToWindow(@NonNull MyPageMentoProfileHolder holder) {
        ((MyPageMentoProfileHolder)holder).enableTextWatcher();
    }


    @Override
    public void onViewDetachedFromWindow(@NonNull MyPageMentoProfileHolder holder) {
        ((MyPageMentoProfileHolder) holder).disableTextWatcher();
    }

    public class MyPageMentoProfileHolder extends RecyclerView.ViewHolder {

        protected ImageView qualiImg;
        protected MaterialCardView deleteImg;
        protected EditText qualiName;
        public  MyCustomEditTextListener myCustomEditTextListener;



        public MyPageMentoProfileHolder(@NonNull @NotNull View itemView,MyCustomEditTextListener myCustomEditTextListener) {
            super(itemView);

            this.qualiName = (EditText) itemView.findViewById(R.id.quali_name);
            this.qualiImg = (ImageView) itemView.findViewById(R.id.quali_img);
            this.deleteImg = (MaterialCardView) itemView.findViewById(R.id.quali_delete);

            this.myCustomEditTextListener = myCustomEditTextListener;

        }

        void enableTextWatcher() {
            qualiName.addTextChangedListener(myCustomEditTextListener);
        }

        void disableTextWatcher() {
            qualiName.removeTextChangedListener(myCustomEditTextListener);
        }



    }

    private class MyCustomEditTextListener implements TextWatcher {
        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // no op
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            listData.get(position).setCertificate(charSequence.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

}
