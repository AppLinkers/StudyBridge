package com.example.studybridge.Study.StudyMento;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.studybridge.R;
import com.example.studybridge.Study.StudyFilter;
import com.example.studybridge.Util.FilterDialog;
import com.example.studybridge.databinding.StudyMentoFragmentBinding;
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.userMentor.ProfileRes;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;


public class StudyMentoFragment extends Fragment {


    //리사이클러
    private StudyMentoAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private StudyFilter filter;

    //화면 위 데이터
    DataService dataService;

    //SharedPref
    SharedPreferences sharedPreferences;
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_ID_KEY = "user_id_key";
    public static final String USER_PK_ID_KEY = "user_pk_id_key";

    private String userId;

    private StudyMentoFragmentBinding binding;

    public static final int SUBJECT = 1;
    public static final int PLACE = 2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = StudyMentoFragmentBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        dataService = new DataService();

        //sharedPreference
        sharedPreferences = view.getContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userId = sharedPreferences.getString(USER_ID_KEY, "사용자 아이디");

/*        setShimmerFrameLayout();*/


        //recycler
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        binding.rCView.setLayoutManager(linearLayoutManager);
        binding.rCView.setItemViewCacheSize(5);



        setRecyclerView();
        adMob();
        setRefresh();
        setFilter(binding.subjectFilt, SUBJECT);
        setFilter(binding.placeFilt, PLACE);
        showAll(binding.subjectFilt, binding.placeFilt);

        return view;
    }

    private void setShimmerFrameLayout(){

        binding.rCView.setVisibility(View.INVISIBLE);
        binding.shimmerView.startShimmer();

/*        Handler handler = new Handler();
        handler.postDelayed(()->{

        },2000);*/
    }

    private void adMob(){
        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {

            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.clearItem();
        getData();
        binding.rCView.setAdapter(adapter);
    }

    private void setRefresh(){
        binding.swipeRC.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                binding.swipeRC.postDelayed(new Runnable() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        binding.rCView.setAdapter(adapter);
                        binding.swipeRC.setRefreshing(false);
                    }
                },500);

            }
        });
        binding.swipeRC.setColorSchemeColors(getResources().getColor(R.color.palletRed));
    }

    private void setFilter(TextView filterName, int type){
        filterName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FilterDialog dialog = FilterDialog.getInstance(type);
                FragmentManager fm = getParentFragmentManager();
                dialog.show(fm,"filter");
                Bundle bundle = new Bundle();
                bundle.putString("filter",filterName.getText().toString());
                dialog.setArguments(bundle);

                dialog.setFilterInterFace(new FilterDialog.FilterInterFace() {
                    @Override
                    public void select(String filter) {
                        filterName.setText(filter);
                        if(filter.equals("과목별")||filter.equals("지역별")){
                            filterUnClick(filterName);
                        }
                        else {
                            filterClick(filterName);
                        }

                    }
                });

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        adapter = new StudyMentoAdapter(getActivity());
                        filter = new StudyFilter(
                                binding.subjectFilt.getText().toString(),
                                binding.placeFilt.getText().toString());
                        getData();
                        binding.rCView.setAdapter(adapter);
                    }
                });
            }
        });
    }

    private void showAll(TextView subject, TextView place){
        binding.filtAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                subject.setText("과목별");
                place.setText("지역별");
                filterUnClick(subject);
                filterUnClick(place);

                adapter = new StudyMentoAdapter(getActivity());
                filter = new StudyFilter(
                        binding.subjectFilt.getText().toString(),
                        binding.placeFilt.getText().toString());
                getData();
                binding.rCView.setAdapter(adapter);
            }
        });
    }
    private void filterUnClick(TextView textView){
        textView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
        textView.setTextColor(ContextCompat.getColor(getContext(), R.color.textColorPrimary));
        textView.setTypeface(null, Typeface.NORMAL);
    }
    private void filterClick(TextView textView){
        textView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.textColorPrimary70));
        textView.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        textView.setTypeface(null, Typeface.BOLD);
    }


    private void setRecyclerView(){
        adapter = new StudyMentoAdapter(getActivity());
        adapter.setHasStableIds(true);
        filter = new StudyFilter(
                binding.subjectFilt.getText().toString(),
                binding.placeFilt.getText().toString());
        getData();
        binding.rCView.setAdapter(adapter);
    }

    @SuppressLint({"StaticFieldLeak", "NewApi"})
    private void getData() {
        AsyncTask<Void, Void, List<ProfileRes>> listAPI = new AsyncTask<Void, Void, List<ProfileRes>>() {
            @Override
            protected List<ProfileRes> doInBackground(Void... params) {
                Call<List<ProfileRes>> call = dataService.userMentor.getAllProfile(userId);
                try {
                        for (ProfileRes res : call.execute().body()) {
                            if (res.getNickName() != null) {  //임시 조건

                                adapter.addItem(res,filter);
                            }
                        }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(List<ProfileRes> s) {super.onPostExecute(s);}
        }.execute();


        List<ProfileRes> result = null;
/*        binding.rCView.setVisibility(View.VISIBLE);
        binding.shimmerView.stopShimmer();
        binding.shimmerView.setVisibility(View.INVISIBLE);*/

        try {
            result = listAPI.get();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
