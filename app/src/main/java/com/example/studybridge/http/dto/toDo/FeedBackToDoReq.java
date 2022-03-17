package com.example.studybridge.http.dto.toDo;


public class FeedBackToDoReq {

    private Long mentorId;

    private Long toDoId;

    private String feedBack;

    public FeedBackToDoReq(Long mentorId, Long toDoId, String feedBack) {
        this.mentorId = mentorId;
        this.toDoId = toDoId;
        this.feedBack = feedBack;
    }
}
