package com.example.studybridge.http.dto.toDo;

public class ConfirmToDoRes {

    private Long mentorId;

    private Long toDoId;

    private ToDoStatus toDoStatus;

    public Long getMentorId() {
        return mentorId;
    }

    public Long getToDoId() {
        return toDoId;
    }

    public ToDoStatus getToDoStatus() {
        return toDoStatus;
    }
}
