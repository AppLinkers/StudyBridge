package com.example.studybridge.ToDo.Mento.Inside;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybridge.R;
import com.example.studybridge.ToDo.Mento.Inside.Detail.ToDoMentoInsideDetailAdapter;
import com.example.studybridge.Util.BackDialog;
import com.example.studybridge.databinding.TodoMentorInsideItemBinding;
import com.example.studybridge.http.DataService;
import com.example.studybridge.http.dto.assignedToDo.FindAssignedToDoRes;
import com.example.studybridge.http.dto.toDo.FindToDoRes;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ToDoMentoInsideAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<FindToDoRes> todoAssigned;
    private Activity activity;

    public ToDoMentoInsideAdapter(List<FindToDoRes> todoAssigned,Activity activity) {
        this.todoAssigned = todoAssigned;
        this.activity = activity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_mentor_inside_item, parent, false);
        return new ToDoMentorInsdieHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ((ToDoMentorInsdieHolder) holder).onBind(todoAssigned.get(position),activity);

    }
    public void deleteItem(int position){
        todoAssigned.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,todoAssigned.size());
    }


    @Override
    public int getItemCount() {
        return todoAssigned.size();
    }


    ////////////////////////////////////////////////////////////

    public class ToDoMentorInsdieHolder extends RecyclerView.ViewHolder{

        private TodoMentorInsideItemBinding binding;

        private LinearLayoutManager linearLayoutManager;
        private ToDoMentoInsideDetailAdapter adapter;
        private DataService dataService = new DataService();
        private FindToDoRes todo;
        private Activity activity;

        public final int ONE_DAY = 24 * 60 * 60 * 1000;
        private long dayResult;

        public ToDoMentorInsdieHolder(@NonNull View itemView) {
            super(itemView);
            binding = TodoMentorInsideItemBinding.bind(itemView);


            binding.con.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(binding.RCView.getVisibility()==View.GONE){
                        //안보일 때
                        setRecyclerView();
                    } else if(binding.RCView.getVisibility()==View.VISIBLE){
                        //보일 때
                        binding.RCView.setVisibility(View.GONE);
                        binding.arrow.setImageResource(R.drawable.ic_arrow_down);

                    }
                }
            });

            binding.con.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    setAlertDialog();

                    return true;
                }
            });
        }

        public void onBind(FindToDoRes data, Activity activity){
            todo = data;
            binding.taskName.setText(data.getTask());
            binding.explain.setText(data.getExplain());
            binding.dueDate.setText(getDday(data.getDueDate()));
            this.activity = activity;

        }


        private void setRecyclerView(){

            dataService = new DataService();
            binding.RCView.setVisibility(View.VISIBLE);
            binding.arrow.setImageResource(R.drawable.ic_arrow_up);
            linearLayoutManager = new LinearLayoutManager(itemView.getContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            binding.RCView.setLayoutManager(linearLayoutManager);

            getData();

        }

        private void getData(){
            dataService.assignedToDo.findByToDo(todo.getId()).enqueue(new Callback<List<FindAssignedToDoRes>>() {
                @Override
                public void onResponse(Call<List<FindAssignedToDoRes>> call, Response<List<FindAssignedToDoRes>> response) {
                    adapter = new ToDoMentoInsideDetailAdapter(response.body(),activity);
                    binding.RCView.setAdapter(adapter);
                }

                @Override
                public void onFailure(Call<List<FindAssignedToDoRes>> call, Throwable t) {

                }
            });
        }
        private void setAlertDialog(){

            BackDialog dialog =BackDialog.getInstance(1);
            FragmentManager fm = ((AppCompatActivity)itemView.getContext()).getSupportFragmentManager();
            dialog.show(fm,"delete");

            dialog.setBackInterface(new BackDialog.BackInterface() {
                @Override
                public void okBtnClick() {
                    dataService.toDo.delete(todo.getId()).enqueue(new Callback<Integer>() {
                        @Override
                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                            if(response.isSuccessful()){
                                deleteItem(getAdapterPosition());
                                Toast.makeText(itemView.getContext(), "삭제되었습니다",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Integer> call, Throwable t) {

                        }
                    });
                }
            });
        }

        private String getDday(String duedate){

            final int year,month,day;
            StringTokenizer st = new StringTokenizer(duedate,"-");
            year = Integer.parseInt(st.nextToken());
            month = Integer.parseInt(st.nextToken());
            day = Integer.parseInt(st.nextToken().substring(0,2));

            final Calendar ddayCal = Calendar.getInstance();
            ddayCal.set(year,month-1,day);

            final long dday = ddayCal.getTimeInMillis()/ONE_DAY;
            final long today = Calendar.getInstance().getTimeInMillis()/ONE_DAY;
            dayResult = dday - today;

            // 출력 시 d-day 에 맞게 표시
            final String strFormat;
            if (dayResult > 0) {
                strFormat = "D-%d";
            } else if (dayResult == 0) {
                strFormat = "D-Day";
            } else {
                strFormat = "마감";
                binding.dueDate.setTextColor(ContextCompat.getColor(itemView.getContext(),R.color.redDark));
            }

            final String strCount = (String.format(strFormat,dayResult));

            return strCount;
        }
    }


}
