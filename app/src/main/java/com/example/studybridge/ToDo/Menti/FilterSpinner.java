package com.example.studybridge.ToDo.Menti;

public class FilterSpinner {
    private Long key;
    private String value;

    public FilterSpinner(Long key, String value){
        this.key = key;
        this.value = value;
    }

    public Long getKey() {
        return key;
    }


    @Override
    public String toString() {
        return value;
    }
}
