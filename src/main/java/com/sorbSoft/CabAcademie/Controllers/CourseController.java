package com.sorbSoft.CabAcademie.Controllers;


import com.sorbSoft.CabAcademie.Entities.Course;
import com.sorbSoft.CabAcademie.Entities.Error.MessageResponse;
import com.sorbSoft.CabAcademie.Entities.LanguageEntity;
import com.sorbSoft.CabAcademie.Services.CourseService;
import com.sorbSoft.CabAcademie.Services.Dtos.Validation.Result;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.CourseViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Iterator;
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
    public ResponseEntity<CourseViewModel> getCourseViewModel(@PathVariable Long id){
        if(id<0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        CourseViewModel vm = courseService.getCourseViewModel(id);
        if(vm==null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(vm, HttpStatus.OK);
    }

    @GetMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CourseViewModel> getCourseViewModel(){
        CourseViewModel vm = courseService.getCourseViewModel(null);
        if(vm==null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(vm, HttpStatus.OK);
    }

    // TODO: RETURN COURSE INFO IN A STANDARD WAY NOT ALL OBJECT FOR JUST A SINGLE STRING
    @GetMapping(value = "/all" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Course>> getAllCourses(){
        List<Course> courses = courseService.fetchAllCourses();
        if(courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @GetMapping(value = "/subSection/{id}" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Course>> getAllCoursesBySubSection(@PathVariable Long id){
        List<Course> courses = courseService.fetchCourseBySubSection(id);
        if(courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @GetMapping(value = "/subCategory/{id}" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Course>> getAllCoursesBySubCategory(@PathVariable Long id){
        List<Course> courses = courseService.fetchCourseBySubCategory(id);
        if(courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    /**
     * Get All Courses Paginate
     * * @param count
     * * @param page
     *
     * @return courses
     */

    @GetMapping(value="/paginate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<Course>> getAllCoursesByPage(@RequestParam(value = "count", defaultValue = "10") String count,
                                                            @RequestParam(value = "page", defaultValue = "1") String page){
        Page<Course> courses = courseService.fetchAllCoursesByPage(Integer.valueOf(page), Integer.valueOf(count));
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    public  ResponseEntity<MessageResponse> saveCourse(@Valid @RequestBody CourseViewModel vm){
        Result result = courseService.saveCourse(vm);
        if(!result.isValid())
            return new ResponseEntity<>(MessageResponse.of(result.lista.get(0).getMessage()), HttpStatus.CONFLICT);
        return  new ResponseEntity<>(MessageResponse.of("Course successfully saved"), HttpStatus.OK);
    }

    //Added this
    @PutMapping(value = "/update")
    public ResponseEntity<Course> updateCourse(@RequestBody CourseViewModel courseVm){

        //TODO: Validators should be implemenmted as seperate layer
        if(courseVm == null
                || courseVm.getId()==null)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Long id = courseVm.getId();
        if(!courseService.exists(id))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);


        Result result = courseService.updateCourse(courseVm);

        if (result == null)
            return  new ResponseEntity<>(HttpStatus.NOT_MODIFIED);

        return new ResponseEntity(result, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<MessageResponse> deleteCourse(@PathVariable Long id ){
        Result result = courseService.deleteCourse(id);
        if (!result.isValid()){
            return new ResponseEntity<>(MessageResponse.of(result.lista.get(0).getMessage()), HttpStatus.CONFLICT);
        }
        return  new ResponseEntity<>(MessageResponse.of("Section successfully deleted"), HttpStatus.OK);
    }
}
