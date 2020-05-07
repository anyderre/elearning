package com.sorbSoft.CabAcademie.Controllers;

import com.sorbSoft.CabAcademie.Entities.Error.MessageResponse;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.appointment.GroupAppointmentViewModel;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.appointment.OneToOneAppointmentMakeRequestModel;
import com.sorbSoft.CabAcademie.Services.OneToManyAppointmeentService;
import com.sorbSoft.CabAcademie.Services.OneToOneAppointmeentService;
import com.sorbSoft.CabAcademie.Services.Dtos.Validation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/appointment")
public class AppointmentController {

    @Autowired
    private OneToOneAppointmeentService oneToOneAppointmeentService;

    @Autowired
    private OneToManyAppointmeentService oneToManyAppointmeentService;

    @PostMapping(value = "/oneToOne/make", consumes = MediaType.APPLICATION_JSON_VALUE)
    //add roles
    public ResponseEntity<MessageResponse> make121Appointment(@Valid @RequestBody OneToOneAppointmentMakeRequestModel appointmentVmSlot) {

        Result result = oneToOneAppointmeentService.book121Meeting(appointmentVmSlot);
        if (!result.isValid())
            return new ResponseEntity<>(MessageResponse.of(result.lista.get(0).getMessage()), HttpStatus.CONFLICT);
        return new ResponseEntity<>(MessageResponse.of("Appointment has been made successfully"), HttpStatus.OK);
    }

    @PostMapping(value = "/oneToMany/make", consumes = MediaType.APPLICATION_JSON_VALUE)
    //add roles
    public ResponseEntity<MessageResponse> makeOneToManyAppointment(@Valid @RequestBody GroupAppointmentViewModel appointmentVmSlot) {

        Result result = oneToManyAppointmeentService.subscribeToGroupMeeting(appointmentVmSlot);
        if (!result.isValid())
            return new ResponseEntity<>(MessageResponse.of(result.lista.get(0).getMessage()), HttpStatus.CONFLICT);
        return new ResponseEntity<>(MessageResponse.of("Appointment has been made successfully"), HttpStatus.OK);
    }

    @GetMapping(value = "/approveByTeacher/{uid}")
    //add roles
    public ResponseEntity<MessageResponse> approveAppointment(@PathVariable String uid) {

        if (uid == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Result result = oneToOneAppointmeentService.approve121Appointment(uid);

        if (!result.isValid())
            return new ResponseEntity<>(MessageResponse.of(result.lista.get(0).getMessage()), HttpStatus.CONFLICT);

        return new ResponseEntity<>(MessageResponse.of("Appointment has been approved"), HttpStatus.OK);
    }

    @GetMapping(value = "/declineByTeacher/{uid}")
    //add roles
    public ResponseEntity<MessageResponse> declineAppointment(@PathVariable String uid) {

        if (uid == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Result result = oneToOneAppointmeentService.decline121Appointment(uid);

        if (!result.isValid())
            return new ResponseEntity<>(MessageResponse.of(result.lista.get(0).getMessage()), HttpStatus.CONFLICT);

        return new ResponseEntity<>(MessageResponse.of("Appointment has been declined"), HttpStatus.OK);
    }

    @GetMapping(value = "/cancelByAttendee/{uid}")
    //add roles
    public ResponseEntity<MessageResponse> cancelAppointment(@PathVariable String uid) {

        if (uid == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Result result = oneToOneAppointmeentService.cancel121Appointment(uid);

        if (!result.isValid())
            return new ResponseEntity<>(MessageResponse.of(result.lista.get(0).getMessage()), HttpStatus.CONFLICT);

        return new ResponseEntity<>(MessageResponse.of("Appointment has been canceled"), HttpStatus.OK);
    }


}
