package com.sorbSoft.CabAcademie.Controllers;

import com.sorbSoft.CabAcademie.Entities.Error.MessageResponse;
import com.sorbSoft.CabAcademie.Services.AppointmentSlotsService;
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
@RequestMapping(value = "/api/appointment/slots")
public class AppointmentSlotsController {

    @Autowired
    private AppointmentSlotsService slotsService;

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPER_ADMIN') or hasAuthority('ROLE_INSTRUCTOR')")
    public ResponseEntity<MessageResponse> saveAppointmentSlots(@Valid @RequestBody List<AppointmentSlotViewModel> appointmentVmSlots){

        /*
        Result result = categoryService.saveCategory(vm);
        if(!result.isValid())
            return new ResponseEntity<>(MessageResponse.of(result.lista.get(0).getMessage()), HttpStatus.CONFLICT);
        return  new ResponseEntity<>(MessageResponse.of("Category successfully added"), HttpStatus.CREATED);
         */
        slotsService.save(appointmentVmSlots);
        return  new ResponseEntity<>(MessageResponse.of("Appointment Slots Saved Successfully"), HttpStatus.OK);
    }

    @PostMapping(value = "/getByUserIdWithinDateRange", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppointmentSlotViewModel> getAppointmentSlotsByUserIdWithinDateRange(
            @Valid @RequestBody AppointmentSlotViewModel vmSlots){

        if(vmSlots.getUserId()==null
                || vmSlots.getUserId()==0
                || vmSlots.getDateFrom() == null
                || vmSlots.getDateTo() == null) {
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<AppointmentSlotViewModel> slots = slotsService.getSlotsByUserIdWithinDateRange(vmSlots);

        if(slots == null || slots.size() == 0)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity(slots, HttpStatus.OK);
    }

    @DeleteMapping(value = "/deleteOne/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPER_ADMIN') or hasAuthority('ROLE_INSTRUCTOR')")
    public ResponseEntity<MessageResponse> deleteSlot(@PathVariable Long id ){
        Result result = slotsService.deleteOne(id);
        if (!result.isValid()){
            return new ResponseEntity<>(MessageResponse.of(result.lista.get(0).getMessage()), HttpStatus.CONFLICT);
        }
        return  new ResponseEntity<>(MessageResponse.of("Slot has been successfully deleted"), HttpStatus.OK);
    }
}
