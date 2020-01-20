package com.sorbSoft.CabAcademie.Repository;


import com.sorbSoft.CabAcademie.Entities.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Dany on 15/05/2018.
 */
public interface CourseRepository extends JpaRepository<Course, Long> {

    Page<Course> findAll(Pageable pagin);
    Course findCourseByTitle(String name);
    Course findCourseByTitleAndIdIsNot(String title, Long id);
    Page<Course> findByTitleContainsAllIgnoreCase(String searchText, Pageable pagin);
}
