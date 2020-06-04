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

    @GetMapping(value = "/school/students/count")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Long> getStudentsAmount(Principal principal) throws SchoolNotFoundExcepion, UserNotFoundExcepion {

        if (principal == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Long studentsCount = userService.countUsersInSchoolByRole(principal.getName(), Roles.ROLE_STUDENT);
        if (studentsCount == null || studentsCount <= 0)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(studentsCount, HttpStatus.OK);
    }

    @GetMapping(value = "/school/professors/count")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
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
    public ResponseEntity<Long> getCoursesAmount(Principal principal) throws SchoolNotFoundExcepion, UserNotFoundExcepion {

        if (principal == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Long coursesCount = courseService.countAllCoursesInSchool(principal.getName());
        if (coursesCount == null || coursesCount <= 0)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(coursesCount, HttpStatus.OK);
    }

    @GetMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserViewModel> getCourseViewModel(@RequestParam(value = "filterRoles", defaultValue = "1") String filterRoles) {
        UserViewModel vm = userService.getUserViewModel(null, filterRoles);
        if (vm == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(vm, HttpStatus.OK);
    }


    @GetMapping(value = "/all", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> findAllUsers() {
        List<User> users = userService.findAllUser();
        if (users.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping(value = "/professors", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> findAllProfessors() {
        List<User> users = userService.findAllProfessor();
        if (users.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping(value = "/professor/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> findProfessor(@PathVariable Long id) {
        User professor = userService.findProfessor(id);
        if (professor == null) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(professor, HttpStatus.OK);
    }

    @GetMapping(value = "/info", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserInfo>> findUserInfo() {
        List<UserInfo> users = userService.getUserInfo();
        if (users.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }


    @DeleteMapping(value = "/{id}")
    public ResponseEntity<MessageResponse> deleteUser(@PathVariable Long id) {
        Result result = userService.deleteUser(id);
        if (!result.isValid()) {
            return new ResponseEntity<>(MessageResponse.of(result.lista.get(0).getMessage()), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(MessageResponse.of("Section successfully deleted"), HttpStatus.OK);
    }


}
