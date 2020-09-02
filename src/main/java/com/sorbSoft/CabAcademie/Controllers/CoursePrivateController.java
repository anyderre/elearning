package com.sorbSoft.CabAcademie.Controllers;


import com.sorbSoft.CabAcademie.Entities.Course;
import com.sorbSoft.CabAcademie.Services.CourseService;
import com.sorbSoft.CabAcademie.exception.*;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping(value = "/api/school/course")
@Log4j2
public class CoursePrivateController {

    @Autowired
    private CourseService courseService;

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





    @GetMapping(value = "/private/featured/{amount}" , consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get Featured Private Courses by Amount")
    public ResponseEntity<List<Course>> getFeaturedCoursesPrivateCourses(
            @PathVariable Integer amount,
            Principal principal)
            throws UserNotFoundExcepion, SchoolNotFoundExcepion {

        if(amount<=0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Course> courses = courseService.fetchFeaturedPrivateCourses(amount, principal.getName());
        if(courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @GetMapping(value = "/private/featured/{amount}/category/{categoryId}" , consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get Featured Private Courses by Amount and Category ID")
    public ResponseEntity<List<Course>> getFeaturedByCategoryPrivateCourses(
            @PathVariable Integer amount,
            @PathVariable Long categoryId,
            Principal principal) throws UserNotFoundExcepion, SchoolNotFoundExcepion, CategoryNotFoundException {

        if(amount<=0 || categoryId <=0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Course> courses = courseService.fetchFeaturedByCategoryPrivateCourses(amount, categoryId, principal.getName());
        if(courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);

    }

    @GetMapping(value = "/private/featured/{amount}/subCategory/{subCategoryId}" , consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get Featured Private Courses by Amount and Sub Category ID")
    public ResponseEntity<List<Course>> getFeaturedBySubCategoryPrivateCourses(
            @PathVariable Integer amount,
            @PathVariable Long subCategoryId,
            Principal principal) throws UserNotFoundExcepion, SchoolNotFoundExcepion, SubCategoryNotFoundException {

        if(amount<=0 || subCategoryId <=0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Course> courses = courseService.fetchFeaturedBySubCategoryPrivateCourses(amount, subCategoryId, principal.getName());
        if(courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);

    }

    @GetMapping(value = "/private/featured/{amount}/section/{sectionId}" , consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get Featured Private Courses by Amount and Section ID")
    public ResponseEntity<List<Course>> getFeaturedBySectionPublicCourses(
            @PathVariable Integer amount,
            @PathVariable Long sectionId,
            Principal principal) throws UserNotFoundExcepion, SchoolNotFoundExcepion, SectionNotFoundException {

        if(amount<=0 || sectionId <=0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Course> courses = courseService.fetchFeaturedBySectionPrivateCourses(amount, sectionId, principal.getName());
        if(courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);

    }

    @GetMapping(value = "/private/featured/{amount}/subSection/{subSectionId}" , consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get Featured Private Courses by Amount and Sub Section ID")
    public ResponseEntity<List<Course>> getFeaturedBySubSectionPrivateCourses(
            @PathVariable Integer amount,
            @PathVariable Long subSectionId,
            Principal principal) throws UserNotFoundExcepion, SchoolNotFoundExcepion, SubSectionNotFoundException {

        if(amount<=0 || subSectionId <=0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Course> courses = courseService.fetchFeaturedBySubSectionPrivateCourses(amount, subSectionId, principal.getName());
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
    @GetMapping(value="/private/filterCourses", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<Course>> getAllCoursesByFilters(@RequestParam(value = "count", defaultValue = "10") String count,
                                                               @RequestParam(value = "page", defaultValue = "1") String page,
                                                               @RequestParam(value = "sort", required = false) String sort,
                                                               @RequestParam(value = "filter", required = false) String filter,
                                                               Principal principal) throws EmptyValueException, UserNotFoundExcepion, SchoolNotFoundExcepion {
        Page<Course> courses;
        String sortName = null;
        String sortValue = null;

        if (StringUtils.isNotBlank(sort)) {
            String[] split = sort.split(" ");

            if (split.length != 2) {
                throw new IllegalArgumentException("sort is incorrect, should be splitted by space ");
            }


            sortName = split[0];
            sortValue = split[1];
        }

        if (StringUtils.isNotBlank(filter)) {
            String[] split = filter.split(" ");

            if (split.length != 2) {
                throw new IllegalArgumentException("filter is incorrect, should be splitted by space ");
            }

            String filedName = split[0];
            String filedValue = split[1];

            switch(filedName.trim().toLowerCase()) {

                case "title":
                    courses = courseService.fetchAllPrivateCoursesByPageAndTitle(filedValue, Integer.valueOf(page), Integer.valueOf(count), sortName, sortValue, principal.getName());
                    return new ResponseEntity<>(courses, HttpStatus.OK);
                case "userId":
                    courses = courseService.fetchAllPrivateCoursesByPageAndUserId(Long.valueOf(filedValue), Integer.valueOf(page), Integer.valueOf(count), sortName, sortValue, principal.getName());
                    return new ResponseEntity<>(courses, HttpStatus.OK);
            }

        }

        courses = courseService.fetchAllPrivateCourses(Integer.valueOf(page), Integer.valueOf(count), sortName, sortValue, principal.getName());

        return new ResponseEntity<>(courses, HttpStatus.OK);
    }
}
