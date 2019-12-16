package com.sorbSoft.CabAcademie.Services;


import com.sorbSoft.CabAcademie.Entities.Course;
import com.sorbSoft.CabAcademie.Entities.Syllabus;
import com.sorbSoft.CabAcademie.Repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by Dany on 16/05/2018.
 */
@Service
@Transactional
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private SyllabusService syllabusService;

    public List<Course> fetchAllCourses(){
        return courseRepository.findAll();
    }

    public Page<Course> fetchAllCoursesByPage(int page, int itemsPerPage){
        Pageable pageable = new PageRequest(page, itemsPerPage);
        return courseRepository.findAll(pageable);
    }

    public Page<Course> fetchAllCoursesByPageAndSerchText(int page, int itemsPerPage, String searchText){
        Pageable pageable = new PageRequest(page, itemsPerPage);
        return courseRepository.findByTitleContainsAllIgnoreCase(searchText, pageable);
    }

    public Course fetchCourse(Long id){
        return courseRepository.findOne(id);
    }


    public Course updateCourse (Course course){
        Course currentCourse= courseRepository.findOne(course.getId());
        currentCourse.setCategory(course.getCategory());
        currentCourse.setDuration(course.getDuration());
        currentCourse.setPremium(course.isPremium());
        currentCourse.setPrice(course.getPrice());
        currentCourse.setStartDate(course.getStartDate());
        currentCourse.setSyllabus(course.getSyllabus());
        currentCourse.setTitle(course.getTitle());
        currentCourse.setUser(course.getUser());
        return courseRepository.save(currentCourse);
    }
    public Course saveCourse (Course course)
    {
        Syllabus syllabus = syllabusService.saveSyllabus(course.getSyllabus());
        course.setSyllabus(syllabus);
        return courseRepository.save(course);
    }
    public void deleteCourse(Long id){
        courseRepository.delete(id);
    }
    //other delete methods
    //other fetching methods
}
