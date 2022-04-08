package com.example.studybridge.Chat;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.studybridge.R;
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.message.Message;
import com.example.studybridge.http.dto.message.Room;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.List;


import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

public class ChatActivity extends AppCompatActivity {

    //sharedPref
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_PK_ID_KEY = "user_pk_id_key";
    public static final String USER_ID_KEY = "user_id_key";
    public static final String USER_NAME = "user_name_key";
    SharedPreferences sharedPreferences;
    public static final int PICK_IMAGE = 101;

    //id 값들
    Long userPkId;
    String userName;
    String userId;
    String newChat;
    Long studyId;
    Long roomId;

    //이미지 업로드
    String dir;
    File imgFile;

    private Toolbar toolbar;
    private EditText chatEt;
    private ImageView addImg,exampleImg;

    ChatAdapter adapter;
    RecyclerView rcChat;

    private static final String TAG = "Chat";

    private StompClient stompClient;

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
        addImg = (ImageView) findViewById(R.id.chat_addImg);
        exampleImg = (ImageView) findViewById(R.id.exapleImg);


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

        setAddImg();

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
                        adapter.addItem(message);
                        rcChat.setAdapter(adapter);
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

        if (result.size() > 0) {
            result.forEach(c -> {
                if (c.getSenderId().equals(userPkId)) {
                    chk++;
                }


                adapter.addItem(c);
            });
        }

        System.out.println(adapter.getItemCount());
        rcChat.setAdapter(adapter);
    }


    public void send(View view) throws URISyntaxException {

        String messageType = "PHOTO";
        //메세지 타입 설정 필요

        if (messageType.equals("TALK")) {
            newChat = chatEt.getText()+"";


            // send to server
            Message message = new Message("TALK", new Room(roomId), userPkId, userName, newChat);
            String sendMessage = gson.toJson(message);
            stompClient.send("/pub/chat/message", sendMessage).subscribe();
            // edit UI
            adapter.addItem(message);
            rcChat.setAdapter(adapter);
            rcChat.scrollToPosition(adapter.getItemCount()-1);
            chatEt.setText("");
        } else if (messageType.equals("PHOTO")) {

            String img_url = null;

            RequestBody image = RequestBody.create(MediaType.parse("multipart/form-data"), imgFile);
            MultipartBody.Part chatImg = MultipartBody.Part.createFormData("image", "image", image);

            @SuppressLint("StaticFieldLeak")
            AsyncTask<Void, Void, String> API = new AsyncTask<Void, Void, String>() {

                final ProgressDialog dialog = new ProgressDialog(ChatActivity.this);

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    dialog.setMessage("Processing ...");
                    dialog.show();
                }

                @Override
                protected String doInBackground(Void... params) {
                    Call<String> call = dataService.s3.chatImg(chatImg);

                    try {
                        return call.execute().body();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(String s) {
                    dialog.dismiss();
                }
            }.execute();

            try {
                img_url = API.get();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // send to server
            Message message = new Message("PHOTO", new Room(roomId), userPkId, userName, img_url);
            String sendMessage = gson.toJson(message);
            stompClient.send("/pub/chat/message", sendMessage).subscribe();

            // edit UI
            adapter.addItem(message);
            rcChat.setAdapter(adapter);
            rcChat.scrollToPosition(adapter.getItemCount()-1);
            chatEt.setText("");
        }
    }
    private void setAddImg(){
        addImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });
    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            switch (requestCode) {
                case PICK_IMAGE: //사진 선택시

                    try {
                        InputStream in = getContentResolver().openInputStream(data.getData());


                        Bitmap img = BitmapFactory.decodeStream(in);

                        Bitmap rImg = rotateImage(data.getData(), img);
                        in.close();

                        exampleImg.setImageBitmap(rImg);

                        dir = saveBitmapToJpg(rImg,"testPath");

                        imgFile = new File(dir);


                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

            }
        }
        else if(requestCode == RESULT_CANCELED){
            //취소할 경우
        }
    }

    //사진 돌아감 방지 메서드
    @RequiresApi(api = Build.VERSION_CODES.N)
    private Bitmap rotateImage(Uri uri, Bitmap bitmap) throws IOException {
        InputStream in = getContentResolver().openInputStream(uri);
        ExifInterface exif = new ExifInterface(in);
        in.close();

        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL);
        Matrix matrix = new Matrix();

        if (orientation == ExifInterface.ORIENTATION_ROTATE_90){
            matrix.postRotate(90);
        }
        else if (orientation == ExifInterface.ORIENTATION_ROTATE_180){
            matrix.postRotate(180);
        }
        else if (orientation == ExifInterface.ORIENTATION_ROTATE_270){
            matrix.postRotate(270);
        }

        return Bitmap.createBitmap(bitmap, 0,0,bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

      public String saveBitmapToJpg(Bitmap bitmap , String name) {

        File storage = getCacheDir(); //  path = /data/user/0/YOUR_PACKAGE_NAME/cache
        String fileName = name + ".jpg";
        File imgFile = new File(storage, fileName);
        try {
            imgFile.createNewFile();
            FileOutputStream out = new FileOutputStream(imgFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();
        } catch (FileNotFoundException e) {
            Log.e("saveBitmapToJpg","FileNotFoundException : " + e.getMessage());
        } catch (IOException e) {
            Log.e("saveBitmapToJpg","IOException : " + e.getMessage());
        }
        Log.d("imgPath" , getCacheDir() + "/" +fileName);

        return getCacheDir() + "/" +fileName;
    }
}