package com.example.studybridge.http.dto.userMentee;

import android.os.Parcel;
import android.os.Parcelable;

public class LikeMentorRes implements Parcelable {

    private Long menteeId;

    private Long mentorId;

    public LikeMentorRes(Parcel in){
        readFromParcel(in);
    }

    @Override
    public String toString() {
        return "LikeMentorRes{" +
                "menteeId=" + menteeId +
                ", mentorId=" + mentorId +
                '}';
    }

    public Long getMenteeId() {
        return menteeId;
    }

    public Long getMentorId() {
        return mentorId;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(menteeId);
        dest.writeLong(mentorId);
    }

    private void readFromParcel(Parcel in){
        menteeId = in.readLong();
        mentorId = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LikeMentorRes> CREATOR = new Creator<LikeMentorRes>() {
        @Override
        public LikeMentorRes createFromParcel(Parcel in) {
            return new LikeMentorRes(in);
        }

        @Override
        public LikeMentorRes[] newArray(int size) {
            return new LikeMentorRes[size];
        }
    };
}
