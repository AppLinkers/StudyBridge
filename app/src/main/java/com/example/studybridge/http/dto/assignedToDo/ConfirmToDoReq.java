package com.example.studybridge.http.dto.assignedToDo;

public class ConfirmToDoReq {

    private Long mentorId;

    private Long assignedToDoId;

    public ConfirmToDoReq(Long mentorId, Long assignedToDoId) {
        this.mentorId = mentorId;
        this.assignedToDoId = assignedToDoId;
    }
}
