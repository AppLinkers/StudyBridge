package com.example.studybridge.http.dto.toDo;


public class FindToDoRes {

    private Long id;

    private Long studyId;

    private String task;

    private String explain;

    private String dueDate;

    public Long getId() {
        return id;
    }

    public Long getStudyId() {
        return studyId;
    }

    public String getTask() {
        return task;
    }

    public String getExplain() {
        return explain;
    }

    public String getDueDate() {
        return dueDate;
    }
}
