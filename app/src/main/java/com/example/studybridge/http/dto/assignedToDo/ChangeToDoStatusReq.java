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

    public Long getMenteeId() {
        return menteeId;
    }

    public Long getAssignedToDoId() {
        return assignedToDoId;
    }

    public String getStatus() {
        return status;
    }
}
