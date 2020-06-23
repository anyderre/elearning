package com.sorbSoft.CabAcademie.Repository;

import com.sorbSoft.CabAcademie.Entities.*;
import com.sorbSoft.CabAcademie.Entities.Enums.CourseStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseSchoolRepository extends JpaRepository<CourseSchool, Long> {

    long countCoursesBySchool(User school);
    long countCoursesBySchoolAndStatus(User school, CourseStatus status);

    long countCoursesBySchoolAndTeacherAndStatus(User school, User teacher, CourseStatus status);

}
