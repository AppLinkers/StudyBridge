package com.example.studybridge.http.dto.toDo;

public class ConfirmToDoReq {

    private Long mentorId;

    private Long toDoId;

    public ConfirmToDoReq(Long mentorId, Long toDoId) {
        this.mentorId = mentorId;
        this.toDoId = toDoId;
    }
}
