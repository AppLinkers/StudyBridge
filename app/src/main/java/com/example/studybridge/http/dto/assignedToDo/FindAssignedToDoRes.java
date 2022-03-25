package com.example.studybridge.http.dto.assignedToDo;

import java.time.LocalDateTime;

public class FindAssignedToDoRes {

    private Long id;

    private Long toDoId;

    private Long studyId;

    private Long menteeId;

    private String task;

    private String explain;

    private LocalDateTime dueDate;

    private String feedBack;

    private String status;

    public Long getId() {
        return id;
    }

    public Long getToDoId() {
        return toDoId;
    }

    public Long getStudyId() {
        return studyId;
    }

    public Long getMenteeId() {
        return menteeId;
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

    public String getFeedBack() {
        return feedBack;
    }

    public String getStatus() {
        return status;
    }
}
