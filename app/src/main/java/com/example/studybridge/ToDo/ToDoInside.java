package com.example.studybridge.ToDo;

public class ToDoInside {

    private Long id;
    private String title;
    private String status;
    private String due;
    private String catagory;

    public ToDoInside(String title, String due, String status, String catagory) {

        this.title = title;
        this.due = due;
        this.status=status;
        this.catagory=catagory;
    }

    public String getTitle() {
        return title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDue() {
        return due;
    }

    public void setDue(String due) {
        this.due = due;
    }

    public String getCatagory() {
        return catagory;
    }

    public void setCatagory(String catagory) {
        this.catagory = catagory;
    }
}
