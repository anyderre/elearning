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
@RequestMapping(value = "/api/student-dashboard")
public class StudentDashboardController {


    @Autowired
    private TimeSlotService timeSlotService;

    @GetMapping(value = "/upcoming-sessions/{page}/{amount}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_STUDENT') or hasAuthority('ROLE_FREE_STUDENT') or hasAuthority('ROLE_EMPLOYEE')")
    @ApiOperation(value = "Get All Upcoming Live Sessions. Role: ROLE_STUDENT, ROLE_FREE_STUDENT, ROLE_EMPLOYEE")
    public ResponseEntity<Page<SlotsResponseModel>> getUpcomingLiveSessions(
            @PathVariable Integer page,
            @PathVariable Integer amount,
            Principal principal)
            throws EmptyValueException, UserNotFoundExcepion {

        Page<SlotsResponseModel> upcomingSessions = timeSlotService.getStudentUpcomingSessions(principal.getName(), page, amount);

        return  new ResponseEntity<>(upcomingSessions, HttpStatus.OK);
    }
}
