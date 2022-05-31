package com.example.studybridge.Home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;

import com.example.studybridge.Mypage.MyPageEditActivity;

import com.example.studybridge.R;
import com.example.studybridge.Study.StudyAddActivity;
import com.example.studybridge.Study.StudyFragment;
import com.example.studybridge.Study.StudyMenti.Detail.StudyMentiDetail;
import com.example.studybridge.Study.StudyMento.Detail.StudyMentoDetail;
import com.example.studybridge.Util.SharedPrefKey;
import com.example.studybridge.databinding.HomeFragmentBinding;
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.study.StudyFindRes;
import com.example.studybridge.http.dto.userAuth.UserProfileRes;
import com.example.studybridge.http.dto.userMentor.ProfileRes;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {


    //Dataservice
    DataService dataService = new DataService();

    SharedPreferences sharedPreferences;
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_PK_ID_KEY = "user_pk_id_key";
    public static final String USER_NAME = "user_name_key";
    public static final String USER_ID_KEY = "user_id_key";

    private String userName,userId,imgPath;
    private Long userIdPk;

    private HomeFragmentBinding binding;

    private StudyFragment studyFragment;
    private ProfileRes profile;

    String managerId;
    Boolean isApplied;
    private StudyFindRes studyFindRes;



    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = HomeFragmentBinding.inflate(inflater,container,false);

        View view = binding.getRoot();

        //sharedpreference
        sharedPreferences = view.getContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userIdPk= sharedPreferences.getLong(USER_PK_ID_KEY,  0);
        userName= sharedPreferences.getString(USER_NAME, "사용자");
        userId = sharedPreferences.getString(USER_ID_KEY, "사용자 아이디");
        imgPath = sharedPreferences.getString(SharedPrefKey.USER_PROFILE,"img");

        setUI();
        //화면 위 데이터

        return view;
    }


    private void setUI(){
        StringBuilder sb;
        sb = new StringBuilder();
        sb.append(userName).append("님");
        binding.title.setText(sb.toString());
        Glide.with(getContext()).load(imgPath).into(binding.headerImg);

        getStudyData();
        getMentorData();
        setHeader(binding.goEnglish,"영어");
        setHeader(binding.goMath,"수학");
        setHeader(binding.goDev,"개발");
        setHeader(binding.goEtc,"기타");
        setBtns();
    }

    private void getStudyData(){
        dataService.study.find().enqueue(new Callback<List<StudyFindRes>>() {
            @Override
            public void onResponse(Call<List<StudyFindRes>> call, Response<List<StudyFindRes>> response) {
                if(response.isSuccessful()){
                    final int lastPos = response.body().size()-1;
                    final StudyFindRes lastStudy = response.body().get(lastPos);
                    studyFindRes = lastStudy;
                    getManagerId(lastStudy.getId());
                    findApplied(lastStudy.getId());
                    binding.studyItem.studyName.setText(lastStudy.getName());
                    binding.studyItem.studyIntro.setText(lastStudy.getInfo());
                    binding.studyItem.studySubject.setText(lastStudy.getType());
                    binding.studyItem.studyPlace.setText(lastStudy.getPlace());
                    StringBuilder sb = new StringBuilder();
                    sb.append("인원수 : ").append(lastStudy.getMenteeCnt()).append(" / ").append(lastStudy.getMaxNum());
                    binding.studyItem.studyNum.setText(sb.toString());
                    lastStudy.statusSet(binding.studyItem.studyStatus,getContext());
                }
            }

            @Override
            public void onFailure(Call<List<StudyFindRes>> call, Throwable t) {

            }
        });
    }

    public void getManagerId(Long studyId){
        dataService.study.maker(studyId).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                managerId = response.body();
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
    private void findApplied(Long study_id){
        dataService.study.isApplied(study_id,userId).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                isApplied = response.body();
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });
    }

    private void getMentorData(){
        dataService.userMentor.getAllProfile(userId).enqueue(new Callback<List<ProfileRes>>() {
            @Override
            public void onResponse(Call<List<ProfileRes>> call, Response<List<ProfileRes>> response) {
                if(response.isSuccessful()){
                    final int lastPos = response.body().size();
                    ProfileRes lastProf=null;
                    for(int i=lastPos-1; i>=0 ; i--){
                        if(response.body().get(i).getNickName() != null){
                            lastProf = response.body().get(i);
                            break;
                        }
                    }
                    profile = lastProf;
                    binding.mentorItem.mentorSubject.setText(lastProf.getSubject());
                    binding.mentorItem.mentorPlace.setText(lastProf.getLocation());
                    binding.mentorItem.mentorName.setText(lastProf.getNickName());
                    binding.mentorItem.mentorIntro.setText(lastProf.getInfo());
                    binding.mentorItem.mentorSchool.setText(lastProf.getSchool());
                    Glide.with(getContext()).load(lastProf.getProfileImg()).into(binding.mentorItem.mentorImg);
/*                    setProfile(lastProf.getUserLoginId());*/
                }
            }

            @Override
            public void onFailure(Call<List<ProfileRes>> call, Throwable t) {

            }
        });
    }

    private void setProfile(String userId){
        dataService.userAuth.getProfile(userId).enqueue(new Callback<UserProfileRes>() {
            @Override
            public void onResponse(Call<UserProfileRes> call, Response<UserProfileRes> response) {
                if(response.isSuccessful()){
                    final String path = response.body().getProfileImg();
                    Glide.with(getActivity()).load(path).into(binding.mentorItem.mentorImg);
                }
            }

            @Override
            public void onFailure(Call<UserProfileRes> call, Throwable t) {

            }
        });
    }

    private void setHeader(LinearLayout linearLayout,String subject){

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("subject",subject);
                moveFragment(bundle);
            }
        });
    }
    
    private void setBtns(){

        binding.headerImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MyPageEditActivity.class);
                startActivity(intent);
                Toast.makeText(getContext(), "[마이]->[프로필 수정]에서 확인할 수 있습니다", Toast.LENGTH_SHORT).show();
                getActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });

        binding.goStudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                moveFragment(null);

                Intent intent = new Intent(getContext(), StudyAddActivity.class);
                intent.putExtra(null,"study");
                startActivity(intent);
            }
        });

        binding.studyItem.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                moveFragment(null);

                Intent intentToDetail = new Intent(view.getContext(), StudyMentiDetail.class);
                intentToDetail.putExtra("study",studyFindRes);
                intentToDetail.putExtra("managerId",managerId);
                intentToDetail.putExtra("isApplied",isApplied);

                view.getContext().startActivity(intentToDetail);
                getActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });

        binding.findMentor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString("goMentor","goMentor");
                moveFragment(bundle);

            }
        });

        binding.mentorItem.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("goMentor","goMentor");
                moveFragment(bundle);

                Intent intent = new Intent(view.getContext(), StudyMentoDetail.class);
                intent.putExtra("profile", profile);
                view.getContext().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

            }
        });
    }

    private void moveFragment(Bundle bundle){
        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        BottomNavigationView navigationView = getActivity().findViewById(R.id.bottom_navigation);
        MenuItem menuItem = navigationView.getMenu().findItem(R.id.navigation_study);
        menuItem.setChecked(true);
        studyFragment = new StudyFragment();
        studyFragment.setArguments(bundle);
        ft.replace(R.id.container,studyFragment);
        ft.commit();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}
