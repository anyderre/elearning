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
@RequestMapping("/api/admin/dashboard")
@Log4j2
public class AdminDashboardController {

    @Autowired
    private UserServices userService;

    @Autowired
    private CourseService courseService;

    @PostMapping(value = "/course/approve")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiOperation(value = "Approve course, Role:ROLE_ADMIN")
    public ResponseEntity<MessageResponse> approveCourse(@Valid @RequestBody CourseApproveRequest approveRq, Principal principal) throws CourseNotFoundExcepion, UserNotFoundExcepion, SchoolNotFoundExcepion {

        if (approveRq.getCourseId() == null || approveRq.getCourseId() <= 0 || principal == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        boolean isApproved = courseService.approveCourse(approveRq.getCourseId(), principal.getName());

        if(isApproved) {
            return new ResponseEntity<>(MessageResponse.of("Course has been Approved"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(MessageResponse.of("Not Approved. Probably Admin's and Course's schools do not match"), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/course/decline")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiOperation(value = "Decline course, Role:ROLE_ADMIN")
    public ResponseEntity<MessageResponse> declineCourse(@Valid @RequestBody CourseDeclineRequest declineRq, Principal principal) throws CourseNotFoundExcepion, UserNotFoundExcepion, SchoolNotFoundExcepion {

        if (declineRq.getCourseId() == null || declineRq.getCourseId() <= 0 || principal == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        boolean isApproved = courseService.declineCourse(declineRq.getCourseId(), principal.getName(), declineRq.getDeclineMessage());

        if(isApproved) {
            return new ResponseEntity<>(MessageResponse.of("Course has been Declined"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(MessageResponse.of("Course Not Declined. Probably Admin's and Course's schools do not match"), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/school/students/count/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiOperation(value = "Count students in School. Statuses: all, Role:ROLE_ADMIN")
    public ResponseEntity<Long> getStudentsAmount(Principal principal) throws SchoolNotFoundExcepion, UserNotFoundExcepion {

        if (principal == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Long studentsCount = userService.countUsersInSchoolByRole(principal.getName(), Roles.ROLE_STUDENT);
        if (studentsCount == null || studentsCount <= 0)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(studentsCount, HttpStatus.OK);
    }

    @GetMapping(value = "/school/professors/count/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiOperation(value = "Count professors in School. Statuses: all, Role:ROLE_ADMIN")
    public ResponseEntity<Long> getProfessorAmount(Principal principal) throws SchoolNotFoundExcepion, UserNotFoundExcepion {

        if (principal == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Long professorsCount = userService.countUsersInSchoolByRole(principal.getName(), Roles.ROLE_PROFESSOR);
        if (professorsCount == null || professorsCount <= 0)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(professorsCount, HttpStatus.OK);
    }

    @GetMapping(value = "/school/courses/count/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiOperation(value = "Count courses in School. Statuses: all, Role:ROLE_ADMIN")
    public ResponseEntity<Long> getCoursesAmount(Principal principal) throws SchoolNotFoundExcepion, UserNotFoundExcepion {

        if (principal == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Long coursesCount = courseService.countAllCoursesInSchool(principal.getName());
        if (coursesCount == null || coursesCount <= 0)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(coursesCount, HttpStatus.OK);
    }

    @GetMapping(value = "/school/courses/count/approved")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiOperation(value = "Count courses in School. Statuses: APPROVED, Role:ROLE_ADMIN")
    public ResponseEntity<Long> getApprovedCoursesAmount(Principal principal) throws SchoolNotFoundExcepion, UserNotFoundExcepion {

        if (principal == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Long coursesCount = courseService.countApprovedCoursesInSchool(principal.getName());
        if (coursesCount == null || coursesCount <= 0)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(coursesCount, HttpStatus.OK);
    }

    @GetMapping(value = "/school/courses/count/declined")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiOperation(value = "Count courses in School. Statuses: DECLINED, Role:ROLE_ADMIN")
    public ResponseEntity<Long> getDeclinedCoursesAmount(Principal principal) throws SchoolNotFoundExcepion, UserNotFoundExcepion {

        if (principal == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Long coursesCount = courseService.countDeclinedCoursesInSchool(principal.getName());
        if (coursesCount == null || coursesCount <= 0)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(coursesCount, HttpStatus.OK);
    }

    @GetMapping(value = "/school/courses/count/pending")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiOperation(value = "Count courses in School. Statuses: PENDING, Role:ROLE_ADMIN")
    public ResponseEntity<Long> getPendingCoursesAmount(Principal principal) throws SchoolNotFoundExcepion, UserNotFoundExcepion {

        if (principal == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Long coursesCount = courseService.countPendingCoursesInSchool(principal.getName());
        if (coursesCount == null || coursesCount <= 0)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(coursesCount, HttpStatus.OK);
    }

    @GetMapping(value = "/school/courses/lastCreated/{amount}/{status}" , consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiOperation(value = "Get Last Created Courses in School, Status: all, pending, approved, declined, Role:ROLE_ADMIN")
    public ResponseEntity<List<Course>> getLastAddedPrivateCourses(@PathVariable Integer amount,
                                                                   @PathVariable String status,
                                                                   Principal principal) {

        log.debug("Principal username:"+principal.getName());
        Set<String> statuses = new HashSet<>();
        statuses.add("all");
        statuses.add("approved");
        statuses.add("declined");
        statuses.add("pending");

        if(amount<=0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        if(!statuses.contains(status.toLowerCase())) {
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<Course> courses = courseService.getLastAddedPrivateCoursesByStatus(amount, principal.getName(), status);
        if(courses == null || courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);

    }
}
