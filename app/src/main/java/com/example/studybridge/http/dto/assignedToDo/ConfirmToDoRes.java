package com.example.studybridge.http.dto.assignedToDo;

import com.example.studybridge.http.dto.toDo.ToDoStatus;

public class ConfirmToDoRes {

    private Long mentorId;

    private Long assignedToDoId;

    private ToDoStatus toDoStatus;

    public Long getMentorId() {
        return mentorId;
    }

    public Long getAssignedToDoId() {
        return assignedToDoId;
    }

    public ToDoStatus getToDoStatus() {
        return toDoStatus;
    }
}
