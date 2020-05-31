package com.sorbSoft.CabAcademie.Controllers;


import com.sorbSoft.CabAcademie.Entities.Course;
import com.sorbSoft.CabAcademie.Entities.Error.MessageResponse;
import com.sorbSoft.CabAcademie.Services.CourseService;
import com.sorbSoft.CabAcademie.Services.Dtos.Validation.Result;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.CourseViewModel;
import com.sorbSoft.CabAcademie.config.JwtTokenUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dany on 20/05/2018.
 */
@RestController
@RequestMapping(value = "/api/course")
@Log4j2
public class CourseController {
    @Autowired
    private CourseService courseService;

    @Autowired
    private JwtTokenUtil tokenUtil;

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

    @GetMapping(value = "/public/subSection/{subSectionId}" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Course>> getAllPublicCoursesBySubSection(@PathVariable Long subSectionId){

        List<Course> courses = courseService.fetchPublicCourseBySubSection(subSectionId);
        if(courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @GetMapping(value = "/private/subSection/{id}" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Course>> getAllPrivateCoursesBySubSection(@PathVariable Long id, Principal principal){

        log.debug("Principal username:"+principal.getName());
        //TODO: fetch all by logged in user
        List<Course> courses = courseService.fetchPrivateCourseBySubSection(id, principal.getName());
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

    @GetMapping(value = "/lastCreated/{amount}" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Course>> getLastAddedCourses(@PathVariable Integer amount) {
        if(amount<=0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Course> courses = courseService.fetchLastAddedCourses(amount);
        if(courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);

    }

    @GetMapping(value = "/lastCreated/{amount}/category/{categoryId}" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Course>> getLastAddedByCategory(@PathVariable Integer amount, @PathVariable Long categoryId) {
        if(amount<=0 || categoryId <=0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Course> courses = courseService.fetchLastAddedByCategory(amount, categoryId);
        if(courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);

    }

    @GetMapping(value = "/lastCreated/{amount}/subCategory/{subCategoryId}" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Course>> getLastAddedBySubCategory(@PathVariable Integer amount, @PathVariable Long subCategoryId) {
        if(amount<=0 || subCategoryId <=0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Course> courses = courseService.fetchLastAddedBySubCategory(amount, subCategoryId);
        if(courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);

    }

    @GetMapping(value = "/lastCreated/{amount}/section/{sectionId}" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Course>> getLastAddedBySection(@PathVariable Integer amount, @PathVariable Long sectionId) {
        if(amount<=0 || sectionId <=0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Course> courses = courseService.fetchLastAddedBySection(amount, sectionId);
        if(courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);

    }

    @GetMapping(value = "/lastCreated/{amount}/subSection/{subSectionId}" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Course>> getLastAddedBySubSection(@PathVariable Integer amount, @PathVariable Long subSectionId) {
        if(amount<=0 || subSectionId <=0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Course> courses = courseService.fetchLastAddedBySubSection(amount, subSectionId);
        if(courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);

    }







    @GetMapping(value = "/bestRated/{amount}" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Course>> getBestRatedCourses(@PathVariable Integer amount) {
        if(amount<=0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Course> courses = courseService.fetchBestRatedCourses(amount);
        if(courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @GetMapping(value = "/bestRated/{amount}/category/{categoryId}" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Course>> getBestRatedByCategory(@PathVariable Integer amount, @PathVariable Long categoryId) {
        if(amount<=0 || categoryId <=0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Course> courses = courseService.fetchBestRatedByCategory(amount, categoryId);
        if(courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);

    }

    @GetMapping(value = "/bestRated/{amount}/subCategory/{subCategoryId}" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Course>> getBestRatedBySubCategory(@PathVariable Integer amount, @PathVariable Long subCategoryId) {
        if(amount<=0 || subCategoryId <=0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Course> courses = courseService.fetchBestRatedBySubCategory(amount, subCategoryId);
        if(courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);

    }

    @GetMapping(value = "/bestRated/{amount}/section/{sectionId}" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Course>> getBestRatedBySection(@PathVariable Integer amount, @PathVariable Long sectionId) {
        if(amount<=0 || sectionId <=0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Course> courses = courseService.fetchBestRatedBySection(amount, sectionId);
        if(courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);

    }

    @GetMapping(value = "/bestRated/{amount}/subSection/{subSectionId}" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Course>> getBestRatedBySubSection(@PathVariable Integer amount, @PathVariable Long subSectionId) {
        if(amount<=0 || subSectionId <=0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Course> courses = courseService.fetchBestRatedBySubSection(amount, subSectionId);
        if(courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);

    }





    @GetMapping(value = "/featured/{amount}" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Course>> getFeaturedCourses(@PathVariable Integer amount) {
        if(amount<=0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Course> courses = courseService.fetchFeaturedCourses(amount);
        if(courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @GetMapping(value = "/featured/{amount}/category/{categoryId}" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Course>> getFeaturedByCategory(@PathVariable Integer amount, @PathVariable Long categoryId) {
        if(amount<=0 || categoryId <=0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Course> courses = courseService.fetchFeaturedByCategory(amount, categoryId);
        if(courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);

    }

    @GetMapping(value = "/featured/{amount}/subCategory/{subCategoryId}" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Course>> getFeaturedBySubCategory(@PathVariable Integer amount, @PathVariable Long subCategoryId) {
        if(amount<=0 || subCategoryId <=0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Course> courses = courseService.fetchFeaturedBySubCategory(amount, subCategoryId);
        if(courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);

    }

    @GetMapping(value = "/featured/{amount}/section/{sectionId}" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Course>> getFeaturedBySection(@PathVariable Integer amount, @PathVariable Long sectionId) {
        if(amount<=0 || sectionId <=0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Course> courses = courseService.fetchFeaturedBySection(amount, sectionId);
        if(courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);

    }

    @GetMapping(value = "/featured/{amount}/subSection/{subSectionId}" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Course>> getFeaturedBySubSection(@PathVariable Integer amount, @PathVariable Long subSectionId) {
        if(amount<=0 || subSectionId <=0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Course> courses = courseService.fetchFeaturedBySubSection(amount, subSectionId);
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
    @PreAuthorize("hasAuthority('ROLE_ADMIN') " +
            "or hasRole('ROLE_SUPER_ADMIN') " +
            "or hasRole('ROLE_SCHOOL') " +
            "or hasRole('ROLE_PROFESSOR') " +
            "or hasRole('ROLE_FREELANCER') " +
            "or hasRole('ROLE_ORGANIZATION') " +
            "or hasRole('ROLE_INSTRUCTOR')")
    public  ResponseEntity<MessageResponse> saveCourse(@Valid @RequestBody CourseViewModel vm, Principal principal, HttpServletRequest request){
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
