package com.sorbSoft.CabAcademie.Services;


import com.sorbSoft.CabAcademie.Entities.*;
import com.sorbSoft.CabAcademie.Repository.CourseRepository;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Date;
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

    public Pair<String, Course> updateCourse(Course course){
        Course savedCourse = courseRepository.findCourseByTitleAndIdIsNot(course.getTitle(), course.getId());
        if (savedCourse != null) {
            return new Pair<>("The course name already exist for another definition", null);
        }
        Course currentCourse= courseRepository.findOne(course.getId());
        currentCourse.setEndDate(course.getEndDate());
        currentCourse.setCategory(course.getCategory());
        currentCourse.setPremium(course.isPremium());
        currentCourse.setPrice(course.getPrice());
        currentCourse.setStartDate(course.getStartDate());
        currentCourse.setUser(course.getUser());
        currentCourse.setDeleted(false);
        currentCourse.setSyllabus(course.getSyllabus());
        
        Course result = courseRepository.save(currentCourse);
        if (result == null) {
            return new Pair<>("Couldn't update the course", null);
        } else {
            return new Pair<>("Course updated successfully", result);
        }
    }

    public String deleteCourse(Long id){
        if (id <= 0L) {
            return "You should indicate the id of the course";
        }
        Course course = fetchCourse(id);
        if (course == null) {
            return "The course you want to delete doesn't exist";
        }
        course.setDeleted(true);
        return "Course successfully deleted";
    }

    public Course getCourseViewModel(){
        return new Course() {
            @Override
            public Long getId() {
                return 0L;
            }

            @Override
            public String getTitle() {
                return "";
            }

            @Override
            public User getUser() {
                return null;
            }

            @Override
            public double getPrice() {
                return 0D;
            }

            @Override
            public List<Syllabus> getSyllabus() {
                return new ArrayList<>();
            }

            @Override
            public Category getCategory() {
                return new Category(){
                    @Override
                    public String getName() {
                        return "";
                    }

                    @Override
                    public String getDescription() {
                        return "";
                    }

                };
            }

            @Override
            public boolean isPremium() {
                return true;
            }

            @Override
            public Date getStartDate() {
                return new Date();
            }

            @Override
            public Date getEndDate() {
                return new Date();
            }
        };
    }
    
    public Pair<String, Course> saveCourse(Course course){
        if (course.getId() > 0L) {
            return updateCourse(course);
        } else {
            Course savedCourse = courseRepository.findCourseByTitle(course.getTitle());

            if ( savedCourse != null){
                return new Pair<>("The course you are trying to save already exist", null);
            }
            Course result = courseRepository.save(course);
            if (result == null){
                return new Pair<>("Couldn't save the course", null);
            } else {
                return  new Pair<>("Course saved successfully", result);
            }
        }
    }
}
