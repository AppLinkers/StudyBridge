package com.example.studybridge.Study.StudyMenti;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.studybridge.R;
import com.example.studybridge.Study.StudyAddActivity;
import com.example.studybridge.Study.StudyFilter;
import com.example.studybridge.Util.FilterDialog;
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
    private DataService dataService = new DataService();

    ////////////////////////////////////////////////////////
    private StudyMentiFragmentBinding binding;
    public static final int STATUS = 0;
    public static final int SUBJECT = 1;
    public static final int PLACE = 2;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = StudyMentiFragmentBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        setShimmerFrameLayout();
        toAddStudy();

        //필터 버튼 & 텍스트

        //recycler
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        binding.rcView.setLayoutManager(linearLayoutManager);

        setRecyclerView();

        setFilter(binding.statusFilt,STATUS);
        setFilter(binding.subjectFilt,SUBJECT);
        setFilter(binding.placeFilt,PLACE);
        showAll(binding.statusFilt,binding.subjectFilt,binding.placeFilt);

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


    private void setShimmerFrameLayout(){

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
                        if(filter.equals("상태별")||filter.equals("과목별")||filter.equals("지역별")){
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
                        adapter = new StudyMentiAdapter(getActivity());
                        filter = new StudyFilter(
                                binding.statusFilt.getText().toString(),
                                binding.subjectFilt.getText().toString(),
                                binding.placeFilt.getText().toString());
                        getData();
                        binding.rcView.setAdapter(adapter);
                    }
                });
            }
        });
    }

    private void showAll(TextView status,TextView subject, TextView place){
        binding.filtAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status.setText("상태별");
                subject.setText("과목별");
                place.setText("지역별");
                filterUnClick(status);
                filterUnClick(subject);
                filterUnClick(place);

                adapter = new StudyMentiAdapter(getActivity());
                filter = new StudyFilter(
                        binding.statusFilt.getText().toString(),
                        binding.subjectFilt.getText().toString(),
                        binding.placeFilt.getText().toString());
                getData();
                binding.rcView.setAdapter(adapter);
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

        adapter = new StudyMentiAdapter(getActivity());
        filter = new StudyFilter(
                binding.statusFilt.getText().toString(),
                binding.subjectFilt.getText().toString(),
                binding.placeFilt.getText().toString());
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
