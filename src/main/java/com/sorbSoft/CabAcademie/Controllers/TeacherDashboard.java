package com.sorbSoft.CabAcademie.Controllers;

import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.appointment.SlotsResponseModel;
import com.sorbSoft.CabAcademie.Services.TimeSlotService;
import com.sorbSoft.CabAcademie.exception.EmptyValueException;
import com.sorbSoft.CabAcademie.exception.UserNotFoundExcepion;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(value = "/api/teacher-dashboard")
public class TeacherDashboard {


    @Autowired
    private TimeSlotService timeSlotService;

    @GetMapping(value = "/upcoming-sessions", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_INSTRUCTOR') or hasAuthority('ROLE_PROFESSOR') or hasAuthority('ROLE_FREELANCER')")
    @ApiOperation(value = "Get All Upcoming Live Sessions. Role: ROLE_PROFESSOR, ROLE_INSTRUCTOR")
    public ResponseEntity<List<SlotsResponseModel>> getUpcommingLiveSessions(
            Principal principal)
            throws EmptyValueException, UserNotFoundExcepion {

        List<SlotsResponseModel> upcomingSessions = timeSlotService.getUpcomingSessions(principal.getName());

        return  new ResponseEntity<>(upcomingSessions, HttpStatus.OK);
    }
}
