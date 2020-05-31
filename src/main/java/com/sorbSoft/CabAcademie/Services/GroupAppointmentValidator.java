package com.sorbSoft.CabAcademie.Services;

import com.sorbSoft.CabAcademie.Entities.Attendee;
import com.sorbSoft.CabAcademie.Entities.Enums.AppointmentType;
import com.sorbSoft.CabAcademie.Entities.Enums.AttendeeStatus;
import com.sorbSoft.CabAcademie.Entities.TimeSlot;
import com.sorbSoft.CabAcademie.Entities.User;
import com.sorbSoft.CabAcademie.Repository.AttendeeRepository;
import com.sorbSoft.CabAcademie.Repository.SlotsRepository;
import com.sorbSoft.CabAcademie.Repository.UserRepository;
import com.sorbSoft.CabAcademie.Services.Dtos.Validation.Result;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.appointment.GroupAppointmentViewModel;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.appointment.SlotAddRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupAppointmentValidator {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SlotsRepository slotsRepo;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private GenericValidator validator;

    @Autowired
    private AttendeeRepository attendeeRepository;



    public Result validateSubscribeToMeeting(GroupAppointmentViewModel vm) {
        Result result = new Result();

        result = validateNull(vm);
        if (!result.isValid()) return result;


        result = userValidator.validateUserExists(vm.getStudentId());

        if (!result.isValid()) return result;

        return result;
    }


    public Result validateNull(GroupAppointmentViewModel vm) {

        Result result = new Result();

        result = validator.validateNull(vm.getAppointmentId(), "Appointment id");
        if (!result.isValid()) return result;

        result = validator.validateNull(vm.getStudentId(), "Student id");
        if (!result.isValid()) return result;

        return result;
    }

    public Result validateMaxEnrolled(Long maxAttendees, Long enrolledAttendees) {

        Result result = new Result();

        if(enrolledAttendees!=null && enrolledAttendees>=maxAttendees) {
            result.add("Max amount of approved attendees has been reached");
            return result;
        }

        return result;

    }

    public Result validateIfAttendeeSubscribed(GroupAppointmentViewModel vm) {

        Result result = new Result();

        User user = userRepository.getOne(vm.getStudentId());
        TimeSlot timeSlot = slotsRepo.getOne(vm.getAppointmentId());

        List<Attendee> attendees = attendeeRepository.findByUserAndTimeSlot(user, timeSlot);

        for(Attendee attendee : attendees) {
            if(attendee.getStatus() == AttendeeStatus.APPROVED) {
                result.add("You have been already subscribed to this appointment. Your status: "+AttendeeStatus.APPROVED);
                return result;
            }
            if(attendee.getStatus() == AttendeeStatus.PENDING_APPROVAL) {
                result.add("You have been already subscribed to this appointment. Your status: "+AttendeeStatus.PENDING_APPROVAL);
                return result;
            }
        }

        return result;
    }

}
