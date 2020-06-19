package com.sorbSoft.CabAcademie.Controllers;


import com.sorbSoft.CabAcademie.Entities.Course;
import com.sorbSoft.CabAcademie.Entities.Enums.CourseStatus;
import com.sorbSoft.CabAcademie.Entities.Error.MessageResponse;
import com.sorbSoft.CabAcademie.Services.CourseService;
import com.sorbSoft.CabAcademie.Services.Dtos.Validation.Result;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.CourseViewModel;
import com.sorbSoft.CabAcademie.config.JwtTokenUtil;
import com.sorbSoft.CabAcademie.exception.CourseAccessDeniedException;
import com.sorbSoft.CabAcademie.exception.CourseNotFoundExcepion;
import com.sorbSoft.CabAcademie.exception.EmptyValueException;
import com.sorbSoft.CabAcademie.exception.UserNotFoundExcepion;
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

    @GetMapping(value = "/allByUserId/{userId}" , consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get All Courses by user id. Role: ROLE_ADMIN, ROLE_SUPER_ADMIN, ROLE_SCHOOL, ROLE_PROFESSOR, ROLE_FREELANCER, ROLE_ORGANIZATION, ROLE_INSTRUCTOR")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') " +
            "or hasRole('ROLE_SUPER_ADMIN') " +
            "or hasRole('ROLE_SCHOOL') " +
            "or hasRole('ROLE_PROFESSOR') " +
            "or hasRole('ROLE_FREELANCER') " +
            "or hasRole('ROLE_ORGANIZATION') " +
            "or hasRole('ROLE_INSTRUCTOR')")
    public ResponseEntity<List<CourseViewModel>> getAllCoursesByUserId(@PathVariable Long userId) throws UserNotFoundExcepion {

        List<CourseViewModel> courses = courseService.fetchAllCoursesByUser(userId);
        if(courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @GetMapping(value = "/allByUserId/{userId}/amount/{amount}" , consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get All Courses by user id and amount. Role: ROLE_ADMIN, ROLE_SUPER_ADMIN, ROLE_SCHOOL, ROLE_PROFESSOR, ROLE_FREELANCER, ROLE_ORGANIZATION, ROLE_INSTRUCTOR")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') " +
            "or hasRole('ROLE_SUPER_ADMIN') " +
            "or hasRole('ROLE_SCHOOL') " +
            "or hasRole('ROLE_PROFESSOR') " +
            "or hasRole('ROLE_FREELANCER') " +
            "or hasRole('ROLE_ORGANIZATION') " +
            "or hasRole('ROLE_INSTRUCTOR')")
    public ResponseEntity<List<CourseViewModel>> getAllCoursesByUserIdAndAmount(
            @PathVariable Long userId,
            @PathVariable Integer amount) throws UserNotFoundExcepion {

        List<CourseViewModel> courses = courseService.fetchAllCoursesByUserAndAmount(userId, amount);
        if(courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @GetMapping(value = "/countByUserId/{userId}" , consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get User Courses Count by user id. Role: ROLE_ADMIN, ROLE_SUPER_ADMIN, ROLE_SCHOOL, ROLE_PROFESSOR, ROLE_FREELANCER, ROLE_ORGANIZATION, ROLE_INSTRUCTOR")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') " +
            "or hasRole('ROLE_SUPER_ADMIN') " +
            "or hasRole('ROLE_SCHOOL') " +
            "or hasRole('ROLE_PROFESSOR') " +
            "or hasRole('ROLE_FREELANCER') " +
            "or hasRole('ROLE_ORGANIZATION') " +
            "or hasRole('ROLE_INSTRUCTOR')")
    public ResponseEntity<Long> getUserCoursesCount(
            @PathVariable Long userId) throws UserNotFoundExcepion {

        long userCoursesCount = courseService.countUserCourses(userId);
        return new ResponseEntity<>(userCoursesCount, HttpStatus.OK);
    }

    @GetMapping(value = "/countByUserId/{userId}/approved" , consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get User Courses Count by user id and status. Role: ROLE_ADMIN, ROLE_SUPER_ADMIN, ROLE_SCHOOL, ROLE_PROFESSOR, ROLE_FREELANCER, ROLE_ORGANIZATION, ROLE_INSTRUCTOR")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') " +
            "or hasRole('ROLE_SUPER_ADMIN') " +
            "or hasRole('ROLE_SCHOOL') " +
            "or hasRole('ROLE_PROFESSOR') " +
            "or hasRole('ROLE_FREELANCER') " +
            "or hasRole('ROLE_ORGANIZATION') " +
            "or hasRole('ROLE_INSTRUCTOR')")
    public ResponseEntity<Long> getApprovedUserCoursesCount(
            @PathVariable Long userId) throws UserNotFoundExcepion, EmptyValueException {

        long userCoursesCount = courseService.countUserCoursesWithStatus(userId, CourseStatus.APPROVED);
        return new ResponseEntity<>(userCoursesCount, HttpStatus.OK);
    }

    @GetMapping(value = "/countByUserId/{userId}/pending" , consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get User Courses Count by user id and status. Role: ROLE_ADMIN, ROLE_SUPER_ADMIN, ROLE_SCHOOL, ROLE_PROFESSOR, ROLE_FREELANCER, ROLE_ORGANIZATION, ROLE_INSTRUCTOR")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') " +
            "or hasRole('ROLE_SUPER_ADMIN') " +
            "or hasRole('ROLE_SCHOOL') " +
            "or hasRole('ROLE_PROFESSOR') " +
            "or hasRole('ROLE_FREELANCER') " +
            "or hasRole('ROLE_ORGANIZATION') " +
            "or hasRole('ROLE_INSTRUCTOR')")
    public ResponseEntity<Long> getPendingUserCoursesCount(
            @PathVariable Long userId) throws UserNotFoundExcepion, EmptyValueException {

        long userCoursesCount = courseService.countUserCoursesWithStatus(userId, CourseStatus.PENDING);
        return new ResponseEntity<>(userCoursesCount, HttpStatus.OK);
    }

    @GetMapping(value = "/countByUserId/{userId}/declined" , consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get User Courses Count by user id and status. Role: ROLE_ADMIN, ROLE_SUPER_ADMIN, ROLE_SCHOOL, ROLE_PROFESSOR, ROLE_FREELANCER, ROLE_ORGANIZATION, ROLE_INSTRUCTOR")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') " +
            "or hasRole('ROLE_SUPER_ADMIN') " +
            "or hasRole('ROLE_SCHOOL') " +
            "or hasRole('ROLE_PROFESSOR') " +
            "or hasRole('ROLE_FREELANCER') " +
            "or hasRole('ROLE_ORGANIZATION') " +
            "or hasRole('ROLE_INSTRUCTOR')")
    public ResponseEntity<Long> getDeclinedUserCoursesCount(
            @PathVariable Long userId) throws UserNotFoundExcepion, EmptyValueException {

        long userCoursesCount = courseService.countUserCoursesWithStatus(userId, CourseStatus.DECLINED);
        return new ResponseEntity<>(userCoursesCount, HttpStatus.OK);
    }

    @GetMapping(value = "/countByUserId/{userId}/draft" , consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get User Courses Count by user id and status. Role: ROLE_ADMIN, ROLE_SUPER_ADMIN, ROLE_SCHOOL, ROLE_PROFESSOR, ROLE_FREELANCER, ROLE_ORGANIZATION, ROLE_INSTRUCTOR")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') " +
            "or hasRole('ROLE_SUPER_ADMIN') " +
            "or hasRole('ROLE_SCHOOL') " +
            "or hasRole('ROLE_PROFESSOR') " +
            "or hasRole('ROLE_FREELANCER') " +
            "or hasRole('ROLE_ORGANIZATION') " +
            "or hasRole('ROLE_INSTRUCTOR')")
    public ResponseEntity<Long> getDraftUserCoursesCount(
            @PathVariable Long userId) throws UserNotFoundExcepion, EmptyValueException {

        long userCoursesCount = courseService.countUserCoursesWithStatus(userId, CourseStatus.DRAFT);
        return new ResponseEntity<>(userCoursesCount, HttpStatus.OK);
    }

    @GetMapping(value = "/public/subSection/{subSectionId}" , consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get Public Courses by Sub Section")
    public ResponseEntity<List<Course>> getAllPublicCoursesBySubSection(@PathVariable Long subSectionId){

        List<Course> courses = courseService.fetchPublicCourseBySubSection(subSectionId);
        if(courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @GetMapping(value = "/subCategory/{id}" , consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get Public Courses by Sub Category")
    public ResponseEntity<List<Course>> getAllPublicCoursesBySubCategory(@PathVariable Long id){
        List<Course> courses = courseService.fetchPublicCourseBySubCategory(id);
        if(courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @GetMapping(value = "/lastCreated/{amount}" , consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get Last Created Public Courses by Amount")
    public ResponseEntity<List<Course>> getLastAddedPublicCourses(@PathVariable Integer amount) {
        if(amount<=0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Course> courses = courseService.fetchLastAddedPublicCourses(amount);
        if(courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);

    }

    @GetMapping(value = "/lastCreated/{amount}/category/{categoryId}" , consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get Last Created Public Courses by Amount and Category ID")
    public ResponseEntity<List<Course>> getLastAddedByCategoryPublicCourses(@PathVariable Integer amount, @PathVariable Long categoryId) {
        if(amount<=0 || categoryId <=0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Course> courses = courseService.fetchLastAddedByCategoryPublicCourses(amount, categoryId);
        if(courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);

    }

    @GetMapping(value = "/lastCreated/{amount}/subCategory/{subCategoryId}" , consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get Last Created Public Courses by Amount and Sub Category ID")
    public ResponseEntity<List<Course>> getLastAddedBySubCategoryPublicCourses(@PathVariable Integer amount, @PathVariable Long subCategoryId) {
        if(amount<=0 || subCategoryId <=0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Course> courses = courseService.fetchLastAddedBySubCategoryPublicCourses(amount, subCategoryId);
        if(courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);

    }

    @GetMapping(value = "/lastCreated/{amount}/section/{sectionId}" , consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get Last Created Public Courses by Amount and Section ID")
    public ResponseEntity<List<Course>> getLastAddedBySectionPublicCourses(@PathVariable Integer amount, @PathVariable Long sectionId) {
        if(amount<=0 || sectionId <=0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Course> courses = courseService.fetchLastAddedBySectionPublicCourses(amount, sectionId);
        if(courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);

    }

    @GetMapping(value = "/lastCreated/{amount}/subSection/{subSectionId}" , consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get Last Created Public Courses by Amount and Sub Section ID")
    public ResponseEntity<List<Course>> getLastAddedBySubSectionPublicCourses(@PathVariable Integer amount, @PathVariable Long subSectionId) {
        if(amount<=0 || subSectionId <=0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Course> courses = courseService.fetchLastAddedBySubSectionPublicCourses(amount, subSectionId);
        if(courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);

    }


    @GetMapping(value = "/bestRated/{amount}" , consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get Best Rated Public Courses by Amount")
    public ResponseEntity<List<Course>> getBestRatedCoursesPublicCourses(@PathVariable Integer amount) {
        if(amount<=0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Course> courses = courseService.fetchBestRatedPublicCourses(amount);
        if(courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @GetMapping(value = "/bestRated/{amount}/category/{categoryId}" , consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get Best Rated Public Courses by Amount and Category ID")
    public ResponseEntity<List<Course>> getBestRatedByCategoryPublicCourses(@PathVariable Integer amount, @PathVariable Long categoryId) {
        if(amount<=0 || categoryId <=0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Course> courses = courseService.fetchBestRatedByCategoryPublicCourses(amount, categoryId);
        if(courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);

    }

    @GetMapping(value = "/bestRated/{amount}/subCategory/{subCategoryId}" , consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get Best Rated Public Courses by Amount and Sub Category ID")
    public ResponseEntity<List<Course>> getBestRatedBySubCategoryPublicCourses(@PathVariable Integer amount, @PathVariable Long subCategoryId) {
        if(amount<=0 || subCategoryId <=0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Course> courses = courseService.fetchBestRatedBySubCategoryPublicCourses(amount, subCategoryId);
        if(courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);

    }

    @GetMapping(value = "/bestRated/{amount}/section/{sectionId}" , consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get Best Rated Public Courses by Amount and Section ID")
    public ResponseEntity<List<Course>> getBestRatedBySectionPublicCourses(@PathVariable Integer amount, @PathVariable Long sectionId) {
        if(amount<=0 || sectionId <=0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Course> courses = courseService.fetchBestRatedBySectionPublicCourses(amount, sectionId);
        if(courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);

    }

    @GetMapping(value = "/bestRated/{amount}/subSection/{subSectionId}" , consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get Best Rated Public Courses by Amount and Sub Section ID")
    public ResponseEntity<List<Course>> getBestRatedBySubSectionPublicCourses(@PathVariable Integer amount, @PathVariable Long subSectionId) {
        if(amount<=0 || subSectionId <=0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Course> courses = courseService.fetchBestRatedBySubSectionPublicCourses(amount, subSectionId);
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

    @PostMapping(value = "/saveByAdmin", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Save Course at status Approved")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') " +
            "or hasRole('ROLE_SUPER_ADMIN') ")
    public  ResponseEntity<MessageResponse> saveApprovedCourseByAdmin(@Valid @RequestBody CourseViewModel vm, Principal principal, HttpServletRequest request){
        vm.setStatus(CourseStatus.APPROVED);
        Result result = courseService.saveCourse(vm);
        if(!result.isValid())
            return new ResponseEntity<>(MessageResponse.of(result.lista.get(0).getMessage()), HttpStatus.CONFLICT);
        return  new ResponseEntity<>(MessageResponse.of("Course successfully saved"), HttpStatus.OK);
    }

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Save Course at status Pending (available for Admin approve")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') " +
            "or hasRole('ROLE_SUPER_ADMIN') " +
            "or hasRole('ROLE_SCHOOL') " +
            "or hasRole('ROLE_PROFESSOR') " +
            "or hasRole('ROLE_FREELANCER') " +
            "or hasRole('ROLE_ORGANIZATION') " +
            "or hasRole('ROLE_INSTRUCTOR')")
    public  ResponseEntity<MessageResponse> saveCourse(@Valid @RequestBody CourseViewModel vm, Principal principal, HttpServletRequest request){
        vm.setStatus(CourseStatus.PENDING);
        Result result = courseService.saveCourse(vm);
        if(!result.isValid())
            return new ResponseEntity<>(MessageResponse.of(result.lista.get(0).getMessage()), HttpStatus.CONFLICT);
        return  new ResponseEntity<>(MessageResponse.of("Course successfully saved"), HttpStatus.OK);
    }

    @PostMapping(value = "/saveDraft", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Save Course at status Draft. Not available for admin approve.")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') " +
            "or hasRole('ROLE_SUPER_ADMIN') " +
            "or hasRole('ROLE_SCHOOL') " +
            "or hasRole('ROLE_PROFESSOR') " +
            "or hasRole('ROLE_FREELANCER') " +
            "or hasRole('ROLE_ORGANIZATION') " +
            "or hasRole('ROLE_INSTRUCTOR')")
    public  ResponseEntity<MessageResponse> saveDraftCourse(@Valid @RequestBody CourseViewModel vm, Principal principal, HttpServletRequest request){
        vm.setStatus(CourseStatus.DRAFT);
        Result result = courseService.saveCourse(vm);
        if(!result.isValid())
            return new ResponseEntity<>(MessageResponse.of(result.lista.get(0).getMessage()), HttpStatus.CONFLICT);
        return  new ResponseEntity<>(MessageResponse.of("Course successfully saved"), HttpStatus.OK);
    }

    @PutMapping(value = "/update")
    @ApiOperation(value = "Update Course. This api sets course status Pending (available for Admin approve)")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') " +
            "or hasRole('ROLE_SUPER_ADMIN') " +
            "or hasRole('ROLE_SCHOOL') " +
            "or hasRole('ROLE_PROFESSOR') " +
            "or hasRole('ROLE_FREELANCER') " +
            "or hasRole('ROLE_ORGANIZATION') " +
            "or hasRole('ROLE_INSTRUCTOR')")
    public ResponseEntity<Course> updateCourse(@RequestBody CourseViewModel courseVm){

        //TODO: Validators should be implemenmted as seperate layer
        if(courseVm == null
                || courseVm.getId()==null)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Long id = courseVm.getId();
        if(!courseService.exists(id))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);


        courseVm.setStatus(CourseStatus.PENDING);
        Result result = courseService.updateCourse(courseVm);

        if (result == null)
            return  new ResponseEntity<>(HttpStatus.NOT_MODIFIED);

        return new ResponseEntity("Course updated", HttpStatus.OK);
    }

    @PutMapping(value = "/makePendingByTeacher/{courseId}")
    @ApiOperation(value = "Publish course. This api sets course status Pending (available for Admin approve)")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') " +
            "or hasRole('ROLE_SUPER_ADMIN') " +
            "or hasRole('ROLE_SCHOOL') " +
            "or hasRole('ROLE_PROFESSOR') " +
            "or hasRole('ROLE_FREELANCER') " +
            "or hasRole('ROLE_ORGANIZATION') " +
            "or hasRole('ROLE_INSTRUCTOR')")
    public ResponseEntity<MessageResponse> publishCourse(
            @PathVariable Long courseId,
            Principal principal)
            throws CourseNotFoundExcepion, CourseAccessDeniedException, UserNotFoundExcepion {

        if(courseId <= 0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        boolean isStatusChanged = courseService.makeCourseStatusPending(courseId, principal.getName());

        if(isStatusChanged) {
            return new ResponseEntity<>(MessageResponse.of("Course status changed"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(MessageResponse.of("Course status Not changed"), HttpStatus.NOT_MODIFIED);
        }
    }

    @PutMapping(value = "/makeDraftByTeacher/{courseId}")
    @ApiOperation(value = "Make course draft course. This api sets course status Draft (Not available for Admin approve)")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') " +
            "or hasRole('ROLE_SUPER_ADMIN') " +
            "or hasRole('ROLE_SCHOOL') " +
            "or hasRole('ROLE_PROFESSOR') " +
            "or hasRole('ROLE_FREELANCER') " +
            "or hasRole('ROLE_ORGANIZATION') " +
            "or hasRole('ROLE_INSTRUCTOR')")
    public ResponseEntity<MessageResponse> makeCourseDraft(
            @PathVariable Long courseId,
            Principal principal)
            throws CourseNotFoundExcepion, CourseAccessDeniedException, UserNotFoundExcepion {

        if(courseId <= 0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        boolean isStatusChanged = courseService.makeCourseStatusDraft(courseId, principal.getName());

        if(isStatusChanged) {
            return new ResponseEntity<>(MessageResponse.of("Course status changed"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(MessageResponse.of("Course status Not changed"), HttpStatus.NOT_MODIFIED);
        }
    }

    @PutMapping(value = "/updateDraft")
    @ApiOperation(value = "Update Course. This api sets course status Draft (Not available for Admin approve)")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') " +
            "or hasRole('ROLE_SUPER_ADMIN') " +
            "or hasRole('ROLE_SCHOOL') " +
            "or hasRole('ROLE_PROFESSOR') " +
            "or hasRole('ROLE_FREELANCER') " +
            "or hasRole('ROLE_ORGANIZATION') " +
            "or hasRole('ROLE_INSTRUCTOR')")
    public ResponseEntity<Course> updateDraftCourse(@RequestBody CourseViewModel courseVm){

        //TODO: Validators should be implemenmted as seperate layer
        if(courseVm == null
                || courseVm.getId()==null)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Long id = courseVm.getId();
        if(!courseService.exists(id))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);


        courseVm.setStatus(CourseStatus.DRAFT);
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
