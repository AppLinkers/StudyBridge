package com.example.studybridge.http.dto.toDo;

import lombok.Data;

@Data
public class ChangeToDoStatusReq {

    private Long menteeId;

    private Long toDoId;

    private String status;
}
