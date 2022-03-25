package com.example.studybridge.ToDo;

import java.io.Serializable;

public class ToDo implements Serializable {

    private Long studyId, todoId;
    private String status;
    private String mentoId,mentiId,taskName,taskInfo,dueDate,feedBack;

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
}
