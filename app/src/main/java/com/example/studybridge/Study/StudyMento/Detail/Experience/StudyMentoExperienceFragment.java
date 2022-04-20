package com.example.studybridge.Study.StudyMento.Detail.Experience;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.studybridge.R;
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.userMentor.ProfileRes;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudyMentoExperienceFragment extends Fragment {

    private TextView curi,experience;

    DataService dataService = new DataService();


    //SharedPref
    SharedPreferences sharedPreferences;
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_ID_KEY = "user_id_key";


    //넘어온 데이터들
    private String userId;

    //null값 처리
    public static final String VALUE_NULL_STR = "내용이 없습니다";

    @Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.study_mento_experience_fragment,container,false);

        curi = (TextView) view.findViewById(R.id.mento_curi_tv);
        experience = (TextView) view.findViewById(R.id.mento_experience_tv);


        //sharedPreference, 현재 이용자 아이디 불러옴
        sharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userId= sharedPreferences.getString(USER_ID_KEY, "사용자 아이디");

        Bundle extras = getArguments();

        ProfileRes profile = extras.getParcelable("profile");
        final String mentoId = extras.getString("mentoId");


        if(mentoId == null || mentoId.equals("")) {
            if (extras != null) {
                curi.setText(checkNull(profile.getCurriculum()));
                experience.setText(checkNull(profile.getExperience()));
            }
        }
        else {
            dataService.userMentor.getProfile(mentoId, userId).enqueue(new Callback<ProfileRes>() {
                @Override
                public void onResponse(Call<ProfileRes> call, Response<ProfileRes> response) {
                    if (response.isSuccessful())
                    {
                        curi.setText(checkNull(response.body().getCurriculum()));
                        experience.setText(checkNull(response.body().getExperience()));
                    }
                }

                @Override
                public void onFailure(Call<ProfileRes> call, Throwable t) {

                }
            });
        }


        return view;
    }

    public static String checkNull(String str){
        if(str==null){
            return VALUE_NULL_STR;
        }
        else
            return str;
    }


}

