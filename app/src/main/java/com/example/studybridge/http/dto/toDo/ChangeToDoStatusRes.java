package com.example.studybridge.http.dto.toDo;

public class ChangeToDoStatusRes {

    private Long menteeId;

    private Long toDoId;

    private ToDoStatus status;

    public Long getMenteeId() {
        return menteeId;
    }

    public Long getToDoId() {
        return toDoId;
    }

    public ToDoStatus getStatus() {
        return status;
    }
}
