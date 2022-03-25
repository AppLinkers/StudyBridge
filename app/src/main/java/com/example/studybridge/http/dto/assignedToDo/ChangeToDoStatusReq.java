package com.example.studybridge.http.dto.assignedToDo;

public class ChangeToDoStatusReq {

    private Long menteeId;

    private Long assignedToDoId;

    private String status;

    public ChangeToDoStatusReq(Long menteeId, Long assignedToDoId, String status) {
        this.menteeId = menteeId;
        this.assignedToDoId = assignedToDoId;
        this.status = status;
    }
}
