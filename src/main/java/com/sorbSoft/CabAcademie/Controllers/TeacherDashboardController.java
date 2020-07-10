package com.sorbSoft.CabAcademie.Controllers;

import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.appointment.SlotsResponseModel;
import com.sorbSoft.CabAcademie.Services.TimeSlotService;
import com.sorbSoft.CabAcademie.exception.EmptyValueException;
import com.sorbSoft.CabAcademie.exception.UserNotFoundExcepion;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(value = "/api/teacher-dashboard")
public class TeacherDashboardController {


    @Autowired
    private TimeSlotService timeSlotService;

    @GetMapping(value = "/upcoming-sessions/{page}/{amount}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_INSTRUCTOR') or hasAuthority('ROLE_PROFESSOR') or hasAuthority('ROLE_FREELANCER')")
    @ApiOperation(value = "Get All Upcoming Live Sessions. Role: ROLE_PROFESSOR, ROLE_INSTRUCTOR, ROLE_FREELANCER")
    public ResponseEntity<Page<SlotsResponseModel>> getUpcommingLiveSessions(
            @PathVariable Integer page,
            @PathVariable Integer amount,
            Principal principal)
            throws EmptyValueException, UserNotFoundExcepion {

        Page<SlotsResponseModel> upcomingSessions = timeSlotService.getTeacherUpcomingSessions(principal.getName(), page, amount);

        return  new ResponseEntity<>(upcomingSessions, HttpStatus.OK);
    }


    @GetMapping(value = "/pending-students/count", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_INSTRUCTOR') or hasAuthority('ROLE_PROFESSOR') or hasAuthority('ROLE_FREELANCER')")
    @ApiOperation(value = "Get All Pending students. Role: ROLE_PROFESSOR, ROLE_INSTRUCTOR, ROLE_FREELANCER")
    public ResponseEntity<Long> countPendingStudents(
            Principal principal)
            throws EmptyValueException, UserNotFoundExcepion {

        long amount = timeSlotService.getPendingStudents(principal.getName());

        return  new ResponseEntity<>(amount, HttpStatus.OK);
    }
}
