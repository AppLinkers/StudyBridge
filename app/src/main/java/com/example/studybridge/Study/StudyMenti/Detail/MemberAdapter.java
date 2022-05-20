package com.example.studybridge.Study.StudyMenti.Detail;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;

import com.bumptech.glide.Glide;
import com.example.studybridge.R;
import com.example.studybridge.Study.StudyMento.Detail.StudyMentoDetail;
import com.example.studybridge.Util.ItemClickListener;
import com.example.studybridge.databinding.StudyMenteeMemberitemBinding;
import com.example.studybridge.databinding.StudyMentorMemberitemBinding;
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.userAuth.UserProfileRes;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MemberAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<String> listData;
    private int path;
    private View view;

    private ItemClickListener itemClickListener;
    private Activity activity;
    int selectPosition = -1;

    private DataService dataService = new DataService();

    public MemberAdapter(List<String> listData, int path, ItemClickListener itemClickListener,Activity activity) {
        this.listData = listData;
        this.path = path;
        this.itemClickListener = itemClickListener;
        this.activity = activity;
    }

    public MemberAdapter(List<String> listData, int path) {
        this.listData = listData;
        this.path = path;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(path==0){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.study_mentee_memberitem,parent,false);
        }
        else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.study_mentor_memberitem,parent,false);
        }
        return new MemberHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(path==0){
            ((MemberHolder) holder).onBind(listData.get(position));
        }
        else {
            ((MemberHolder) holder).onBind(listData.get(position));
            if(itemClickListener!=null){
                ((MemberHolder) holder).bindMentor.mentorId.setChecked(position==selectPosition);
                ((MemberHolder) holder).bindMentor.mentorId.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if(b){
                            selectPosition = holder.getAdapterPosition();
                            itemClickListener.onClick(((MemberHolder) holder).bindMentor.mentorId.getText().toString());
                            ((MemberHolder) holder).bindMentor.mentorId.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.palletRed50));
                            ((MemberHolder) holder).bindMentor.mentorId.setTextColor(ContextCompat.getColor(view.getContext(), R.color.white));
                        }
                        else {
                            ((MemberHolder) holder).bindMentor.mentorId.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.white));
                            ((MemberHolder) holder).bindMentor.mentorId.setTextColor(ContextCompat.getColor(view.getContext(), R.color.textColorPrimary));
                        }
                    }
                });
            } else {
                ((MemberHolder) holder).bindMentor.mentorId.setEnabled(false);
            }
        }
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    ////////Holder////////////////
    public class MemberHolder extends RecyclerView.ViewHolder{

        private StudyMenteeMemberitemBinding bindMentee;
        private StudyMentorMemberitemBinding bindMentor;

        public MemberHolder(@NonNull View itemView) {
            super(itemView);
            if(path==0){
                bindMentee = StudyMenteeMemberitemBinding.bind(itemView);
            } else {
                bindMentor = StudyMentorMemberitemBinding.bind(itemView);
                bindMentor.showProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(view.getContext(), StudyMentoDetail.class);
                        intent.putExtra("mentoId",bindMentor.mentorId.getText().toString());
                        view.getContext().startActivity(intent);
                        activity.overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    }
                });
            }

        }

        public void onBind(String data){

            if(path==0){
                bindMentee.menteeId.setText(data);
                setProfile(data,bindMentee.img);
            }
            else {
                bindMentor.mentorId.setText(data);
                setProfile(data,bindMentor.img);
            }
        }

        private void setProfile(String userId, ImageView imageView){
            dataService.userAuth.getProfile(userId).enqueue(new Callback<UserProfileRes>() {
                @Override
                public void onResponse(Call<UserProfileRes> call, Response<UserProfileRes> response) {
                    if(response.isSuccessful()){
                        final String path = response.body().getProfileImg();
                        Glide.with(itemView).load(path).into(imageView);
                    }
                }

                @Override
                public void onFailure(Call<UserProfileRes> call, Throwable t) {

                }
            });
        }
    }

}
