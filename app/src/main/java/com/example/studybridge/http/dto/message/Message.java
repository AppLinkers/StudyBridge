package com.example.studybridge.http.dto.message;

import com.example.studybridge.http.dto.message.Room;

public class Message {

    private Long id;

    private String messageType;

    private Room room;

    private Long senderId; // 채팅 전송자 식별자

    private String senderName; // 채팅 전송자 아이디

    private String message; // 채팅 본문

    public Message(String messageType, Room room, Long senderId, String senderName, String message) {
        this.messageType = messageType;
        this.room = room;
        this.senderId = senderId;
        this.senderName = senderName;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
