package com.example.studybridge.http.dto.assignedToDo;

import com.example.studybridge.http.dto.toDo.ToDoStatus;

public class ChangeToDoStatusRes {

    private Long menteeId;

    private Long assignedToDoId;

    private ToDoStatus status;

    public Long getMenteeId() {
        return menteeId;
    }

    public Long getAssignedToDoId() {
        return assignedToDoId;
    }

    public ToDoStatus getStatus() {
        return status;
    }
}
