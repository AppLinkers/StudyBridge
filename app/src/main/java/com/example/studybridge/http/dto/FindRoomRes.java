package com.example.studybridge.http.dto;

public class FindRoomRes {

    private Long roomId;

    private Long studyId;

    public FindRoomRes(Long roomId, Long studyId) {
        this.roomId = roomId;
        this.studyId = studyId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Long getStudyId() {
        return studyId;
    }

    public void setStudyId(Long studyId) {
        this.studyId = studyId;
    }
}
