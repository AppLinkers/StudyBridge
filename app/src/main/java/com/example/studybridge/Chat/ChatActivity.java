package com.example.studybridge.Chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;

import com.example.studybridge.R;
import com.example.studybridge.http.DataService;
import com.google.gson.Gson;

import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
   // private ChatAdapter adapter;
    private EditText mycontext;

    // creating constant keys for shared preferences.
    public static final String SHARED_PREFS = "shared_prefs";

    // key for storing Member
    public static final String USER_ID_KEY = "user_id_key";
    public static final String USER_LOGIN_ID_KEY = "user_login_id_key";

    // variable for shared preferences.
    SharedPreferences sharedPreferences;
    Long user_id;
    String user_login_id;

 //   Room room;
    Long ticketId;

    private static final String TAG = "Chat";

    DataService dataService = new DataService();
 //   private StompClient stompClient;
  //  private List<StompHeader> headerList;

    Gson gson = new Gson();
    int chk = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
    }
}