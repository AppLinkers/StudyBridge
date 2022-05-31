package com.example.studybridge.http.dto.message;

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

    public String getMessageType() {
        return messageType;
    }

    public Room getRoom() {
        return room;
    }

    public Long getSenderId() {
        return senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", messageType='" + messageType + '\'' +
                ", room=" + room +
                ", senderId=" + senderId +
                ", senderName='" + senderName + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
