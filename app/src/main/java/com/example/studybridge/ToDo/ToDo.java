package com.example.studybridge.ToDo;

import android.os.Parcel;
import android.os.Parcelable;

public class ToDo implements Parcelable {

    private Long studyId, todoId;
    private String status;
    private String mentoId,mentiId,taskName,taskInfo,dueDate,feedBack;

    public ToDo(Parcel in) {
        readFromParcel(in);
    }

    // constructor
    public ToDo(Long todoId,Long studyId, String status, String mentoId, String mentiId, String taskName, String taskInfo, String dueDate, String feedBack) {
        this.todoId = todoId;
        this.studyId = studyId;
        this.status = status;
        this.mentoId = mentoId;
        this.mentiId = mentiId;
        this.taskName = taskName;
        this.taskInfo = taskInfo;
        this.dueDate = dueDate;
        this.feedBack = feedBack;
    }

    // getter & setter

    public Long getTodoId() {
        return todoId;
    }

    public void setTodoId(Long todoId) {
        this.todoId = todoId;
    }

    public Long getStudyId() {
        return studyId;
    }

    public void setStudyId(Long studyId) {
        this.studyId = studyId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMentoId() {
        return mentoId;
    }

    public void setMentoId(String mentoId) {
        this.mentoId = mentoId;
    }

    public String getMentiId() {
        return mentiId;
    }

    public void setMentiId(String mentiId) {
        this.mentiId = mentiId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskInfo() {
        return taskInfo;
    }

    public void setTaskInfo(String taskInfo) {
        this.taskInfo = taskInfo;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getFeedBack() {
        return feedBack;
    }

    public void setFeedBack(String feedBack) {
        this.feedBack = feedBack;
    }


    ////////메서드
    public String setStatusName(int status){
        if(status==0){
            return "Ready";
        } else if(status==1){
            return "Progress";
        } else if(status==2){
            return "Done";
        } else
            return "Confirm";
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(todoId);
        dest.writeLong(studyId);
        dest.writeString(status);
        dest.writeString(mentoId);
        dest.writeString(mentiId);
        dest.writeString(taskName);
        dest.writeString(taskInfo);
        dest.writeString(dueDate);
        dest.writeString(feedBack);
    }

    private void readFromParcel(Parcel in){
        todoId = in.readLong();
        studyId = in.readLong();
        status = in.readString();
        mentoId = in.readString();
        mentiId = in.readString();
        taskName = in.readString();
        taskInfo = in.readString();
        dueDate = in.readString();
        feedBack = in.readString();
    }

    public static final Creator<ToDo> CREATOR = new Creator<ToDo>() {
        @Override
        public ToDo createFromParcel(Parcel in) {
            return new ToDo(in);
        }

        @Override
        public ToDo[] newArray(int size) {
            return new ToDo[size];
        }
    };
}
