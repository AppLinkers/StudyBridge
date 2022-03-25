package com.example.studybridge.Chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.studybridge.R;
import com.example.studybridge.Study.StudyMenti.StudyMenti;
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.message.FindRoomRes;
import com.example.studybridge.http.dto.message.Message;
import com.example.studybridge.http.dto.message.Room;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

public class ChatActivity extends AppCompatActivity {

    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_PK_ID_KEY = "user_pk_id_key";
    public static final String USER_ID_KEY = "user_id_key";
    public static final String USER_NAME = "user_name_key";
    SharedPreferences sharedPreferences;


    Long userPkId;
    String userName;
    String userId;
    String newChat;

    private Toolbar toolbar;
    private EditText chatEt;

    ChatAdapter adapter;
    RecyclerView rcChat;

    private static final String TAG = "Chat";

    private StompClient stompClient;

    Long studyId;
    Long roomId;

    Gson gson = new Gson();
    int chk = 0;

    DataService dataService = new DataService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        studyId = intent.getLongExtra("studyId",0);
        roomId = intent.getLongExtra("roomId", 0);

        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userPkId = sharedPreferences.getLong(USER_PK_ID_KEY, 1L);
        userName= sharedPreferences.getString(USER_NAME, "사용자");
        userId= sharedPreferences.getString(USER_ID_KEY, "사용자 아이디");


        chatEt = findViewById(R.id.mycontext);


        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("예시 스터디");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        rcChat = findViewById(R.id.rc_chat);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcChat.setLayoutManager(linearLayoutManager);
        adapter = new ChatAdapter();
        getData();
        rcChat.scrollToPosition(adapter.getItemCount()-1);

        initStomp();

    }


    @SuppressLint("CheckResult")
    private void initStomp() {
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://3.141.122.128:8080/ws-stomp/websocket");
        stompClient.lifecycle().subscribe(lifecycleEvent -> {
            switch (lifecycleEvent.getType()) {
                case OPENED:
                    Log.d(TAG, "Stomp connection opened");
                    break;
                case ERROR:
                    Log.e(TAG, "Error", lifecycleEvent.getException());
                    if(lifecycleEvent.getException().getMessage().contains("EOF")){
                    }
                    break;
                case CLOSED:
                    Log.d(TAG, "Stomp connection closed");
                    break;
            }
        });
        stompClient.connect();

        // message 수신
        stompClient.topic("/sub/chat/room/" + roomId).subscribe(topicMessage -> {
            Log.d(TAG, topicMessage.getPayload());
            Message message = gson.fromJson(topicMessage.getPayload(), Message.class);
            runOnUiThread(new Runnable(){
                @Override
                public void run() {
                    if (!message.getSenderId().equals(userPkId)) {
                        Chat chat = new Chat(message.getSenderName(), message.getMessage());
                        adapter.addItem(chat);
                        rcChat.setAdapter(adapter);
                        //scroll 설정
                    }
                }
            });
        });

        // 채팅방 입장 메시지 송신
        if (chk == 0) {
            Message message = new Message("ENTER", new Room(roomId), userPkId, userName, "ENTER");
            String enter = gson.toJson(message);
            stompClient.send("/pub/chat/message", enter).subscribe();
        }

    }


    @SuppressLint({"staticFieldLeak", "NewApi"})
    public void getData(){

        AsyncTask<Void, Void, List<Message>> listApi = new AsyncTask<Void, Void, List<Message>>() {
            @Override
            protected List<Message> doInBackground(Void... voids) {
                Call<List<Message>> call = dataService.chat.messageList(roomId);
                try {
                    return call.execute().body();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }


            @Override
            protected void onPostExecute(List<Message> s) {
                super.onPostExecute(s);
            }
        }.execute();

        List<Message> result = null;

        try {
            result = listApi.get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d(TAG, String.valueOf(result));

        if (result.size() > 0) {
            result.forEach(c -> {
                if (c.getSenderId().equals(userPkId)) {
                    chk++;
                }

                Chat chat = new Chat(c.getSenderName(), c.getMessage());
                adapter.addItem(chat);
            });
        }

        System.out.println(adapter.getItemCount());
        rcChat.setAdapter(adapter);
    }


    public void send(View view) {
        newChat = chatEt.getText()+"";
        Chat chatSend = new Chat(userId, newChat);

        // send to server
        Message message = new Message("TALK", new Room(roomId), userPkId, userName, newChat);
        String sendMessage = gson.toJson(message);
        stompClient.send("/pub/chat/message", sendMessage).subscribe();
        Log.d(TAG, sendMessage);

        // edit UI
        adapter.addItem(chatSend);
        rcChat.setAdapter(adapter);
        rcChat.scrollToPosition(adapter.getItemCount()-1);
        chatEt.setText("");
    }
}