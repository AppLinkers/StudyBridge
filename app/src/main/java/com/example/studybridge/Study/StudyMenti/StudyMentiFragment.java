package com.example.studybridge.Study.StudyMenti;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.loader.content.AsyncTaskLoader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.R;
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.StudyFindRes;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class StudyMentiFragment extends Fragment {
    //리사이클러
    private RecyclerView recyclerView;
    private StudyMentiAdapter adapter;
    ArrayList<StudyMenti> arrayList;
    private FloatingActionButton mentiFab;
    private RelativeLayout mentiFilter;
    private Chip subjectChip,placeChip;

    private DataService dataService = new DataService();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.study_menti_fragment,container,false);

        //recycler
        recyclerView = (RecyclerView) view.findViewById(R.id.study_menti_RCView);
        arrayList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new StudyMentiAdapter();
        getData();
        recyclerView.setAdapter(adapter);

        //study add btn
        mentiFab = (FloatingActionButton) view.findViewById(R.id.menti_addBtn);
        mentiFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), StudyAddActivity.class);
                startActivity(intent);
            }
        });

        //필터 버튼
        mentiFilter = (RelativeLayout) view.findViewById(R.id.menti_filter);
        mentiFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),StudyMentiFilterActivity.class);
                startActivityForResult(intent,101);
            }
        });

        return view;
    }

    //필터에서 가져온 데이터 처리
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        StringBuilder sb;

        if (requestCode == 101){

            sb = new StringBuilder();

            subjectChip = getView().findViewById(R.id.study_menti_subjectFilter);
            placeChip = getView().findViewById(R.id.study_menti_placeFilter);

            sb.append("과목: ").append(data.getStringExtra("subject"));
            String str = sb.toString();
            subjectChip.setText(str);


            Toast.makeText(getContext(),data.getStringExtra("subject"),Toast.LENGTH_SHORT).show();
            Toast.makeText(getContext(),data.getStringExtra("place"),Toast.LENGTH_SHORT).show();

        }
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

                String status = s.getStatus();
                int status_int;

                if (status.equals("APPLY")) {
                    status_int = 0;
                } else if (status.equals("WAIT")) {
                    status_int = 1;
                } else if (status.equals("MATCHED")) {
                    status_int = 2;
                } else {
                    status_int = 3;
                }

                StudyMenti studyMenti = new StudyMenti(status_int, s.getType(), s.getPlace(), s.getName(), s.getInfo(), s.getMaxNum());
                Log.d("test", studyMenti.toString());

                adapter.addItem(studyMenti);

            });
        }


    }


}
