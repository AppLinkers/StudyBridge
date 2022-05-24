package com.example.studybridge.http.dto.assignedToDo;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.studybridge.ToDo.ToDo;

public class FindAssignedToDoRes implements Parcelable {

    private Long id;

    private Long toDoId;

    private Long studyId;

    private Long menteeId;

    private String menteeName;

    private String menteeProfileImg;

    private String mentorName;

    private String mentorProfileImg;

    private String task;

    private String explain;

    private String dueDate;

    private String status;

    public FindAssignedToDoRes(Parcel in){
        readFromParcel(in);
    }

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

    public String getMenteeProfileImg() {
        return menteeProfileImg;
    }

    public String getMentorName() {
        return mentorName;
    }

    public String getMentorProfileImg() {
        return mentorProfileImg;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeLong(toDoId);
        parcel.writeLong(studyId);
        parcel.writeLong(menteeId);
        parcel.writeString(menteeName);
        parcel.writeString(menteeProfileImg);
        parcel.writeString(mentorName);
        parcel.writeString(mentorProfileImg);
        parcel.writeString(task);
        parcel.writeString(explain);
        parcel.writeString(dueDate);
        parcel.writeString(status);
    }

    private void readFromParcel(Parcel in){
        id = in.readLong();
        toDoId = in.readLong();
        studyId = in.readLong();
        menteeId = in.readLong();
        menteeName = in.readString();
        menteeProfileImg = in.readString();
        mentorName = in.readString();
        mentorProfileImg = in.readString();
        task = in.readString();
        explain = in.readString();
        dueDate = in.readString();
        status = in.readString();
    }

    public static final Creator<FindAssignedToDoRes> CREATOR = new Creator<FindAssignedToDoRes>() {
        @Override
        public FindAssignedToDoRes createFromParcel(Parcel in) {
            return new FindAssignedToDoRes(in);
        }

        @Override
        public FindAssignedToDoRes[] newArray(int size) {
            return new FindAssignedToDoRes[size];
        }
    };
}
