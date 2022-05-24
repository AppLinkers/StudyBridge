package com.example.studybridge.http.dto.toDo;

import java.time.LocalDateTime;

public class UpdateToDoDueDateReq {

    private Long toDoId;

    private String dueDate;

    public UpdateToDoDueDateReq(Long toDoId, String dueDate) {
        this.toDoId = toDoId;
        this.dueDate = dueDate;
    }
}
