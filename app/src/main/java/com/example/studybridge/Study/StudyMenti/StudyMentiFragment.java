package com.example.studybridge.Study.StudyMenti;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.studybridge.R;
import com.example.studybridge.Study.StudyAddActivity;
import com.example.studybridge.Study.StudyFilter;
import com.example.studybridge.Util.StudyFilterDialog;
import com.example.studybridge.databinding.StudyMentiFragmentBinding;
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.study.StudyFindRes;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;


public class StudyMentiFragment extends Fragment {

    //리사이클러
    private LinearLayoutManager linearLayoutManager;
    private StudyMentiAdapter adapter;

    private StudyFilter filter;
    public static final int STUDYFIND = 0;
    private DataService dataService = new DataService();

    ////////////////////////////////////////////////////////
    private StudyMentiFragmentBinding binding;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = StudyMentiFragmentBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        setShimmerFrameLayout(view);
        toAddStudy();

        //필터 버튼 & 텍스트

/*        statusFilter = (TextView) view.findViewById(R.id.study_statusTv);
        subjectFilter = (TextView) view.findViewById(R.id.study_typeTv);
        placeFilter = (TextView) view.findViewById(R.id.study_placeTv);*/

        //recycler
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        binding.rcView.setLayoutManager(linearLayoutManager);

        setRecyclerView();
/*        setFilterFab();*/
        setRefresh();

        return view;
    }

    private void toAddStudy(){
        binding.studyAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), StudyAddActivity.class);
                intent.putExtra(null,"study");
                startActivity(intent);
            }
        });
    }


    private void setShimmerFrameLayout(View view){

        binding.rcView.setVisibility(View.INVISIBLE);
        binding.shimmerView.startShimmer();
        binding.studyAdd.setVisibility(View.INVISIBLE);

        Handler handler = new Handler();
        handler.postDelayed(()->{
            binding.rcView.setVisibility(View.VISIBLE);
            binding.studyAdd.setVisibility(View.VISIBLE);

            binding.shimmerView.stopShimmer();
            binding.shimmerView.setVisibility(View.INVISIBLE);

        },2000);
    }
/*    //필터링
    private void setFilterFab(){
        filterFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StudyFilterDialog bottomSheet = StudyFilterDialog.getInstance(STUDYFIND);
                bottomSheet.show(getChildFragmentManager(), StudyFilterDialog.getInstance(STUDYFIND).getTag());

                bottomSheet.setDialogInterfacer(new StudyFilterDialog.DialogInterfacer() {
                    @Override
                    public void onFilterBtnClick(String status,String subject, String place) {
                        statusFilter.setText(status);
                        subjectFilter.setText(subject);
                        placeFilter.setText(place);
                    }
                });
                bottomSheet.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        adapter = new StudyMentiAdapter();
                        filter = new StudyFilter(
                                statusFilter.getText().toString(),
                                subjectFilter.getText().toString(),
                                placeFilter.getText().toString());
                        getData();
                        binding.rcView.setAdapter(adapter);
                    }
                });

            }
        });
    }*/

    private void setRefresh(){
        binding.swipeRC.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                binding.swipeRC.postDelayed(new Runnable() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        binding.rcView.setAdapter(adapter);
                        binding.swipeRC.setRefreshing(false);
                    }
                },500);

            }
        });
        binding.swipeRC.setColorSchemeColors(getResources().getColor(R.color.palletRed));
    }
    @Override
    public void onResume() {
        super.onResume();
        adapter.clearItem();
        getData();
        binding.rcView.setAdapter(adapter);
    }

    private void setRecyclerView(){

        adapter = new StudyMentiAdapter();
        filter = new StudyFilter(
                "전체",
                "전체",
                "전체");
        getData();
        binding.rcView.setAdapter(adapter);

    }


    @SuppressLint({"StaticFieldLeak", "NewApi"})
    private void getData() {
        AsyncTask<Void, Void, List<StudyFindRes>> listAPI = new AsyncTask<Void, Void, List<StudyFindRes>>() {
            @Override
            protected List<StudyFindRes> doInBackground(Void... params) {
                Call<List<StudyFindRes>> call = dataService.study.find();
                try {
                    return call.execute().body();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<StudyFindRes> s) {
                super.onPostExecute(s);
            }
        }.execute();


        List<StudyFindRes> result = null;

        try {
            result = listAPI.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result != null) {
                result.forEach(s -> {
                    adapter.addItem(s,filter);

                });

        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
