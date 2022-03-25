package com.example.studybridge.http.dto.assignedToDo;


public class FeedBackToDoReq {

    private Long mentorId;

    private Long assignedToDoId;

    private String feedBack;

    public FeedBackToDoReq(Long mentorId, Long assignedToDoId, String feedBack) {
        this.mentorId = mentorId;
        this.assignedToDoId = assignedToDoId;
        this.feedBack = feedBack;
    }
}
