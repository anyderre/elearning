package com.sorbSoft.CabAcademie.Repository;

import com.sorbSoft.CabAcademie.Entities.Course;
import com.sorbSoft.CabAcademie.Entities.CourseSchool;
import com.sorbSoft.CabAcademie.Entities.Enums.CourseStatus;
import com.sorbSoft.CabAcademie.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseSchoolRepository extends JpaRepository<CourseSchool, Long> {

    long countCoursesBySchool(User school);
    List<CourseSchool> findAllBySchool(User school);
    long countAllBy();
    long countCoursesByStatus(CourseStatus status);
    long countCoursesBySchoolAndStatus(User school, CourseStatus status);

    long countCoursesBySchoolAndTeacherAndStatus(User school, User teacher, CourseStatus status);

    List<CourseSchool> findAllByCourseAndTeacher(Course course, User teacher);
    void deleteAllByCourseAndTeacher(Course course, User teacher);

    CourseSchool findOneByCourseAndTeacherAndSchool(Course existingCourse, User teacher, User schoolPersistent);
}
