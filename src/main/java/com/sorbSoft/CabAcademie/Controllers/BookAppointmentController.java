package com.sorbSoft.CabAcademie.Controllers;

import com.sorbSoft.CabAcademie.Entities.Error.MessageResponse;
import com.sorbSoft.CabAcademie.Services.AppointmentSlotsService;
import com.sorbSoft.CabAcademie.Services.BookAppointmentService;
import com.sorbSoft.CabAcademie.Services.Dtos.Validation.Result;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.AppointmentSlotViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api/appointment")
public class BookAppointmentController {

    @Autowired
    private AppointmentSlotsService bookService;

    @PostMapping(value = "/book", consumes = MediaType.APPLICATION_JSON_VALUE)
    //add roles
    public ResponseEntity<MessageResponse> bookAppointmentSlots(@Valid @RequestBody AppointmentSlotViewModel appointmentVmSlot){

        Result result = bookService.bookMeeting(appointmentVmSlot);
        if(!result.isValid())
            return new ResponseEntity<>(MessageResponse.of(result.lista.get(0).getMessage()), HttpStatus.CONFLICT);
        return  new ResponseEntity<>(MessageResponse.of("Appointment Slots Saved Successfully"), HttpStatus.OK);
    }


}
