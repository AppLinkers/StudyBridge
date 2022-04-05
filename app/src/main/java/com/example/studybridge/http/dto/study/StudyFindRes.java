package com.example.studybridge.http.dto.study;

import android.graphics.Color;

import androidx.cardview.widget.CardView;

import com.example.studybridge.Study.StudyFilter;

import java.io.Serializable;

public class StudyFindRes implements Serializable {

    private Long id;

    private String name;

    private String info;

    private String explain;

    private Integer maxNum;

    private String status;

    private String place;

    private String type;

    private int menteeCnt;


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

}
