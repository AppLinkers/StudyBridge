package com.example.studybridge.Mypage.Chat;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.Chat.ChatActivity;
import com.example.studybridge.R;
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.message.FindRoomRes;
import com.example.studybridge.http.dto.message.Message;
import com.example.studybridge.http.dto.study.StudyFindRes;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MypageChatHolder extends RecyclerView.ViewHolder{

    private ImageView img;
    private TextView name,lastChat,num;

    private FindRoomRes findRoomRes;
    private StudyFindRes study;

    DataService dataService;

    public MypageChatHolder(@NonNull View itemView) {
        super(itemView);
        //화면 위 데이터
        img = (ImageView) itemView.findViewById(R.id.mypage_chat_img);
        name = (TextView) itemView.findViewById(R.id.mypage_chat_name);
        lastChat = (TextView) itemView.findViewById(R.id.mypage_chat_lastChat);
        num = (TextView) itemView.findViewById(R.id.mypage_chat_num);

        dataService = new DataService();

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToChat(view);
            }
        });
    }

    public void onBind(StudyFindRes data) {
        name.setText(data.getName());
        num.setText(String.valueOf(data.getMenteeCnt()+1));
        getRoom(data.getId(),lastChat);
        study = data;
    }

    private void getRoom(Long studyId,TextView textView){
        dataService.chat.getRoom(studyId).enqueue(new Callback<FindRoomRes>() {
            @Override
            public void onResponse(Call<FindRoomRes> call, Response<FindRoomRes> response) {
                if(response.isSuccessful()){
                    findRoomRes = response.body();
                    getLastMsg(response.body().getRoomId(),textView);
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
        chatIntent.putExtra("roomId", findRoomRes.getRoomId());
        chatIntent.putExtra("study",study);
        view.getContext().startActivity(chatIntent);
    }
}
