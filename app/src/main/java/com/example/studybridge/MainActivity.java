package com.example.studybridge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.studybridge.Chat.ChatFragment;
import com.example.studybridge.Home.HomeFragment;
import com.example.studybridge.Mypage.MyPageFragment;
import com.example.studybridge.Study.StudyFragment;
import com.example.studybridge.ToDo.ToDoFragment;
import com.example.studybridge.Util.SharedPrefKey;
import com.example.studybridge.databinding.ActivityMainBinding;
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.userAuth.UserProfileRes;
import com.google.android.material.navigation.NavigationBarView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity{

    Fragment HomeFragment;
    Fragment StudyFragment;
    Fragment ToDoFragment;
    Fragment MyPageFragment;

    DataService dataService = new DataService();

    private ActivityMainBinding binding;

    // creating constant keys for shared preferences.
    SharedPreferences.Editor editor;
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_ID_KEY = "user_id_key";
    public static final String USER_NAME = "user_name_key";
    public static final String USER_ISMENTEE = "user_mentee_key";
    SharedPreferences sharedPreferences;
    String userName;
    String userId;
    boolean isMentee;

    private long backBtnTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userName= sharedPreferences.getString(USER_NAME, "사용자");
        userId= sharedPreferences.getString(USER_ID_KEY, "사용자 아이디");

        editor = sharedPreferences.edit();


        defineMentee();
        userProfileImg();

        setNavigation(savedInstanceState);


    }

    private void setNavigation(Bundle savedInstanceState){


        HomeFragment = new HomeFragment();
        StudyFragment = new StudyFragment();
        ToDoFragment = new ToDoFragment();
        MyPageFragment = new MyPageFragment();

        StudyFragment.setArguments(null);


        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, HomeFragment)
                    .commit();
        }

        binding.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, HomeFragment).commit();
                        return true;
                    case R.id.navigation_study:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,StudyFragment).commit();
                        return true;
                    case R.id.navigation_toDo:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,ToDoFragment).commit();
                        return true;
                    case R.id.navigation_myPage:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,MyPageFragment).commit();
                        return true;
                }
                return false;
            }
        });
    }


    private void defineMentee(){
        dataService.userAuth.isMentee(userId).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.isSuccessful()){
                    if(response.body()==true){
                        isMentee = true;
                    }else{
                        isMentee = false;
                    }

                    editor.putBoolean(USER_ISMENTEE, isMentee);
                    editor.apply();

                }
            }
            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });
    }
    private void userProfileImg(){
        dataService.userAuth.getProfile(userId).enqueue(new Callback<UserProfileRes>() {
            @Override
            public void onResponse(Call<UserProfileRes> call, Response<UserProfileRes> response) {
                if(response.isSuccessful()){
                    assert response.body() != null;
                    final String uri = response.body().getProfileImg();
                    editor.putString(SharedPrefKey.USER_PROFILE,uri);
                    editor.apply();
                }
            }

            @Override
            public void onFailure(Call<UserProfileRes> call, Throwable t) {

            }
        });
    }

    public void navigationBlink(int menuItem){
        MenuItem selectedItem = binding.bottomNavigation.getMenu().findItem(menuItem);
        selectedItem.setChecked(true);
    }

    @Override
    public void onBackPressed() {
        long curTime = System.currentTimeMillis();
        long gapTime = curTime - backBtnTime;

        if(0 <= gapTime && 2000 >= gapTime) {
            super.onBackPressed();
        }
        else {
            backBtnTime = curTime;
            Toast.makeText(this, "한번 더 누르면 종료됩니다.",Toast.LENGTH_SHORT).show();
        }

    }

}