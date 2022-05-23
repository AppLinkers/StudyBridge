package com.example.studybridge.http.dto.toDo;

import java.time.LocalDateTime;

public class UpdateToDoDueDateReq {

    private Long toDoId;

    private LocalDateTime dueDate;

    public UpdateToDoDueDateReq(Long toDoId, LocalDateTime dueDate) {
        this.toDoId = toDoId;
        this.dueDate = dueDate;
    }
}
