package com.sorbSoft.CabAcademie.Controladores;


import com.sorbSoft.CabAcademie.Entities.Course;
import com.sorbSoft.CabAcademie.Services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by Dany on 20/05/2018.
 */
@RestController
@RequestMapping(value = "/api/course")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<Course> getCourse(@PathVariable Long id){
        if(id<0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Course course = courseService.fetchCourse(id);
        if(course==null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(course, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<Course>> getAllCourses(){
        List<Course> courses = courseService.fetchAllCourses();
        if(courses.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @GetMapping(value={"/{page}/{itemsPerPage}"})
    public ResponseEntity<Page<Course>> getAllCoursesByPage(@PathVariable int  page, @PathVariable int itemsPerPage){
        Page<Course> courses = courseService.fetchAllCoursesByPage(page,itemsPerPage);
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @GetMapping(value={"/{page}/{itemsPerPage}/{searchText}"})
    public ResponseEntity<Page<Course>> getAllCoursesByPageAndSearchText(@PathVariable int  page, @PathVariable int itemsPerPage, @PathVariable String searchText){
        Page<Course> courses = courseService.fetchAllCoursesByPageAndSerchText(page,itemsPerPage,searchText);
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @PostMapping()
    public  ResponseEntity<Course> saveCourse(@Valid @RequestBody Course course){
        Course currentCourse = courseService.saveCourse(course);
        if(currentCourse==null)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return  new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable Long id, @RequestBody Course course){
        if(id<0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(courseService.fetchCourse(id)==null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Course currentCourse = courseService.updateCourse(course);
        if (currentCourse==null)
            return  new ResponseEntity<>(HttpStatus.NOT_MODIFIED);

        return new ResponseEntity<>(currentCourse, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id ){
        if(id<0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(courseService.fetchCourse(id)==null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        courseService.deleteCourse(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
