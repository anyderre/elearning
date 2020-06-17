package com.sorbSoft.CabAcademie.Controllers;

import com.sorbSoft.CabAcademie.Entities.Error.MessageResponse;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.appointment.GroupAppointmentViewModel;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.appointment.OneToOneAppointmentMakeRequestModel;
import com.sorbSoft.CabAcademie.Services.Appointment12nService;
import com.sorbSoft.CabAcademie.Services.Appointmeent121Service;
import com.sorbSoft.CabAcademie.Services.Dtos.Validation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/appointment")
public class AppointmentController {

    @Autowired
    private Appointmeent121Service appointmeent121Service;

    @Autowired
    private Appointment12nService appointment12nService;

    @PostMapping(value = "/oneToOne/make", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN') " +
            "or hasAuthority('ROLE_SUPER_ADMIN') " +
            "or hasAuthority('ROLE_STUDENT') " +
            "or hasAuthority('ROLE_EMPLOYEE') " +
            "or hasAuthority('ROLE_FREE_STUDENT')")
    public ResponseEntity<MessageResponse> make121Appointment(@Valid @RequestBody OneToOneAppointmentMakeRequestModel appointmentVmSlot) {

        Result result = appointmeent121Service.book121Meeting(appointmentVmSlot);
        if (!result.isValid())
            return new ResponseEntity<>(MessageResponse.of(result.lista.get(0).getMessage()), HttpStatus.CONFLICT);
        return new ResponseEntity<>(MessageResponse.of("Appointment has been made successfully"), HttpStatus.OK);
    }

    @PostMapping(value = "/oneToMany/make", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN') " +
            "or hasAuthority('ROLE_SUPER_ADMIN') " +
            "or hasAuthority('ROLE_STUDENT') " +
            "or hasAuthority('ROLE_EMPLOYEE') " +
            "or hasAuthority('ROLE_FREE_STUDENT')")
    public ResponseEntity<MessageResponse> makeOneToManyAppointment(@Valid @RequestBody GroupAppointmentViewModel appointmentVmSlot) {

        Result result = appointment12nService.subscribeToGroupMeeting(appointmentVmSlot);
        if (!result.isValid())
            return new ResponseEntity<>(MessageResponse.of(result.lista.get(0).getMessage()), HttpStatus.CONFLICT);
        return new ResponseEntity<>(MessageResponse.of("Appointment has been made successfully"), HttpStatus.OK);
    }

    @GetMapping(value = "/approve/{uid}")
    public ResponseEntity<MessageResponse> approveAppointment(@PathVariable String uid) {

        if (uid == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Result result = appointmeent121Service.approveAppointment(uid);

        if (!result.isValid())
            return new ResponseEntity<>(MessageResponse.of(result.lista.get(0).getMessage()), HttpStatus.CONFLICT);

        return new ResponseEntity<>(MessageResponse.of("Appointment has been approved"), HttpStatus.OK);
    }

    @GetMapping(value = "/decline/{uid}")
    public ResponseEntity<MessageResponse> declineAppointment(@PathVariable String uid) {

        if (uid == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Result result = appointmeent121Service.declineAppointment(uid);

        if (!result.isValid())
            return new ResponseEntity<>(MessageResponse.of(result.lista.get(0).getMessage()), HttpStatus.CONFLICT);

        return new ResponseEntity<>(MessageResponse.of("Appointment has been cancelled"), HttpStatus.OK);
    }

    @GetMapping(value = "/cancel/{uid}")
    public ResponseEntity<MessageResponse> cancelAppointment(@PathVariable String uid) {

        if (uid == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Result result = appointmeent121Service.cancelAppointment(uid);

        if (!result.isValid())
            return new ResponseEntity<>(MessageResponse.of(result.lista.get(0).getMessage()), HttpStatus.CONFLICT);

        return new ResponseEntity<>(MessageResponse.of("Appointment has been canceled"), HttpStatus.OK);
    }
}
