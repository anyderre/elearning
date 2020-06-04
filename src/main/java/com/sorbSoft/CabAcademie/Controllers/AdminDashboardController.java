package com.sorbSoft.CabAcademie.Controllers;

import com.sorbSoft.CabAcademie.Entities.Enums.Roles;
import com.sorbSoft.CabAcademie.Entities.Error.MessageResponse;
import com.sorbSoft.CabAcademie.Entities.User;
import com.sorbSoft.CabAcademie.Repository.UserRepository;
import com.sorbSoft.CabAcademie.Services.CourseService;
import com.sorbSoft.CabAcademie.Services.Dtos.Info.UserInfo;
import com.sorbSoft.CabAcademie.Services.Dtos.Validation.Result;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.UserViewModel;
import com.sorbSoft.CabAcademie.Services.UserServices;
import com.sorbSoft.CabAcademie.exception.SchoolNotFoundExcepion;
import com.sorbSoft.CabAcademie.exception.UserNotFoundExcepion;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping("/api/admin/dashboard")
@Log4j2
public class AdminDashboardController {

    @Autowired
    private UserServices userService;

    @Autowired
    private CourseService courseService;

    @GetMapping(value = "/school/students/count/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiOperation(value = "Count students in School. Statuses: all")
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
    @ApiOperation(value = "Count professors in School. Statuses: all")
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
    @ApiOperation(value = "Count courses in School. Statuses: all")
    public ResponseEntity<Long> getCoursesAmount(Principal principal) throws SchoolNotFoundExcepion, UserNotFoundExcepion {

        if (principal == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Long coursesCount = courseService.countAllCoursesInSchool(principal.getName());
        if (coursesCount == null || coursesCount <= 0)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(coursesCount, HttpStatus.OK);
    }
}
