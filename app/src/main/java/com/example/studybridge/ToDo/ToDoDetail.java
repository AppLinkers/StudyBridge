package com.example.studybridge.ToDo;

public class ToDoDetail {

    String todoStatus;

    public ToDoDetail(String todoStatus) {
        this.todoStatus = todoStatus;
    }

    public String getTodoStatus() {
        return todoStatus;
    }

    public void setTodoStatus(String todoStatus) {
        this.todoStatus = todoStatus;
    }
}
