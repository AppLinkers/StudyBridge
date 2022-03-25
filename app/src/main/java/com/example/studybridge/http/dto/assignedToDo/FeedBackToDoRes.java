package com.example.studybridge.http.dto.assignedToDo;

public class FeedBackToDoRes {
    private Long mentorId;

    private Long assignedToDoId;

    private String feedBack;

    public Long getMentorId() {
        return mentorId;
    }

    public Long getAssignedToDoId() {
        return assignedToDoId;
    }

    public String getFeedBack() {
        return feedBack;
    }
}
