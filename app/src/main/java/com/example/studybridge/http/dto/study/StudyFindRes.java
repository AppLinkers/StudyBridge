package com.example.studybridge.http.dto.study;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.cardview.widget.CardView;

import com.example.studybridge.Study.StudyFilter;

import java.io.Serializable;

public class StudyFindRes implements Parcelable {

    private Long id;

    private String name;

    private String info;

    private String explain;

    private Integer maxNum;

    private String status;

    private String place;

    private String type;

    private int menteeCnt;

    public StudyFindRes(Parcel in) {
        readFromParcel(in);
    }


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getInfo() {
        return info;
    }

    public String getExplain() {
        return explain;
    }

    public Integer getMaxNum() {
        return maxNum;
    }

    public String getStatus() {
        return status;
    }

    public String getPlace() {
        return place;
    }

    public String getType() {
        return type;
    }

    public int getMenteeCnt() {
        return menteeCnt;
    }

    public String statusSet(CardView card){
        if(this.status.equals("APPLY")){
            card.setCardBackgroundColor(Color.parseColor("#FF03DAC5"));
            return "멘티 모집중";
        }
        else if(this.status.equals("WAIT")){
            card.setCardBackgroundColor(Color.parseColor("#FBB8AC"));
            return "멘토 모집중";
        } else{
            card.setCardBackgroundColor(Color.parseColor("#E0E0E0"));
            return "모집 종료";
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(name);
        parcel.writeString(info);
        parcel.writeString(explain);
        parcel.writeInt(maxNum);
        parcel.writeString(status);
        parcel.writeString(place);
        parcel.writeString(type);
        parcel.writeInt(menteeCnt);
    }

    private void readFromParcel(Parcel in){
        id = in.readLong();
        name = in.readString();
        info = in.readString();
        explain = in.readString();
        maxNum = in.readInt();
        status = in.readString();
        place = in.readString();
        type = in.readString();
        menteeCnt = in.readInt();
    }

    public static final Creator<StudyFindRes> CREATOR = new Creator<StudyFindRes>() {
        @Override
        public StudyFindRes createFromParcel(Parcel in) {
            return new StudyFindRes(in);
        }

        @Override
        public StudyFindRes[] newArray(int size) {
            return new StudyFindRes[size];
        }
    };
}
