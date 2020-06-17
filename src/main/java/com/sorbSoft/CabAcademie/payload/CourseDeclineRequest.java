package com.sorbSoft.CabAcademie.payload;

import lombok.Data;

@Data
public class CourseDeclineRequest {

    private Long courseId;

    private String declineMessage;

}
