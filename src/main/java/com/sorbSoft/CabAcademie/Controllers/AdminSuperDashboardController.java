package com.sorbSoft.CabAcademie.Controllers;

import com.sorbSoft.CabAcademie.Entities.Course;
import com.sorbSoft.CabAcademie.Entities.Enums.CourseStatus;
import com.sorbSoft.CabAcademie.Entities.Enums.Roles;
import com.sorbSoft.CabAcademie.Entities.Error.MessageResponse;
import com.sorbSoft.CabAcademie.Services.CourseService;
import com.sorbSoft.CabAcademie.Services.UserServices;
import com.sorbSoft.CabAcademie.exception.CourseNotFoundExcepion;
import com.sorbSoft.CabAcademie.exception.SchoolNotFoundExcepion;
import com.sorbSoft.CabAcademie.exception.UserNotFoundExcepion;
import com.sorbSoft.CabAcademie.payload.CourseApproveRequest;
import com.sorbSoft.CabAcademie.payload.CourseDeclineRequest;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("/api/super-admin/dashboard")
@Log4j2
public class AdminSuperDashboardController {

    @Autowired
    private UserServices userService;

    @Autowired
    private CourseService courseService;

    @PostMapping(value = "public/course/approve")
    @PreAuthorize("hasAuthority('ROLE_SUPER_ADMIN')")
    @ApiOperation(value = "Approve course, Role:ROLE_SUPER_ADMIN")
    public ResponseEntity<MessageResponse> approveCourse(@Valid @RequestBody CourseApproveRequest approveRq) throws CourseNotFoundExcepion, UserNotFoundExcepion, SchoolNotFoundExcepion {

        if (approveRq.getCourseId() == null || approveRq.getCourseId() <= 0)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        boolean isApproved = courseService.approveCourse(approveRq.getCourseId());

        if(isApproved) {
            return new ResponseEntity<>(MessageResponse.of("Course has been Approved"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(MessageResponse.of("Not Approved"), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "public/course/decline")
    @PreAuthorize("hasAuthority('ROLE_SUPER_ADMIN')")
    @ApiOperation(value = "Decline course, Role:ROLE_SUPER_ADMIN")
    public ResponseEntity<MessageResponse> declineCourse(@Valid @RequestBody CourseDeclineRequest declineRq) throws CourseNotFoundExcepion, UserNotFoundExcepion, SchoolNotFoundExcepion {

        if (declineRq.getCourseId() == null || declineRq.getCourseId() <= 0)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        boolean isApproved = courseService.declineCourse(declineRq.getCourseId(), declineRq.getDeclineMessage());

        if(isApproved) {
            return new ResponseEntity<>(MessageResponse.of("Course has been Declined"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(MessageResponse.of("Course Not Declined"), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/public/students/count/all")
    @PreAuthorize("hasAuthority('ROLE_SUPER_ADMIN')")
    @ApiOperation(value = "Count students (public). Statuses: all, Role:ROLE_SUPER_ADMIN")
    public ResponseEntity<Long> getStudentsAmount() {

        Long studentsCount = userService.countFreeUsersByRole(Roles.ROLE_FREE_STUDENT);
        if (studentsCount == null || studentsCount <= 0)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(studentsCount, HttpStatus.OK);
    }

    @GetMapping(value = "/public/freelancers/count/all")
    @PreAuthorize("hasAuthority('ROLE_SUPER_ADMIN')")
    @ApiOperation(value = "Count freelancers (public). Statuses: all, Role:ROLE_SUPER_ADMIN")
    public ResponseEntity<Long> getFreelancersAmount() {

        Long professorsCount = userService.countFreeUsersByRole(Roles.ROLE_FREELANCER);
        if (professorsCount == null || professorsCount <= 0)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(professorsCount, HttpStatus.OK);
    }

    @GetMapping(value = "/public/courses/count/all")
    @PreAuthorize("hasAuthority('ROLE_SUPER_ADMIN')")
    @ApiOperation(value = "Count courses (public). Statuses: all, Role:ROLE_SUPER_ADMIN")
    public ResponseEntity<Long> getCoursesAmount() {

        Long coursesCount = courseService.countAllPublicCourses();
        if (coursesCount == null || coursesCount <= 0)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(coursesCount, HttpStatus.OK);
    }

    @GetMapping(value = "/public/courses/count/approved")
    @PreAuthorize("hasAuthority('ROLE_SUPER_ADMIN')")
    @ApiOperation(value = "Count courses (public). Statuses: APPROVED, Role:ROLE_SUPER_ADMIN")
    public ResponseEntity<Long> getApprovedCoursesAmount() {

        Long coursesCount = courseService.countApprovedPublicCourses();
        if (coursesCount == null || coursesCount <= 0)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(coursesCount, HttpStatus.OK);
    }

    @GetMapping(value = "/public/courses/count/declined")
    @PreAuthorize("hasAuthority('ROLE_SUPER_ADMIN')")
    @ApiOperation(value = "Count courses (public). Statuses: DECLINED, Role:ROLE_SUPER_ADMIN")
    public ResponseEntity<Long> getDeclinedCoursesAmount() {

        Long coursesCount = courseService.countDeclinedPublicCourses();
        if (coursesCount == null || coursesCount <= 0)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(coursesCount, HttpStatus.OK);
    }

    @GetMapping(value = "/public/courses/count/pending")
    @PreAuthorize("hasAuthority('ROLE_SUPER_ADMIN')")
    @ApiOperation(value = "Count courses (public). Statuses: PENDING, Role:ROLE_SUPER_ADMIN")
    public ResponseEntity<Long> getPendingCoursesAmount() {

        Long coursesCount = courseService.countPendingPublicCourses();
        if (coursesCount == null || coursesCount <= 0)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(coursesCount, HttpStatus.OK);
    }

    @GetMapping(value = "/public/courses/lastCreated/{amount}/all" , consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_SUPER_ADMIN')")
    @ApiOperation(value = "Get Last Created Courses (public), Status: all, Role:ROLE_SUPER_ADMIN")
    public ResponseEntity<List<Course>> getLastAddedPublicCourses(@PathVariable Integer amount) {
        if(amount<=0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Course> courses = courseService.fetchLastAddedPublicCoursesByStatus(amount, null);
        if(courses == null || courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);

    }

    @GetMapping(value = "/public/courses/lastCreated/{amount}/approved" , consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_SUPER_ADMIN')")
    @ApiOperation(value = "Get Last Created Courses (public), Status: approved, Role:ROLE_SUPER_ADMIN")
    public ResponseEntity<List<Course>> getLastAddedPublicCoursesApproved(@PathVariable Integer amount) {
        if(amount<=0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Course> courses = courseService.fetchLastAddedPublicCoursesByStatus(amount, CourseStatus.APPROVED);
        if(courses == null || courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);

    }

    @GetMapping(value = "/public/courses/lastCreated/{amount}/declined" , consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_SUPER_ADMIN')")
    @ApiOperation(value = "Get Last Created Courses (public), Status: declined, Role:ROLE_SUPER_ADMIN")
    public ResponseEntity<List<Course>> getLastAddedPublicCoursesDeclined(@PathVariable Integer amount) {
        if(amount<=0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Course> courses = courseService.fetchLastAddedPublicCoursesByStatus(amount, CourseStatus.DECLINED);
        if(courses == null || courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);

    }

    @GetMapping(value = "/public/courses/lastCreated/{amount}/pending" , consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_SUPER_ADMIN')")
    @ApiOperation(value = "Get Last Created Courses (public), Status: declined, Role:ROLE_SUPER_ADMIN")
    public ResponseEntity<List<Course>> getLastAddedPublicCoursesPending(@PathVariable Integer amount) {
        if(amount<=0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Course> courses = courseService.fetchLastAddedPublicCoursesByStatus(amount, CourseStatus.PENDING);
        if(courses == null || courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);

    }
}
