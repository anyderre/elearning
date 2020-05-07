package com.sorbSoft.CabAcademie.Services;

import com.sorbSoft.CabAcademie.Entities.Attendee;
import com.sorbSoft.CabAcademie.Entities.Enums.AttendeeStatus;
import com.sorbSoft.CabAcademie.Entities.Enums.AppointmentType;
import com.sorbSoft.CabAcademie.Entities.TimeSlot;
import com.sorbSoft.CabAcademie.Entities.User;
import com.sorbSoft.CabAcademie.Repository.SlotsRepository;
import com.sorbSoft.CabAcademie.Repository.UserRepository;
import com.sorbSoft.CabAcademie.Services.Dtos.Validation.Result;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.appointment.GroupAppointmentViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class OneToManyAppointmeentService {

    @Value("${minimum.time.slot.size.minutes}")
    private Long MIN_SLOT_SIZE_MINUTES;

    @Autowired
    private SlotsRepository slotsR;

    @Autowired
    private UserRepository userR;

    @Autowired
    private TimeSlotValidator tsv;

    @Autowired
    private GroupAppointmentValidator gav;

    @Autowired
    private EmailApiService emailSender;

    //TODO: add appointment tsv service

    public Result subscribeToGroupMeeting(GroupAppointmentViewModel vm) {

        Result result = new Result();

        result = gav.validateSubscribeToMeeting(vm);
        if (!result.isValid()) return result;

        Long appointmentId = vm.getAppointmentId();

        TimeSlot groupTimeSlot = slotsR.findOne(appointmentId);

        if (groupTimeSlot == null || groupTimeSlot.getType()!=AppointmentType.GROUP) {
            result.add("There is no group appointment with id "+appointmentId);
            return result;
        }

        result = tsv.validateDatesBeforeNow(groupTimeSlot.getDateFrom(), groupTimeSlot.getDateTo());
        if (!result.isValid()) return result;

        result = gav.validateMaxEnrolled(
                groupTimeSlot.getMaxAttendees(),
                groupTimeSlot.getApprovedAttendee());
        if (!result.isValid()) return result;


        result = gav.validateIfAttendeeSubscribed(vm);
        if (!result.isValid()) return result;

        User student = userR.findById(vm.getStudentId());

        Attendee attendee = new Attendee();
        attendee.setUser(student);
        attendee.setStatus(AttendeeStatus.PENDING_APPROVAL);
        attendee.setTimeSlot(groupTimeSlot);
        attendee.setApprovalUid(generateUid());
        attendee.setDeclineUid(generateUid());

        List<Attendee> attendees = groupTimeSlot.getAttendees();

        if(attendees == null) {
            attendees = new ArrayList<>();
            attendees.add(attendee);
            groupTimeSlot.setAttendees(attendees);
        } else {
            attendees.add(attendee);
        }

        /*Long approvedAttendee = groupTimeSlot.getApprovedAttendee();
        if(approvedAttendee==null) {
            approvedAttendee = 0L;
        }
        approvedAttendee++;
        groupTimeSlot.setApprovedAttendee(approvedAttendee);*/

        //TODO: book

        //make student pending

        //send approval link to teacher

        //increase approvedAttendee

        //send video conference link to student

        //sendEmailToTeacher(booked);
        //sendEmailToStudent(booked);



        return result;
    }


    private void sendEmailToStudent(TimeSlot booked) {
        //send email to student to inform him
        //String to = student.getEmail();
        String toStudent = "postullat2@gmail.com";
        String subjectStudent = "Meeting request sent";
        String textStudent = "Dear Student, You request has been sent to teacher." + " We will notify you once teacher will approve it";
        //TODO: fix email send
        //TODO: generate link with uid
        //TODO: save uid in db
        //emailSender.sendSimpleMessage(toStudent, subjectStudent, textStudent);
        System.out.println("Email sent");
        //System.out.println("****Approval UID" + booked.getApprovalUid());
        //System.out.println("****Decline UID" + booked.getDeclineUid());


        //book
        //save splitted slot
    }

    private void sendEmailToTeacher(TimeSlot booked) {
        //String to = teacher.getEmail();
        String to = "w.volodymyr.bondarchuk@gmail.com";
        String subject = "Meeting request";
        String text = "Dear Teacher, One of the student is requesting meeting with you." + " Please confirm or decline";
        //TODO: fix email send
        //emailSender.sendSimpleMessage(to, subject, text);
        //send email to teacher
        System.out.println("Email sent");
    }

    private String generateUid() {
        UUID uuid = UUID.randomUUID();
        String id = uuid.toString().replace("-", "");
        Date now = new Date();

        return id + now.getTime();
    }



}
