package com.example.studybridge.http.dto.toDo;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AssignToDoRes {

    private Long studyId;

    private Integer menteeCnt;

}
