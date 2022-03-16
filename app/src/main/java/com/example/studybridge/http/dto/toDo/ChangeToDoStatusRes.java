package com.example.studybridge.http.dto.toDo;

import lombok.Builder;

@Builder
public class ChangeToDoStatusRes {

    private Long menteeId;

    private Long toDoId;

    private ToDoStatus status;
}
