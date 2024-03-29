package com.example.studybridge.http.dto.toDo;

import java.time.LocalDateTime;

public class AssignToDoReq {

    private Long studyId;

    private Long mentorId;

    private String task;

    private String explain;

    private String dueDate;

    public AssignToDoReq(Long studyId, Long mentorId, String task, String explain, String dueDate) {
        this.studyId = studyId;
        this.mentorId = mentorId;
        this.task = task;
        this.explain = explain;
        this.dueDate = dueDate;
    }
}
