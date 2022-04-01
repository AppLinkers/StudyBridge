package com.example.studybridge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.studybridge.Home.HomeFragment;
import com.example.studybridge.Mypage.MyPageFragment;
import com.example.studybridge.Study.StudyFragment;
import com.example.studybridge.Study.StudyMenti.StudyMenti;
import com.example.studybridge.ToDo.ToDoFragment;
import com.example.studybridge.http.DataService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationHost{

    Fragment HomeFragment;
    Fragment StudyFragment;
    Fragment ToDoFragment;
    Fragment MyPageFragment;
    BottomNavigationView bottomNavigationView;

    DataService dataService;

    boolean isMentee;

    StudyMenti newStudy;


    // creating constant keys for shared preferences.
    SharedPreferences.Editor editor;
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_ID_KEY = "user_id_key";
    public static final String USER_NAME = "user_name_key";
    public static final String USER_ISMENTEE = "user_mentee_key";
    SharedPreferences sharedPreferences;
    String userName;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userName= sharedPreferences.getString(USER_NAME, "사용자");
        userId= sharedPreferences.getString(USER_ID_KEY, "사용자 아이디");

        editor = sharedPreferences.edit();

        bottomNavigationView = findViewById(R.id.bottom_navigation);



        HomeFragment = new HomeFragment();
        StudyFragment = new StudyFragment();
        ToDoFragment = new ToDoFragment();
        MyPageFragment = new MyPageFragment();

        defineMentee();

        Bundle bundle = new Bundle(3);
        bundle.putString("name", userName);
        bundle.putString("id", userId);

        HomeFragment.setArguments(bundle);
        StudyFragment.setArguments(bundle);
        ToDoFragment.setArguments(bundle);
        MyPageFragment.setArguments(bundle);


        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, HomeFragment)
                    .commit();
        }




        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
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

    @Override
    public void navigateTo(Fragment fragment, boolean addToBackstack) {

        FragmentTransaction transaction =
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, fragment);

        if (addToBackstack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();

    }

    private void defineMentee(){
        dataService = new DataService();
        dataService.userAuth.isMentee(userId).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.isSuccessful()){
                    if(response.body()==true){
                        isMentee = true;
                    }else{
                        isMentee = false;
                    }

                    Bundle bundle = new Bundle(3);
                    bundle.putString("name", userName);
                    bundle.putString("id", userId);
                    bundle.putBoolean("isMentee", isMentee);

                    editor.putBoolean(USER_ISMENTEE, isMentee);
                    editor.apply();

                    HomeFragment.setArguments(bundle);
                    StudyFragment.setArguments(bundle);
                    ToDoFragment.setArguments(bundle);
                    MyPageFragment.setArguments(bundle);

                }
            }
            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });
    }

    public void navigationBlink(int menuItem){
        MenuItem selectedItem = bottomNavigationView.getMenu().findItem(menuItem);
        selectedItem.setChecked(true);
    }
}