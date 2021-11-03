package com.example.studybridge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.studybridge.Home.HomeFragment;
import com.example.studybridge.Mypage.MyPageFragment;
import com.example.studybridge.Study.StudyFragment;
import com.example.studybridge.ToDo.ToDoFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity implements NavigationHost{

    Fragment HomeFragment;
    Fragment StudyFragment;
    Fragment ToDoFragment;
    Fragment MyPageFragment;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        HomeFragment = new HomeFragment();
        StudyFragment = new StudyFragment();
        ToDoFragment = new ToDoFragment();
        MyPageFragment = new MyPageFragment();

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new HomeFragment())
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
}