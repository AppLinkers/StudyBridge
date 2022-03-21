package com.example.studybridge.http.dto.toDo;

import java.time.LocalDateTime;

public class FindToDoRes {

    private Long id;

    private Long menteeId;

    private Long studyId;

    private String task;

    private LocalDateTime dueDate;

    private String feedBack;

    private ToDoStatus toDoStatus;

    public Long getId() {
        return id;
    }

    public Long getMenteeId() {
        return menteeId;
    }

    public Long getStudyId() {
        return studyId;
    }

    public String getTask() {
        return task;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public String getFeedBack() {
        return feedBack;
    }

    public ToDoStatus getToDoStatus() {
        return toDoStatus;
    }
}
