package com.sorbSoft.CabAcademie.Controllers;

import com.sorbSoft.CabAcademie.Entities.Course;
import com.sorbSoft.CabAcademie.Entities.Enums.CourseStatus;
import com.sorbSoft.CabAcademie.Entities.Enums.Roles;
import com.sorbSoft.CabAcademie.Entities.Error.MessageResponse;
import com.sorbSoft.CabAcademie.Services.CourseService;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.UserViewModel;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.appointment.SlotsResponseModel;
import com.sorbSoft.CabAcademie.Services.TimeSlotService;
import com.sorbSoft.CabAcademie.Services.UserServices;
import com.sorbSoft.CabAcademie.exception.*;
import com.sorbSoft.CabAcademie.payload.CourseApproveRequest;
import com.sorbSoft.CabAcademie.payload.CourseDeclineRequest;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @Autowired
    private TimeSlotService timeSlotService;

    @PostMapping(value = "/course/approve")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiOperation(value = "Approve course, Role:ROLE_ADMIN")
    public ResponseEntity<MessageResponse> approveCourse(
            @Valid @RequestBody CourseApproveRequest approveRq,
            Principal principal) throws CourseNotFoundExcepion, UserNotFoundExcepion, SchoolNotFoundExcepion, MaxCoursesPerProfessorExceededException, CourseAccessDeniedException, PaymentException, SubscriptionPlanDateExpired {

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
    public ResponseEntity<MessageResponse> declineCourse(@Valid @RequestBody CourseDeclineRequest declineRq, Principal principal) throws CourseNotFoundExcepion, UserNotFoundExcepion, SchoolNotFoundExcepion, CourseAccessDeniedException {

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
    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_SUPER_ADMIN')")
    @ApiOperation(value = "Count students in School. Statuses: all, Role:ROLE_ADMIN and ROLE_SUPER_ADMIN")
    public ResponseEntity<Long> getStudentsAmount(Principal principal) throws SchoolNotFoundExcepion, UserNotFoundExcepion {

        if (principal == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Long studentsCount = userService.countAllUsersInSchoolByRole(principal.getName(), Roles.ROLE_STUDENT);

        return new ResponseEntity<>(studentsCount, HttpStatus.OK);
    }

    @GetMapping(value = "/school/students/count/pending")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_SUPER_ADMIN')")
    @ApiOperation(value = "Count students in School. Statuses: pending, Role:ROLE_ADMIN and ROLE_SUPER_ADMIN")
    public ResponseEntity<Long> getStudentsAmountPending(Principal principal) throws SchoolNotFoundExcepion, UserNotFoundExcepion {

        if (principal == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Long studentsCount = userService.countPendingUsersInSchoolByRole(principal.getName(), Roles.ROLE_STUDENT);

        return new ResponseEntity<>(studentsCount, HttpStatus.OK);
    }

    @GetMapping(value = "/school/professors/count/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_SUPER_ADMIN')")
    @ApiOperation(value = "Count professors in School. Statuses: all, Role:ROLE_ADMIN and ROLE_SUPER_ADMIN")
    public ResponseEntity<Long> getProfessorAmount(Principal principal) throws SchoolNotFoundExcepion, UserNotFoundExcepion {

        if (principal == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Long professorsCount = userService.countAllUsersInSchoolByRole(principal.getName(), Roles.ROLE_PROFESSOR);

        return new ResponseEntity<>(professorsCount, HttpStatus.OK);
    }

    @GetMapping(value = "/school/professors/count/pending")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_SUPER_ADMIN')")
    @ApiOperation(value = "Count pending professors in School. Statuses: pending, Role:ROLE_ADMIN and ROLE_SUPER_ADMIN")
    public ResponseEntity<Long> getPendingProfessorAmount(Principal principal) throws SchoolNotFoundExcepion, UserNotFoundExcepion {

        if (principal == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Long professorsCount = userService.countPendingUsersInSchoolByRole(principal.getName(), Roles.ROLE_PROFESSOR);

        return new ResponseEntity<>(professorsCount, HttpStatus.OK);
    }

    @GetMapping(value = "/school/courses/count/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_SUPER_ADMIN')")
    @ApiOperation(value = "Count courses in School. Statuses: all, Role:ROLE_ADMIN")
    public ResponseEntity<Long> getCoursesAmount(Principal principal) throws SchoolNotFoundExcepion, UserNotFoundExcepion {

        if (principal == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Long coursesCount = courseService.countAllCoursesInSchool(principal.getName());

        return new ResponseEntity<>(coursesCount, HttpStatus.OK);
    }

    @GetMapping(value = "/school/courses/count/approved")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_SUPER_ADMIN')")
    @ApiOperation(value = "Count courses in School. Statuses: APPROVED, Role:ROLE_ADMIN")
    public ResponseEntity<Long> getApprovedCoursesAmount(Principal principal) throws SchoolNotFoundExcepion, UserNotFoundExcepion {

        if (principal == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Long coursesCount = courseService.countApprovedCoursesInSchool(principal.getName());

        return new ResponseEntity<>(coursesCount, HttpStatus.OK);
    }

    @GetMapping(value = "/school/courses/count/declined")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_SUPER_ADMIN')")
    @ApiOperation(value = "Count courses in School. Statuses: DECLINED, Role:ROLE_ADMIN")
    public ResponseEntity<Long> getDeclinedCoursesAmount(Principal principal) throws SchoolNotFoundExcepion, UserNotFoundExcepion {

        if (principal == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Long coursesCount = courseService.countDeclinedCoursesInSchool(principal.getName());

        return new ResponseEntity<>(coursesCount, HttpStatus.OK);
    }

    @GetMapping(value = "/school/courses/count/pending")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_SUPER_ADMIN')")
    @ApiOperation(value = "Count courses in School. Statuses: PENDING, Role:ROLE_ADMIN")
    public ResponseEntity<Long> getPendingCoursesAmount(Principal principal) throws SchoolNotFoundExcepion, UserNotFoundExcepion {

        if (principal == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Long coursesCount = courseService.countPendingCoursesInSchool(principal.getName());

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

    @GetMapping(value = "/school/courses/lastCreated/{amount}/all" , consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiOperation(value = "Get Last Created Courses in School, Status: all, Role:ROLE_ADMIN")
    public ResponseEntity<List<Course>> getLastAddedPrivateCoursesAnyStatus(
            @PathVariable Integer amount,
            Principal principal) {

        log.debug("Principal username:"+principal.getName());


        if(amount<=0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Course> courses = courseService.getLastAddedPrivateCoursesAnyStatus(amount, principal.getName());
        if(courses == null || courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);

    }

    @GetMapping(value = "/school/courses/lastCreated/{amount}/pending" , consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiOperation(value = "Get Last Created Courses in School, Status: PENDING, Role:ROLE_ADMIN")
    public ResponseEntity<List<Course>> getLastAddedPrivateCoursesWithStatusPending(
            @PathVariable Integer amount,
            Principal principal) {

        log.debug("Principal username:"+principal.getName());


        if(amount<=0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Course> courses = courseService.getLastAddedPrivateCoursesPending(amount, principal.getName());
        if(courses == null || courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);

    }

    @GetMapping(value = "/school/courses/lastCreated/{amount}/approved" , consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiOperation(value = "Get Last Created Courses in School, Status: APPROVED, Role:ROLE_ADMIN")
    public ResponseEntity<List<Course>> getLastAddedPrivateCoursesWithStatusApproved(
            @PathVariable Integer amount,
            Principal principal) {

        log.debug("Principal username:"+principal.getName());


        if(amount<=0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Course> courses = courseService.getLastAddedPrivateCoursesApproved(amount, principal.getName());
        if(courses == null || courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);

    }

    @GetMapping(value = "/school/courses/lastCreated/{amount}/declined" , consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiOperation(value = "Get Last Created Courses in School, Status: DECLINED, Role:ROLE_ADMIN")
    public ResponseEntity<List<Course>> getLastAddedPrivateCoursesWithStatusDeclined(
            @PathVariable Integer amount,
            Principal principal) {

        log.debug("Principal username:"+principal.getName());


        if(amount<=0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Course> courses = courseService.getLastAddedPrivateCoursesDeclined(amount, principal.getName());
        if(courses == null || courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);

    }

    //START: Last Added By Category
    @GetMapping(value = "/school/courses/lastCreated/{amount}/category/{categoryId}/all" , consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiOperation(value = "Get Last Created Courses By Category in School, Status: all, Role:ROLE_ADMIN")
    public ResponseEntity<List<Course>> getLastAddedPrivateCoursesByCategoryAnyStatus(
            @PathVariable Integer amount,
            @PathVariable Long categoryId,
            Principal principal) throws CategoryNotFoundException, UserNotFoundExcepion, SchoolNotFoundExcepion {

        log.debug("Principal username:"+principal.getName());


        if(amount<=0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Course> courses = courseService.fetchLastAddedPrivateCoursesByCategoryAndStatus(amount, principal.getName(), categoryId, null);
        if(courses == null || courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);

    }

    @GetMapping(value = "/school/courses/lastCreated/{amount}/category/{categoryId}/pending" , consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiOperation(value = "Get Last Created Courses By Category in School, Status: PENDING, Role:ROLE_ADMIN")
    public ResponseEntity<List<Course>> getLastAddedPrivateCoursesByCategoryWithStatusPending(
            @PathVariable Integer amount,
            @PathVariable Long categoryId,
            Principal principal) throws CategoryNotFoundException, UserNotFoundExcepion, SchoolNotFoundExcepion {

        log.debug("Principal username:"+principal.getName());


        if(amount<=0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Course> courses = courseService.fetchLastAddedPrivateCoursesByCategoryAndStatus(amount, principal.getName(), categoryId, CourseStatus.PENDING);
        if(courses == null || courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);

    }

    @GetMapping(value = "/school/courses/lastCreated/{amount}/category/{categoryId}/approved" , consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiOperation(value = "Get Last Created Courses By Category in School, Status: APPROVED, Role:ROLE_ADMIN")
    public ResponseEntity<List<Course>> getLastAddedPrivateCoursesByCategoryWithStatusApproved(
            @PathVariable Integer amount,
            @PathVariable Long categoryId,
            Principal principal) throws CategoryNotFoundException, UserNotFoundExcepion, SchoolNotFoundExcepion {

        log.debug("Principal username:"+principal.getName());


        if(amount<=0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Course> courses = courseService.fetchLastAddedPrivateCoursesByCategoryAndStatus(amount, principal.getName(), categoryId, CourseStatus.APPROVED);
        if(courses == null || courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);

    }

    @GetMapping(value = "/school/courses/lastCreated/{amount}/category/{categoryId}/declined" , consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiOperation(value = "Get Last Created Courses by Category in School, Status: DECLINED, Role:ROLE_ADMIN")
    public ResponseEntity<List<Course>> getLastAddedPrivateCoursesByCategoryWithStatusDeclined(
            @PathVariable Integer amount,
            @PathVariable Long categoryId,
            Principal principal)
            throws CategoryNotFoundException, UserNotFoundExcepion, SchoolNotFoundExcepion {

        log.debug("Principal username:"+principal.getName());


        if(amount<=0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Course> courses = courseService.fetchLastAddedPrivateCoursesByCategoryAndStatus(amount, principal.getName(), categoryId, CourseStatus.DECLINED);
        if(courses == null || courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);

    }

    //END: Last Added By Category

    //START: Last Added By SubCategory
    @GetMapping(value = "/school/courses/lastCreated/{amount}/subCategory/{subCategoryId}/all" , consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiOperation(value = "Get Last Created Courses By SubCategory in School, Status: all, Role:ROLE_ADMIN")
    public ResponseEntity<List<Course>> getLastAddedPrivateCoursesBySubCategoryAnyStatus(
            @PathVariable Integer amount,
            @PathVariable Long subCategoryId,
            Principal principal) throws UserNotFoundExcepion, SchoolNotFoundExcepion, SubCategoryNotFoundException {

        log.debug("Principal username:"+principal.getName());


        if(amount<=0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Course> courses = courseService.fetchLastAddedPrivateCoursesBySubCategoryAndStaus(amount, principal.getName(), subCategoryId, null);
        if(courses == null || courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);

    }

    @GetMapping(value = "/school/courses/lastCreated/{amount}/subCategory/{subCategoryId}/pending" , consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiOperation(value = "Get Last Created Courses By SubCategory in School, Status: PENDING, Role:ROLE_ADMIN")
    public ResponseEntity<List<Course>> getLastAddedPrivateCoursesBySubCategoryWithStatusPending(
            @PathVariable Integer amount,
            @PathVariable Long subCategoryId,
            Principal principal) throws UserNotFoundExcepion, SchoolNotFoundExcepion, SubCategoryNotFoundException {

        log.debug("Principal username:"+principal.getName());


        if(amount<=0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Course> courses = courseService.fetchLastAddedPrivateCoursesBySubCategoryAndStaus(amount, principal.getName(), subCategoryId, CourseStatus.PENDING);
        if(courses == null || courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);

    }

    @GetMapping(value = "/school/courses/lastCreated/{amount}/subCategory/{subCategoryId}/approved" , consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiOperation(value = "Get Last Created Courses By SubCategory in School, Status: APPROVED, Role:ROLE_ADMIN")
    public ResponseEntity<List<Course>> getLastAddedPrivateCoursesBySubCategoryWithStatusApproved(
            @PathVariable Integer amount,
            @PathVariable Long subCategoryId,
            Principal principal) throws UserNotFoundExcepion, SchoolNotFoundExcepion, SubCategoryNotFoundException {

        log.debug("Principal username:"+principal.getName());


        if(amount<=0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Course> courses = courseService.fetchLastAddedPrivateCoursesBySubCategoryAndStaus(amount, principal.getName(), subCategoryId, CourseStatus.APPROVED);
        if(courses == null || courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);

    }

    @GetMapping(value = "/school/courses/lastCreated/{amount}/subCategory/{subCategoryId}/declined" , consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiOperation(value = "Get Last Created Courses by SubCategory in School, Status: DECLINED, Role:ROLE_ADMIN")
    public ResponseEntity<List<Course>> getLastAddedPrivateCoursesBySubCategoryWithStatusDeclined(
            @PathVariable Integer amount,
            @PathVariable Long subCategoryId,
            Principal principal) throws UserNotFoundExcepion, SchoolNotFoundExcepion, SubCategoryNotFoundException {

        log.debug("Principal username:"+principal.getName());


        if(amount<=0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Course> courses = courseService.fetchLastAddedPrivateCoursesBySubCategoryAndStaus(amount, principal.getName(), subCategoryId, CourseStatus.DECLINED);
        if(courses == null || courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);

    }
    //END: Last Added By SubCategory


    //START: Last Added By Section
    @GetMapping(value = "/school/courses/lastCreated/{amount}/section/{sectionId}/all" , consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiOperation(value = "Get Last Created Courses By Section in School, Status: all, Role:ROLE_ADMIN")
    public ResponseEntity<List<Course>> getLastAddedPrivateCoursesBySectionAnyStatus(
            @PathVariable Integer amount,
            @PathVariable Long sectionId,
            Principal principal) throws UserNotFoundExcepion, SchoolNotFoundExcepion, SectionNotFoundException {

        log.debug("Principal username:"+principal.getName());


        if(amount<=0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Course> courses = courseService.fetchLastAddedPrivateCoursesBySectionAndStatus(amount, principal.getName(), sectionId, null);
        if(courses == null || courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);

    }

    @GetMapping(value = "/school/courses/lastCreated/{amount}/section/{sectionId}/pending" , consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiOperation(value = "Get Last Created Courses By Section in School, Status: PENDING, Role:ROLE_ADMIN")
    public ResponseEntity<List<Course>> getLastAddedPrivateCoursesBySectionWithStatusPending(
            @PathVariable Integer amount,
            @PathVariable Long sectionId,
            Principal principal) throws UserNotFoundExcepion, SchoolNotFoundExcepion, SectionNotFoundException {

        log.debug("Principal username:"+principal.getName());


        if(amount<=0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Course> courses = courseService.fetchLastAddedPrivateCoursesBySectionAndStatus(amount, principal.getName(), sectionId, CourseStatus.PENDING);
        if(courses == null || courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);

    }

    @GetMapping(value = "/school/courses/lastCreated/{amount}/section/{sectionId}/approved" , consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiOperation(value = "Get Last Created Courses By Section in School, Status: APPROVED, Role:ROLE_ADMIN")
    public ResponseEntity<List<Course>> getLastAddedPrivateCoursesBySectionWithStatusApproved(
            @PathVariable Integer amount,
            @PathVariable Long sectionId,
            Principal principal) throws UserNotFoundExcepion, SchoolNotFoundExcepion, SectionNotFoundException {

        log.debug("Principal username:"+principal.getName());


        if(amount<=0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Course> courses = courseService.fetchLastAddedPrivateCoursesBySectionAndStatus(amount, principal.getName(), sectionId, CourseStatus.APPROVED);
        if(courses == null || courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);

    }

    @GetMapping(value = "/school/courses/lastCreated/{amount}/section/{sectionId}/declined" , consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiOperation(value = "Get Last Created Courses by Section in School, Status: DECLINED, Role:ROLE_ADMIN")
    public ResponseEntity<List<Course>> getLastAddedPrivateCoursesBySectionWithStatusDeclined(
            @PathVariable Integer amount,
            @PathVariable Long sectionId,
            Principal principal) throws UserNotFoundExcepion, SchoolNotFoundExcepion, SectionNotFoundException {

        log.debug("Principal username:"+principal.getName());


        if(amount<=0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Course> courses = courseService.fetchLastAddedPrivateCoursesBySectionAndStatus(amount, principal.getName(), sectionId, CourseStatus.DECLINED);
        if(courses == null || courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);

    }
    //START: Last Added By Section

    //START: Last Added By SubSection
    @GetMapping(value = "/school/courses/lastCreated/{amount}/subSection/{subSectionId}/all" , consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiOperation(value = "Get Last Created Courses By SubSection in School, Status: all, Role:ROLE_ADMIN")
    public ResponseEntity<List<Course>> getLastAddedPrivateCoursesBySubSectionAnyStatus(
            @PathVariable Integer amount,
            @PathVariable Long subSectionId,
            Principal principal) throws UserNotFoundExcepion, SchoolNotFoundExcepion, SubSectionNotFoundException {

        log.debug("Principal username:"+principal.getName());


        if(amount<=0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Course> courses = courseService.fetchLastAddedPrivateCoursesBySubSectionAndStatus(amount, principal.getName(), subSectionId, null);
        if(courses == null || courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);

    }

    @GetMapping(value = "/school/courses/lastCreated/{amount}/subSection/{subSectionId}/pending" , consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiOperation(value = "Get Last Created Courses By SubSection in School, Status: PENDING, Role:ROLE_ADMIN")
    public ResponseEntity<List<Course>> getLastAddedPrivateCoursesBySubSectionWithStatusPending(
            @PathVariable Integer amount,
            @PathVariable Long subSectionId,
            Principal principal) throws UserNotFoundExcepion, SchoolNotFoundExcepion, SubSectionNotFoundException {

        log.debug("Principal username:"+principal.getName());


        if(amount<=0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Course> courses = courseService.fetchLastAddedPrivateCoursesBySubSectionAndStatus(amount, principal.getName(), subSectionId, CourseStatus.PENDING);
        if(courses == null || courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);

    }

    @GetMapping(value = "/school/courses/lastCreated/{amount}/subSection/{subSectionId}/approved" , consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiOperation(value = "Get Last Created Courses By SubSection in School, Status: APPROVED, Role:ROLE_ADMIN")
    public ResponseEntity<List<Course>> getLastAddedPrivateCoursesBySubSectionWithStatusApproved(
            @PathVariable Integer amount,
            @PathVariable Long subSectionId,
            Principal principal) throws UserNotFoundExcepion, SchoolNotFoundExcepion, SubSectionNotFoundException {

        log.debug("Principal username:"+principal.getName());


        if(amount<=0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Course> courses = courseService.fetchLastAddedPrivateCoursesBySubSectionAndStatus(amount, principal.getName(), subSectionId, CourseStatus.APPROVED);
        if(courses == null || courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);

    }

    @GetMapping(value = "/school/courses/lastCreated/{amount}/subSection/{subSectionId}/declined" , consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiOperation(value = "Get Last Created Courses by SubSection in School, Status: DECLINED, Role:ROLE_ADMIN")
    public ResponseEntity<List<Course>> getLastAddedPrivateCoursesBySubSectionWithStatusDeclined(
            @PathVariable Integer amount,
            @PathVariable Long subSectionId,
            Principal principal) throws UserNotFoundExcepion, SchoolNotFoundExcepion, SubSectionNotFoundException {

        log.debug("Principal username:"+principal.getName());


        if(amount<=0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Course> courses = courseService.fetchLastAddedPrivateCoursesBySubSectionAndStatus(amount, principal.getName(), subSectionId, CourseStatus.DECLINED);
        if(courses == null || courses.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(courses, HttpStatus.OK);

    }
    //END: Last Added By SubSection

    @PostMapping("/signup-users-batch")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiOperation(value = "Batch Signup Users at School, Role:ROLE_ADMIN")
    public ResponseEntity<MessageResponse> signupUsersBatch(

            @RequestPart(value = "file") MultipartFile file,
            Principal principal)

            throws SaveUserException, UserNotFoundExcepion, EmptyValueException, CsvParseException, SchoolNotFoundExcepion, WorkspaceNameIsAlreadyTaken, RoleNotAllowedException, RoleFormatException, SubscriptionPlanNotSpecified {

        log.debug("Principal username:"+principal.getName());
        userService.batchSignupUsers(file, principal.getName());

        return new ResponseEntity<>(MessageResponse.of("Users created"),HttpStatus.CREATED);
    }

    @GetMapping(value = "/upcoming-sessions/{page}/{amount}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiOperation(value = "Get All Upcoming Live Sessions. Role: ROLE_ADMIN")
    public ResponseEntity<Page<SlotsResponseModel>> getUpcomingLiveSessions(
            @PathVariable Integer page,
            @PathVariable Integer amount,
            Principal principal) throws EmptyValueException, UserNotFoundExcepion, SchoolNotFoundExcepion {

        Page<SlotsResponseModel> upcomingSessions = timeSlotService.getSchoolUpcomingSessions(principal.getName(), page, amount);

        return  new ResponseEntity<>(upcomingSessions, HttpStatus.OK);
    }
}
