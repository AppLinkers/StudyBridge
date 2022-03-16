package com.example.studybridge.http.dto.toDo;

import lombok.Builder;

@Builder
public class ConfirmToDoRes {

    private Long mentorId;

    private Long toDoId;

    private ToDoStatus toDoStatus;
}
