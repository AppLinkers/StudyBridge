package com.example.studybridge.Chat;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.R;
import com.example.studybridge.databinding.ChatroomItemBinding;
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.message.FindRoomRes;
import com.example.studybridge.http.dto.message.Message;
import com.example.studybridge.http.dto.study.StudyFindRes;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChatRoomHolder extends RecyclerView.ViewHolder{

    private final ChatroomItemBinding binding;

    private StudyFindRes study;
    private Long roomId;
    private Activity activity;
    DataService dataService;

    public ChatRoomHolder(@NonNull View itemView) {
        super(itemView);
        binding = ChatroomItemBinding.bind(itemView);

        dataService = new DataService();

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToChat(view);
            }
        });
    }

    public void onBind(StudyFindRes data,Activity activity) {

        study = data;
        this.activity = activity;

        binding.name.setText(data.getName());
        binding.num.setText(String.valueOf(data.getMenteeCnt()+1));
        getRoom(data.getId(),binding.lastChat);


    }

    private void getRoom(Long studyId,TextView textView){
        dataService.chat.getRoom(studyId).enqueue(new Callback<FindRoomRes>() {
            @Override
            public void onResponse(Call<FindRoomRes> call, Response<FindRoomRes> response) {
                if(response.isSuccessful()){

                    roomId = response.body().getRoomId();
                    getLastMsg(roomId,textView);
                }
            }

            @Override
            public void onFailure(Call<FindRoomRes> call, Throwable t) {

            }
        });
    }

    private void getLastMsg(Long roomId,TextView textView){
        dataService.chat.messageList(roomId).enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                if(response.isSuccessful()){
                    StringBuilder sb = new StringBuilder();

                    if(response.body().size()==0){
                        sb.append("");
                    } else {
                        Message m = response.body().get(response.body().size()-1);

                        if(m.getMessageType().equals("PHOTO")){
                            sb.append(m.getSenderName()).append(": ").append("사진");
                        }
                        else {
                            sb.append(m.getSenderName()).append(": ").append(m.getMessage());
                        }
                    }

                    textView.setText(sb.toString());
                }
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {

            }
        });
    }
    private void goToChat(View view){
        Intent chatIntent = new Intent(itemView.getContext(), ChatActivity.class);
        chatIntent.putExtra("roomId", roomId);
        chatIntent.putExtra("study",study);
        view.getContext().startActivity(chatIntent);
        activity.overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }
}
