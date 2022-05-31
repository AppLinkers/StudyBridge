package com.example.studybridge.Chat;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.studybridge.R;
import com.example.studybridge.databinding.ChatActivityBinding;
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.message.MessageReq;
import com.example.studybridge.http.dto.message.MessageRes;
import com.example.studybridge.http.dto.message.Room;
import com.example.studybridge.http.dto.study.StudyFindRes;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

public class ChatActivity extends AppCompatActivity {


    private ChatActivityBinding binding;
    //sharedPref
    SharedPreferences sharedPreferences;
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_PK_ID_KEY = "user_pk_id_key";
    public static final String USER_ID_KEY = "user_id_key";
    public static final String USER_NAME = "user_name_key";

    private StudyFindRes study;

    //id 값들
    Long userPkId;
    String userName;
    Long studyId;
    Long roomId;

    //이미지 업로드
    String dir;
    File imgFile;

    private ChatAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

    private static final String TAG = "Chat";

    private StompClient stompClient;

    Gson gson = new Gson();
    int chk = 0;

    DataService dataService = new DataService();



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ChatActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.palletRed));

        //sharedPref
        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userPkId = sharedPreferences.getLong(USER_PK_ID_KEY, 1L);
        userName= sharedPreferences.getString(USER_NAME, "사용자");

        intentData();
        setUI();
        setAddImg();
        initStomp();

    }



    private void intentData(){
        Intent intent = getIntent();
        study = getIntent().getExtras().getParcelable("study");
        studyId = study.getId();
        roomId = intent.getLongExtra("roomId", 0);
    }

    private void setUI(){
        binding.roomName.setText(study.getName());
        StringBuilder sb = new StringBuilder();
        sb.append("(").append(study.getMenteeCnt()+1).append(")");
        binding.roomNum.setText(sb.toString());

        setrecycler();
        setSend();
        backBtn();

    }

    private void setrecycler(){
        linearLayoutManager = new LinearLayoutManager(this);
        binding.rcView.setLayoutManager(linearLayoutManager);
        adapter = new ChatAdapter();
        getData();
        binding.rcView.scrollToPosition(adapter.getItemCount()-1);
    }

    @SuppressLint({"staticFieldLeak", "NewApi"})
    public void getData(){

        AsyncTask<Void, Void, List<MessageRes>> listApi = new AsyncTask<Void, Void, List<MessageRes>>() {
            @Override
            protected List<MessageRes> doInBackground(Void... voids) {
                Call<List<MessageRes>> call = dataService.chat.messageList(roomId);
                try {
                    return call.execute().body();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }


            @Override
            protected void onPostExecute(List<MessageRes> s) {
                super.onPostExecute(s);
            }
        }.execute();

        List<MessageRes> result = null;

        try {
            result = listApi.get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (result.size() > 0) {
            result.forEach(c -> {
                if (c.getUserId().equals(userPkId)) {
                    chk++;
                }

                adapter.addItem(c);
            });
        }

        binding.rcView.setAdapter(adapter);
    }


    private void setSend(){
        //보내기 버튼
        binding.chat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().equals("")){
                    binding.sendImg.setImageResource(R.drawable.ic_send_gray);
                    binding.send.setClickable(false);
                }
                 else {
                    binding.sendImg.setImageResource(R.drawable.ic_send);
                    binding.send.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            System.out.println(charSequence + "");
                            String newChat = charSequence + "";

                            // send to server
                            MessageReq message = new MessageReq("TALK", roomId, userPkId, newChat);
                            String sendMessage = gson.toJson(message);
                            stompClient.send("/pub/chat/message", sendMessage).subscribe();

//                            // edit UI
//                            adapter.addItem(message);
//                            binding.rcView.setAdapter(adapter);
                            binding.rcView.scrollToPosition(adapter.getItemCount()-1);
                            binding.chat.setText("");

                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    private void backBtn(){
        //뒤로가기 버튼
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });
    }

    private void setAddImg(){
        binding.addImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                goGetImg.launch(intent);
            }
        });
    }

    ActivityResultLauncher<Intent> goGetImg = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode()==RESULT_OK){


                        assert result.getData() != null;
                        final Uri uri = result.getData().getData();

                        dir = uriPath(uri);

                        binding.tempImgLayout.setVisibility(View.VISIBLE);
                        Glide.with(getApplicationContext()).load(dir).into(binding.tempImg);
                        /*                        imgFile = new File(dir);*/

                        binding.rcView.scrollToPosition(adapter.getItemCount()-1);
                        binding.sendImg.setImageResource(R.drawable.ic_send);
                        binding.send.setClickable(true);
                        binding.send.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                binding.tempImgLayout.setVisibility(View.GONE);
                                if(dir!=null){
                                    sendTempImg();
                                    dir=null;
                                } else
                                    binding.send.setClickable(false);
                            }
                        });


                        binding.delTempImg.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                binding.tempImgLayout.setVisibility(View.GONE);
                                binding.sendImg.setImageResource(R.drawable.ic_send_gray);
                                binding.send.setClickable(false);
                                dir = null;
                            }
                        });
                    }
                }
            }
    );

    private void sendTempImg(){

        String img_url = null;

        RequestBody image = RequestBody.create(MediaType.parse("multipart/form-data"), new File(dir));
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
        MessageReq message = new MessageReq("PHOTO", roomId, userPkId, img_url);
        String sendMessage = gson.toJson(message);
        stompClient.send("/pub/chat/message", sendMessage).subscribe();

//        // edit UI
//        adapter.addItem(message);
//        binding.rcView.setAdapter(adapter);
        binding.rcView.scrollToPosition(adapter.getItemCount()-1);
        binding.chat.setText("");

    }


    private String uriPath(Uri uri){
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };

            assert uri != null;
            cursor = getContentResolver().query(uri, proj, null, null, null);

            assert cursor != null;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

            cursor.moveToFirst();

            return cursor.getString(column_index);

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @SuppressLint("CheckResult")
    private void initStomp() {
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://10.0.2.2:8080/ws-stomp/websocket");
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
            MessageRes message = gson.fromJson(topicMessage.getPayload(), MessageRes.class);
            runOnUiThread(new Runnable(){
                @Override
                public void run() {
                    adapter.addItem(message);
                    binding.rcView.setAdapter(adapter);
                    binding.rcView.scrollToPosition(adapter.getItemCount()-1);
                    if (message.getUserId().equals(userPkId)) {
                        binding.chat.setText("");
                    }
                }
            });
        });

        // 채팅방 입장 메시지 송신
        if (chk == 0) {
            MessageReq message = new MessageReq("ENTER", roomId, userPkId, "ENTER");
            String enter = gson.toJson(message);
            stompClient.send("/pub/chat/message", enter).subscribe();
        }

    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

/*    //사진 돌아감 방지 메서드
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
    }*/



    /*    private void setNotify(String userName,String chatName, String message){

     *//*        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"studyBridge")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("StudyBridge")
                .setContentText("알림 메세지")
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH);*//*


        NotificationCompat.Builder notification = new NotificationCompat.Builder(this,"studyBridge")
                .setStyle(new NotificationCompat.MessagingStyle(userName)
                    .setConversationTitle(chatName)
                        .addMessage(message,calendar.getTimeInMillis(), (CharSequence) null)
                )
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("StudyBridge")
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH);



        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
//        notificationManagerCompat.notify(123,builder.build());
        notificationManagerCompat.notify(123,notification.build());

    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel("studyBridge", "StudyBridge", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }*/
}