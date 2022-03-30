package com.example.studybridge.http.dto.assignedToDo;

public class FindAssignedToDoRes {

    private Long id;

    private Long toDoId;

    private Long studyId;

    private Long menteeId;

    private String menteeName;

    private String mentorName;

    private String task;

    private String explain;

    private String dueDate;

    private String status;

    public Long getId() {
        return id;
    }

    public Long getToDoId() {
        return toDoId;
    }

    public Long getStudyId() {
        return studyId;
    }

    public Long getMenteeId() {
        return menteeId;
    }

    public String getMenteeName() {
        return menteeName;
    }

    public String getMentorName() {
        return mentorName;
    }

    public String getTask() {
        return task;
    }

    public String getExplain() {
        return explain;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getStatus() {
        return status;
    }
}
