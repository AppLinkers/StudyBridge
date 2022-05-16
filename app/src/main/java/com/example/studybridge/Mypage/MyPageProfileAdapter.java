package com.example.studybridge.Mypage;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.studybridge.R;
import com.example.studybridge.databinding.MypageMentoprofileItemBinding;
import com.example.studybridge.http.dto.userMentor.Certificate;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MyPageProfileAdapter extends RecyclerView.Adapter<MyPageProfileAdapter.MyPageProfileHolder>{

    private List<Certificate> listData;
    private Context context;

    public MyPageProfileAdapter(List<Certificate> listData,Context context) {
        this.listData = listData;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public MyPageProfileAdapter.MyPageProfileHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mypage_mentoprofile_item, parent, false);
        MyPageProfileHolder holder = new MyPageProfileHolder(view,new MyCustomEditTextListener());
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyPageProfileAdapter.MyPageProfileHolder holder, int position) {

        holder.myCustomEditTextListener.updatePosition(holder.getAdapterPosition());
        ((MyPageProfileHolder) holder).onBind(listData.get(position));


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

    ////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onViewAttachedToWindow(@NonNull MyPageProfileHolder holder) {
        ((MyPageProfileHolder)holder).enableTextWatcher();
    }


    @Override
    public void onViewDetachedFromWindow(@NonNull MyPageProfileHolder holder) {
        ((MyPageProfileHolder) holder).disableTextWatcher();
    }

    ////////////////////////////////////////////////////////////////////////////////////////
    public class MyPageProfileHolder extends RecyclerView.ViewHolder {


        public  MyCustomEditTextListener myCustomEditTextListener;
        private MypageMentoprofileItemBinding binding;


        public MyPageProfileHolder(@NonNull @NotNull View itemView,MyCustomEditTextListener myCustomEditTextListener) {
            super(itemView);
            binding = MypageMentoprofileItemBinding.bind(itemView);
            binding.x.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    remove(getAdapterPosition());
                }
            });
            binding.imgCon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                }
            });

            this.myCustomEditTextListener = myCustomEditTextListener;

        }

        public void onBind(Certificate certificate){
            if(certificate.getCertificate()!=null){
                binding.name.setText(certificate.getCertificate());
            }

            Glide.with(itemView).load(certificate.getImgUrl()).into(binding.img);

        }

        void enableTextWatcher() {
            binding.name.addTextChangedListener(myCustomEditTextListener);
        }

        void disableTextWatcher() {
            binding.name.removeTextChangedListener(myCustomEditTextListener);
        }

    }



    /////////////////////////////////////////////////////////////////////////
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
