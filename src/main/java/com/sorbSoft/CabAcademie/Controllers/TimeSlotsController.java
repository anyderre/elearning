package com.sorbSoft.CabAcademie.Controllers;

import com.sorbSoft.CabAcademie.Entities.Error.MessageResponse;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.appointment.SlotAddRequestModel;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.appointment.SlotsGetRequestModel;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.appointment.SlotsResponseModel;
import com.sorbSoft.CabAcademie.Services.TimeSlotService;
import com.sorbSoft.CabAcademie.Services.Dtos.Validation.Result;
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
public class TimeSlotsController {

    @Autowired
    private TimeSlotService timeSlotService;


    @PostMapping(value = "/addSlots", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPER_ADMIN') or hasAuthority('ROLE_INSTRUCTOR')")
    public ResponseEntity<MessageResponse> add121Slots(@Valid @RequestBody List<SlotAddRequestModel> timeSlotVm){

        Result result = timeSlotService.save(timeSlotVm);
        if(!result.isValid())
            return new ResponseEntity<>(MessageResponse.of(result.lista.get(0).getMessage()), HttpStatus.CONFLICT);
        return  new ResponseEntity<>(MessageResponse.of("Appointment Slots Saved Successfully"), HttpStatus.OK);
    }


    @PostMapping(value = "/getByTeacherIdWithinDateRange", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SlotsResponseModel> getSlotsByUserIdWithinDateRange(
            @Valid @RequestBody SlotsGetRequestModel vmSlots){

        if(vmSlots.getTeacherId()==null
                || vmSlots.getTeacherId()==0
                || vmSlots.getDateFrom() == null
                || vmSlots.getDateTo() == null) {
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<SlotsResponseModel> slots = (List<SlotsResponseModel>) timeSlotService.getSlotsByUserIdWithinDateRange(vmSlots);

        if(slots == null || slots.size() == 0)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity(slots, HttpStatus.OK);
    }

    @DeleteMapping(value = "/deleteByTeacherIdWithinDateRange", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPER_ADMIN') or hasAuthority('ROLE_INSTRUCTOR')")
    public ResponseEntity<MessageResponse> deleteSlotsByUserIdWithinDateRange(
            @Valid @RequestBody SlotAddRequestModel vmSlots){

        if(vmSlots==null
            || vmSlots.getDateFrom() == null
            || vmSlots.getDateTo() == null) {
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Result result = timeSlotService.deleteSlotsByUserIdWithinDateRange(vmSlots);

        if (!result.isValid()){
            return new ResponseEntity<>(MessageResponse.of(result.lista.get(0).getMessage()), HttpStatus.CONFLICT);
        }

        return new ResponseEntity(MessageResponse.of("Slots have been successfully deleted"), HttpStatus.OK);
    }

    @DeleteMapping(value = "/deleteOne/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPER_ADMIN') or hasAuthority('ROLE_INSTRUCTOR')")
    public ResponseEntity<MessageResponse> deleteSlot(@PathVariable Long id ){
        Result result = timeSlotService.deleteOne(id);
        if (!result.isValid()){
            return new ResponseEntity<>(MessageResponse.of(result.lista.get(0).getMessage()), HttpStatus.CONFLICT);
        }
        return  new ResponseEntity<>(MessageResponse.of("Slot has been successfully deleted"), HttpStatus.OK);
    }

    @DeleteMapping(value = "/deleteAllByTeacherId/{teacherId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPER_ADMIN') or hasAuthority('ROLE_INSTRUCTOR')")
    public ResponseEntity<MessageResponse> deleteAllByUserId(@PathVariable Long teacherId ){
        Result result = timeSlotService.deleteAllSlotsForUserId(teacherId);
        if (!result.isValid()){
            return new ResponseEntity<>(MessageResponse.of(result.lista.get(0).getMessage()), HttpStatus.CONFLICT);
        }
        return  new ResponseEntity<>(MessageResponse.of("Slot has been successfully deleted"), HttpStatus.OK);
    }
}
