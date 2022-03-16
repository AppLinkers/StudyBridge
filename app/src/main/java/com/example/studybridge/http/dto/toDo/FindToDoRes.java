package com.example.studybridge.http.dto.toDo;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public class FindToDoRes {

    private Long id;

    private Long menteeId;

    private Long studyId;

    private String task;

    private LocalDateTime dueDate;

    private String feedBack;

    private ToDoStatus toDoStatus;
}
