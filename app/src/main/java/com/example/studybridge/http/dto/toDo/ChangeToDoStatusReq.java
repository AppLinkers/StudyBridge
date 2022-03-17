package com.example.studybridge.http.dto.toDo;

public class ChangeToDoStatusReq {

    private Long menteeId;

    private Long toDoId;

    private String status;

    public ChangeToDoStatusReq(Long menteeId, Long toDoId, String status) {
        this.menteeId = menteeId;
        this.toDoId = toDoId;
        this.status = status;
    }
}
