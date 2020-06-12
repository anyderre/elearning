package com.sorbSoft.CabAcademie.Controllers;


import com.sorbSoft.CabAcademie.Entities.Course;
import com.sorbSoft.CabAcademie.Entities.Error.MessageResponse;
import com.sorbSoft.CabAcademie.Services.CourseService;
import com.sorbSoft.CabAcademie.Services.Dtos.Validation.Result;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.CourseViewModel;
import com.sorbSoft.CabAcademie.config.JwtTokenUtil;
import com.sorbSoft.CabAcademie.exception.*;
import io.swagger.annotations.ApiOperation;
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


@RestController
@RequestMapping(value = "/api/school/course")
@Log4j2
public class CoursePrivateController {
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

    @GetMapping(value = "/private/subSection/{subSectionId}" , consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get Private Courses by Sub Section")
    public ResponseEntity<List<Course>> getAllPrivateCoursesBySubSection(@PathVariable Long subSectionId, Principal principal){

        log.debug("Principal username:"+principal.getName());
        List<Course> courses = courseService.fetchPrivateCourseBySubSectionAlternative(subSectionId, principal.getName());
        if(courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @GetMapping(value = "/private/subCategory/{subCategoryId}" , consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get Private Courses by Sub Category")
    public ResponseEntity<List<Course>> getAllPublicCoursesBySubCategory(@PathVariable Long subCategoryId, Principal principal){
        List<Course> courses = courseService.fetchPrivateCourseBySubCategory(subCategoryId, principal.getName());
        if(courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }


    @GetMapping(value = "/private/lastCreated/{amount}" , consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get Last Created Private Courses by Amount")
    public ResponseEntity<List<Course>> getLastAddedPrivateCourses(@PathVariable Integer amount, Principal principal) {

        log.debug("Principal username:"+principal.getName());

        if(amount<=0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Course> courses = courseService.fetchLastAddedPrivateCourses(amount, principal.getName());
        if(courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);

    }

    @GetMapping(value = "/private/lastCreated/{amount}/category/{categoryId}" , consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get Last Created Private Courses by Amount and Category ID")
    public ResponseEntity<List<Course>> getLastAddedByCategoryPublicCourses(
            @PathVariable Integer amount,
            @PathVariable Long categoryId,
            Principal principal)
            throws CategoryNotFoundException, UserNotFoundExcepion, SchoolNotFoundExcepion {

        if(amount<=0 || categoryId <=0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Course> courses = courseService.fetchLastAddedPrivateCoursesByCategory(amount, categoryId, principal.getName());
        if(courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);

    }

    @GetMapping(value = "/private/lastCreated/{amount}/subCategory/{subCategoryId}" , consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get Last Created Private Courses by Amount and Sub Category ID")
    public ResponseEntity<List<Course>> getLastAddedBySubCategoryPublicCourses(
            @PathVariable Integer amount,
            @PathVariable Long subCategoryId,
            Principal principal)
            throws SubCategoryNotFoundException, UserNotFoundExcepion, SchoolNotFoundExcepion {

        if(amount<=0 || subCategoryId <=0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Course> courses = courseService.fetchLastAddedPrivateCoursesBySubCategory(amount, subCategoryId, principal.getName());
        if(courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);

    }

    @GetMapping(value = "/private/lastCreated/{amount}/section/{sectionId}" , consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get Last Created Private Courses by Amount and Section ID")
    public ResponseEntity<List<Course>> getLastAddedBySectionPublicCourses(
            @PathVariable Integer amount,
            @PathVariable Long sectionId,
            Principal principal) throws SchoolNotFoundExcepion, UserNotFoundExcepion, SectionNotFoundException {

        if(amount<=0 || sectionId <=0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Course> courses = courseService.fetchLastAddedPrivateCoursesBySection(amount, sectionId, principal.getName());
        if(courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);

    }

    @GetMapping(value = "/private/lastCreated/{amount}/subSection/{subSectionId}" , consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get Last Created Private Courses by Amount and Sub Section ID")
    public ResponseEntity<List<Course>> getLastAddedBySubSectionPrivateCourses(
            @PathVariable Integer amount,
            @PathVariable Long subSectionId,
            Principal principal) throws SchoolNotFoundExcepion, UserNotFoundExcepion, SubSectionNotFoundException {

        if(amount<=0 || subSectionId <=0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Course> courses = courseService.fetchLastAddedPrivateCoursesBySubSection(amount, subSectionId, principal.getName());
        if(courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);

    }



    @GetMapping(value = "/private/bestRated/{amount}" , consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get Best Rated Private Courses by Amount")
    public ResponseEntity<List<Course>> getBestRatedCoursesPrivateCourses(
            @PathVariable Integer amount,
            Principal principal)
            throws UserNotFoundExcepion, SchoolNotFoundExcepion {

        if(amount<=0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Course> courses = courseService.fetchBestRatedPrivateCourses(amount, principal.getName());
        if(courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @GetMapping(value = "/private/bestRated/{amount}/category/{categoryId}" , consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get Best Rated Private Courses by Amount and Category ID")
    public ResponseEntity<List<Course>> getBestRatedByCategoryPrivateCourses(
            @PathVariable Integer amount,
            @PathVariable Long categoryId,
            Principal principal)
            throws SchoolNotFoundExcepion, UserNotFoundExcepion, CategoryNotFoundException {

        if(amount<=0 || categoryId <=0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Course> courses = courseService.fetchBestRatedPrivateCoursesByCategory(amount, categoryId, principal.getName());
        if(courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);

    }

    @GetMapping(value = "/private/bestRated/{amount}/subCategory/{subCategoryId}" , consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get Best Rated Private Courses by Amount and Sub Category ID")
    public ResponseEntity<List<Course>> getBestRatedBySubCategoryPrivateCourses(
            @PathVariable Integer amount,
            @PathVariable Long subCategoryId,
            Principal principal)
            throws SubCategoryNotFoundException, UserNotFoundExcepion, SchoolNotFoundExcepion {

        if(amount<=0 || subCategoryId <=0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Course> courses = courseService.fetchBestRatedPrivateCoursesBySubCategory(amount, subCategoryId, principal.getName());
        if(courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);

    }

    @GetMapping(value = "/private/bestRated/{amount}/section/{sectionId}" , consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get Best Rated Private Courses by Amount and Section ID")
    public ResponseEntity<List<Course>> getBestRatedBySectionPublicCourses(
            @PathVariable Integer amount,
            @PathVariable Long sectionId,
            Principal principal) throws SchoolNotFoundExcepion, UserNotFoundExcepion, SectionNotFoundException {

        if(amount<=0 || sectionId <=0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Course> courses = courseService.fetchBestRatedPrivateCoursesBySection(amount, sectionId, principal.getName());
        if(courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);

    }

    @GetMapping(value = "/bestRated/{amount}/subSection/{subSectionId}" , consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get Best Rated Private Courses by Amount and Sub Section ID")
    public ResponseEntity<List<Course>> getBestRatedBySubSectionPublicCourses(
            @PathVariable Integer amount,
            @PathVariable Long subSectionId,
            Principal principal) throws SubSectionNotFoundException, UserNotFoundExcepion, SchoolNotFoundExcepion {

        if(amount<=0 || subSectionId <=0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Course> courses = courseService.fetchBestRatedPrivateCoursesBySubSection(amount, subSectionId, principal.getName());
        if(courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);

    }





    @GetMapping(value = "/featured/{amount}" , consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get Featured Public Courses by Amount")
    public ResponseEntity<List<Course>> getFeaturedCoursesPublicCourses(@PathVariable Integer amount) {
        if(amount<=0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Course> courses = courseService.fetchFeaturedPublicCourses(amount);
        if(courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @GetMapping(value = "/featured/{amount}/category/{categoryId}" , consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get Featured Public Courses by Amount and Category ID")
    public ResponseEntity<List<Course>> getFeaturedByCategoryPublicCourses(@PathVariable Integer amount, @PathVariable Long categoryId) {
        if(amount<=0 || categoryId <=0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Course> courses = courseService.fetchFeaturedByCategoryPublicCourses(amount, categoryId);
        if(courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);

    }

    @GetMapping(value = "/featured/{amount}/subCategory/{subCategoryId}" , consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get Featured Public Courses by Amount and Sub Category ID")
    public ResponseEntity<List<Course>> getFeaturedBySubCategoryPublicCourses(@PathVariable Integer amount, @PathVariable Long subCategoryId) {
        if(amount<=0 || subCategoryId <=0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Course> courses = courseService.fetchFeaturedBySubCategoryPublicCourses(amount, subCategoryId);
        if(courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);

    }

    @GetMapping(value = "/featured/{amount}/section/{sectionId}" , consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get Featured Public Courses by Amount and Section ID")
    public ResponseEntity<List<Course>> getFeaturedBySectionPublicCourses(@PathVariable Integer amount, @PathVariable Long sectionId) {
        if(amount<=0 || sectionId <=0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Course> courses = courseService.fetchFeaturedBySectionPublicCourses(amount, sectionId);
        if(courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);

    }

    @GetMapping(value = "/featured/{amount}/subSection/{subSectionId}" , consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get Featured Public Courses by Amount and Sub Section ID")
    public ResponseEntity<List<Course>> getFeaturedBySubSectionPublicCourses(@PathVariable Integer amount, @PathVariable Long subSectionId) {
        if(amount<=0 || subSectionId <=0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Course> courses = courseService.fetchFeaturedBySubSectionPublicCourses(amount, subSectionId);
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
