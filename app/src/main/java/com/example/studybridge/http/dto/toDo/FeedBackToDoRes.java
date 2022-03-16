package com.example.studybridge.http.dto.toDo;

import lombok.Builder;

@Builder
public class FeedBackToDoRes {
    private Long mentorId;

    private Long toDoId;

    private String feedBack;
}
