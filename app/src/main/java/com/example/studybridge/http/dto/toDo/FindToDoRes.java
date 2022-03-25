package com.example.studybridge.http.dto.toDo;

import java.time.LocalDateTime;

public class FindToDoRes {

    private Long id;

    private Long studyId;

    private String task;

    private String explain;

    private LocalDateTime dueDate;

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

    public LocalDateTime getDueDate() {
        return dueDate;
    }
}
