package com.example.studybridge.http.dto.toDo;

import lombok.Data;

@Data
public class FeedBackToDoReq {

    private Long mentorId;

    private Long toDoId;

    private String feedBack;
}
