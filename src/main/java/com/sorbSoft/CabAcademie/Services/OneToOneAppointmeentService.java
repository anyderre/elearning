package com.sorbSoft.CabAcademie.Services;

import com.sorbSoft.CabAcademie.Entities.*;
import com.sorbSoft.CabAcademie.Entities.Enums.AppointmentType;
import com.sorbSoft.CabAcademie.Entities.Enums.AttendeeStatus;
import com.sorbSoft.CabAcademie.Entities.Enums.TimeSlotStatus;
import com.sorbSoft.CabAcademie.Repository.AttendeeRepository;
import com.sorbSoft.CabAcademie.Repository.SlotsRepository;
import com.sorbSoft.CabAcademie.Repository.UserRepository;
import com.sorbSoft.CabAcademie.Services.Dtos.Helpers.DatePointName;
import com.sorbSoft.CabAcademie.Services.Dtos.Validation.Result;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.appointment.OneToOneAppointmentMakeRequestModel;
import com.sorbSoft.CabAcademie.Services.email.EmailApiService;
import com.sorbSoft.CabAcademie.Utils.DateUtils;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class OneToOneAppointmeentService {

    @Value("${minimum.time.slot.size.minutes}")
    private Long MIN_SLOT_SIZE_MINUTES;

    @Autowired
    private SlotsRepository slotsR;

    @Autowired
    private UserRepository userR;

    @Autowired
    private AttendeeRepository attendeeR;

    @Autowired
    private EmailApiService emailSender;

    //TODO: add appointment validator service

    public Result book121Meeting(OneToOneAppointmentMakeRequestModel vm) {

        Result result = new Result();

        result = validateBookMeeting(vm);
        if (!result.isValid()) return result;

        removeSeconds(vm);


        User teacher = userR.findById(vm.getTeacherId());
        TimeSlot availableSlot = slotsR.getAvailableSlot(vm.getDateFrom(), vm.getDateTo(), teacher);

        if (availableSlot == null) {
            result.add("There is no available slots for time you specified");
            return result;
        }

        if (availableSlot.getType() == AppointmentType.GROUP) {
            result.add("There is no available PRIVATE slots for time you specified");
            return result;
        }

        result = validateAppointmentRestrictions(vm, availableSlot);
        if (!result.isValid()) return result;


        DateTime slotFrom = new DateTime(availableSlot.getDateFrom());
        DateTime slotTo = new DateTime(availableSlot.getDateTo());

        DateTime meetingFrom = new DateTime(vm.getDateFrom());
        DateTime meetingTo = new DateTime(vm.getDateTo());

        result = validateIfMeetingFitsSlot(slotFrom, slotTo, meetingFrom, meetingTo);
        if (!result.isValid()) return result;

        Attendee attendee = new Attendee();

        //- if suggested meeting time == slot -> book
        if (meetingFrom.isEqual(slotFrom) && meetingTo.isEqual(slotTo)) {

            makePendingApprovalAndClosed(availableSlot, attendee, vm);

            slotsR.save(availableSlot);
            result.add("Just Booked");
            emailSender.sendAppointmentRequestToTeacherMail(availableSlot, attendee);
            emailSender.sendAppointmentRequestNotificationToStudentMail(availableSlot, attendee);
        } else {

            Map<DatePointName, DateTime> datePoints = calculateSlotsPoints(meetingFrom, meetingTo, slotFrom, slotTo);

            DateTime s1 = datePoints.get(DatePointName.SLOT1_START);
            DateTime e1 = datePoints.get(DatePointName.SLOT1_END);

            DateTime s2 = datePoints.get(DatePointName.SLOT2_START);
            DateTime e2 = datePoints.get(DatePointName.SLOT2_END);


            TimeSlot slotBefore = null;
            TimeSlot slotAfter = null;

            if (s1 != null && e1 != null) {
                slotBefore = createNewOpenedSlot(s1, e1, availableSlot, vm);
            }

            if (s2 != null && e2 != null) {
                slotAfter = createNewOpenedSlot(s2, e2, availableSlot, vm);
            }

            TimeSlot booked = makePendingApprovalAndClosed(meetingFrom, meetingTo, availableSlot, attendee, vm);


            //if slots that left < 10 min then clean them
            if (slotBefore != null && getMinutesDuration(s1, e1) > MIN_SLOT_SIZE_MINUTES) {
                slotsR.save(slotBefore);
            } else {
                result.add("Slot 1 is less than " + MIN_SLOT_SIZE_MINUTES + " minutes. That's why it has been removed.");
            }

            if (slotAfter != null && getMinutesDuration(s2, e2) > MIN_SLOT_SIZE_MINUTES) {
                slotsR.save(slotAfter);
            } else {
                result.add("Slot 2 is less than " + MIN_SLOT_SIZE_MINUTES + " minutes. That's why it has been removed.");
            }

            slotsR.delete(availableSlot);
            slotsR.save(booked);
            result.add("Splited and Booked");


            emailSender.sendAppointmentRequestToTeacherMail(booked, attendee);
            emailSender.sendAppointmentRequestNotificationToStudentMail(booked, attendee);

        }

        return result;
    }

    private Result validateIfMeetingFitsSlot(DateTime slotFrom, DateTime slotTo, DateTime meetingFrom, DateTime meetingTo) {

        Result result = new Result();

        if (!slotFrom.isEqual(meetingFrom) && slotFrom.isAfter(meetingFrom)) {
            result.add("Requested appointment start is before than start of available time slot");
            return result;
        }

        if (!slotTo.isEqual(meetingTo) && slotTo.isBefore(meetingTo)) {
            result.add("Requested appointment end time is more than available time slot");
            return result;
        }

        return result;
    }

    private Map<DatePointName, DateTime> calculateSlotsPoints(DateTime meetingFrom, DateTime meetingTo, DateTime slotFrom, DateTime slotTo) {
        DateTime s1 = null;
        DateTime e1 = null;

        DateTime s2 = null;
        DateTime e2 = null;

        if (meetingFrom.isAfter(slotFrom) && meetingTo.isBefore(slotTo)) {
            //split slot
            s1 = new DateTime(slotFrom);
            e1 = new DateTime(meetingFrom.minusMinutes(1));

            s2 = new DateTime(meetingTo.plusMinutes(1));
            e2 = new DateTime(slotTo);

            //if suggested meeting time < available slot
        } else if (meetingFrom.isEqual(slotFrom) && meetingTo.isBefore(slotTo)) {
            //split slot
            s1 = new DateTime(meetingTo.plusMinutes(1));
            e1 = new DateTime(slotTo);

            //if suggested meeting time < available slot
        } else if (meetingFrom.isAfter(slotFrom) && meetingTo.isEqual(slotTo)) {
            //split slot
            s1 = new DateTime(slotFrom);
            e1 = new DateTime(meetingFrom.minusMinutes(1));
        }

        Map<DatePointName, DateTime> datePoints = new HashMap<>();
        datePoints.put(DatePointName.SLOT1_START, s1);
        datePoints.put(DatePointName.SLOT1_END, e1);

        datePoints.put(DatePointName.SLOT2_START, s2);
        datePoints.put(DatePointName.SLOT2_END, e2);

        return datePoints;
    }

    private TimeSlot makePendingApprovalAndClosed(DateTime meetingFrom, DateTime meetingTo, TimeSlot availableSlot, Attendee attendee, OneToOneAppointmentMakeRequestModel vm) {
        TimeSlot booked = new TimeSlot();


        Long userId = availableSlot.getTeacher().getId();
        User user = userR.findById(userId);

        booked.setTeacher(user);
        booked.setDateFrom(meetingFrom.toDate());
        booked.setDateTo(meetingTo.toDate());
        booked.setStatus(TimeSlotStatus.CLOSED);
        booked.setBookBeforeMinutes(availableSlot.getBookBeforeMinutes());
        booked.setMinMinutes(availableSlot.getMinMinutes());
        booked.setType(availableSlot.getType());
        booked.setMaxAttendees(1L);

        List<AppointmentPrice> prices = new ArrayList<>();
        prices.addAll(availableSlot.getPrices());
        booked.setPrices(prices);

        String approvalUid = generateUid();
        String declineUid = generateUid();

        User student = userR.findById(vm.getStudentId());
        List<Attendee> attendees = new ArrayList<>();
        attendee.setTimeSlot(booked);
        attendee.setUser(student);
        attendee.setStatus(AttendeeStatus.PENDING_APPROVAL);
        attendees.add(attendee);
        attendee.setApprovalUid(approvalUid);
        attendee.setDeclineUid(declineUid);

        booked.setAttendees(attendees);

        if (vm.getSuggestedPrice() != null && vm.getSuggestedPrice() > 0) {
            AppointmentPrice price = new AppointmentPrice();
            price.setPrice(vm.getSuggestedPrice());
            price.setCurrency(vm.getCurrency());
            booked.setSuggestedPrice(price);
        }
        //TODO: calculate a price

        return booked;
    }

    private void makePendingApprovalAndClosed(TimeSlot availableSlot, Attendee attendee, OneToOneAppointmentMakeRequestModel vm) {
        String approvalUid = generateUid();
        String declineUid = generateUid();

        User student = userR.findById(vm.getStudentId());
        List<Attendee> attendees = new ArrayList<>();

        attendee.setTimeSlot(availableSlot);
        attendee.setUser(student);
        attendee.setStatus(AttendeeStatus.PENDING_APPROVAL);
        attendee.setApprovalUid(approvalUid);
        attendee.setDeclineUid(declineUid);

        attendees.add(attendee);

        availableSlot.setAttendees(attendees);

        availableSlot.setStatus(TimeSlotStatus.CLOSED);
    }

    private TimeSlot createNewOpenedSlot(DateTime fromDate, DateTime toDate, TimeSlot availableSlot, OneToOneAppointmentMakeRequestModel vm) {
        TimeSlot slot = new TimeSlot();

        Long userId = availableSlot.getTeacher().getId();
        User user = userR.findById(userId);
        slot.setTeacher(user);

        slot.setDateFrom(fromDate.toDate());
        slot.setDateTo(toDate.toDate());
        slot.setStatus(TimeSlotStatus.OPENED);
        slot.setMaxAttendees(1L);


        List<AppointmentPrice> prices = new ArrayList<>(availableSlot.getPrices().size());
        copyPrices(prices, availableSlot.getPrices());
        slot.setPrices(prices);


        slot.setBookBeforeMinutes(availableSlot.getBookBeforeMinutes());
        slot.setType(availableSlot.getType());
        slot.setMinMinutes(availableSlot.getMinMinutes());

        return slot;
    }

    private void copyPrices(List<AppointmentPrice> dest, List<AppointmentPrice> src) {
        for (int i = 0; i < src.size(); i++) {
            AppointmentPrice price = new AppointmentPrice();
            price.setPrice(src.get(i).getPrice());
            price.setCurrency(src.get(i).getCurrency());
            price.setMinutes(src.get(i).getMinutes());

            dest.add(price);
        }
    }

    private String generateUid() {
        UUID uuid = UUID.randomUUID();
        String id = uuid.toString().replace("-", "");
        Date now = new Date();

        return id + now.getTime();
    }

    private Result validateAppointmentRestrictions(OneToOneAppointmentMakeRequestModel vm, TimeSlot availableSlot) {

        Result result = new Result();

        //check if requested date range meet slot criteria
        Long minMinutes = availableSlot.getMinMinutes();

        List<AppointmentPrice> prices = availableSlot.getPrices();

        AppointmentPrice price = Collections.min(prices, Comparator.comparing(s -> s.getPrice()));
        Long minPrice = price.getPrice();

        if (vm.getSuggestedPrice() != null && vm.getSuggestedPrice() > 0) {
            Long suggestedPrice = vm.getSuggestedPrice();

            if (suggestedPrice < minPrice) {
                result.add("Suggested price is less than minimal allowed");
                return result;
            }
        }


        Duration duration = new Duration(vm.getDateFrom().getTime(), vm.getDateTo().getTime());
        Long requestedMinutes = duration.getStandardMinutes();

        if (requestedMinutes < minMinutes) {
            result.add("Requested time is less than minimal allowed");
            return result;
        }

        return result;

    }

    private Result validateBookMeeting(OneToOneAppointmentMakeRequestModel vm) {
        Result result = new Result();

        result = validateNull(vm);
        if (!result.isValid()) return result;

        result = validateDateFromToOrder(vm.getDateFrom(), vm.getDateTo());

        if (!result.isValid()) return result;


        result = validateDatesAfterNow(vm.getDateFrom(), vm.getDateTo());

        if (!result.isValid()) return result;

        result = validateUserExists(vm);

        if (!result.isValid()) return result;

        return result;
    }

    private void removeSeconds(OneToOneAppointmentMakeRequestModel vm) {
        Date from = vm.getDateFrom();
        Date to = vm.getDateTo();

        from = DateUtils.removeSeconds(from);
        to = DateUtils.removeSeconds(to);

        vm.setDateFrom(from);
        vm.setDateTo(to);
    }

    private long getMinutesDuration(DateTime start, DateTime end) {
        return new Duration(start, end).getStandardMinutes();
    }


    private Result validateDateFromToOrder(Date from, Date to) {

        Result result = new Result();

        DateTime curFrom = new DateTime(from);
        DateTime curTo = new DateTime(to);

        if (curFrom.isAfter(curTo)) {
            result.add("Date 'From' can't be after Date 'To'");
            return result;
        }
        if (curFrom.isEqual(curTo)) {
            result.add("Date 'From' can't be Equal to Date 'To'");
            return result;
        }

        return result;
    }

    private Result validateUserExists(OneToOneAppointmentMakeRequestModel vm) {
        Result result = new Result();

        Long teacherId = vm.getTeacherId();
        User teacher = userR.findById(teacherId);

        Long studentId = vm.getStudentId();
        User student = userR.findById(studentId);

        if (teacher == null) {
            result.add("Teacher with " + teacherId + " doesn't exist");
            return result;
        }

        if (student == null) {
            result.add("Student with " + studentId + " doesn't exist");
            return result;
        }

        return result;
    }

    private Result validateNull(OneToOneAppointmentMakeRequestModel vm) {

        Result result = new Result();

        if (vm.getTeacherId() == null || vm.getTeacherId() <= 0) {
            result.add("Teacher id can't be zero or less!");
            return result;
        }

        if (vm.getStudentId() == null || vm.getStudentId() <= 0) {
            result.add("Student id can't be zero or less!");
            return result;
        }

        if (vm.getDateFrom() == null) {
            result.add("Date From can't be null");
            return result;
        }

        if (vm.getDateTo() == null) {
            result.add("Date To can't be null");
            return result;
        }

        return result;
    }


    private Result validateDatesAfterNow(Date from, Date to) {
        Result result = new Result();

        DateTime curFrom = new DateTime(from);
        DateTime curTo = new DateTime(to);
        DateTime now = DateTime.now();

        if (curFrom.isBefore(now)) {
            result.add("Date 'From' can't be before 'Now'");
            return result;
        }
        if (curTo.isBefore(now)) {
            result.add("Date 'To' can't be before 'Now'");
            return result;
        }

        return result;
    }


    public Result approveAppointment(String uid) {

        Result result = new Result();

        Attendee attendee = attendeeR.findByApprovalUid(uid);
        attendee.getTimeSlot();

        //if exist
        if (attendee == null) {
            result.add("Attendee with uid " + uid + " doesn't exist or it has been already approved");
            return result;
        }

        //if status Waiting approval
        if (attendee.getStatus() == AttendeeStatus.PENDING_APPROVAL) {
            TimeSlot timeSlot = attendee.getTimeSlot();
            if (timeSlot != null) {

                //TODO: add price calculation here


                Long maxAttendees = timeSlot.getMaxAttendees();
                Long approvedAttendee = timeSlot.getApprovedAttendee();

                if (approvedAttendee == null) {
                    approvedAttendee = 0L;
                }

                approvedAttendee++;

                if(approvedAttendee <= maxAttendees) {
                    timeSlot.setApprovedAttendee(approvedAttendee);
                    attendee.setStatus(AttendeeStatus.APPROVED);
                    attendee.setApprovalUid("");
                    //attendee.setDeclineUid("");

                    if(approvedAttendee == maxAttendees) {
                        timeSlot.setStatus(TimeSlotStatus.CLOSED);
                    }

                    attendeeR.save(attendee);

                    emailSender.sendApproveNotificationToTeacher(timeSlot, attendee);
                    emailSender.sendApproveNotificationToStudent(timeSlot, attendee);
                } else {
                    result.add("Attendee can't be approved due to max attendees reached");
                    return result;
                }


            } else {
                result.add("Appointment/Time slot does not exist for attendee Id: " + attendee.getId());
                return result;
            }


        } else if (attendee.getStatus() == AttendeeStatus.APPROVED) {
            result.add("Attendee has been already approved");
            return result;
        } else if (attendee.getStatus() == AttendeeStatus.CANCELED) {
            result.add("Appointment is not scheduled or has been canceled by student");
            return result;
        }

        return result;
    }

    public Result declineAppointment(String uid) {
        Result result = new Result();

        Attendee attendee = attendeeR.findByDeclineUid(uid);

        //if exist
        if (attendee == null) {
            result.add("Attendee with id " + uid + " doesn't exist for declining or it has been already declined");
            return result;
        }

        //if status Waiting approval
        if (attendee.getStatus() == AttendeeStatus.PENDING_APPROVAL
                || attendee.getStatus() == AttendeeStatus.APPROVED) {
            TimeSlot timeSlot = attendee.getTimeSlot();
            if (timeSlot != null) {

                Long maxAttendees = timeSlot.getMaxAttendees();
                Long approvedAttendee = timeSlot.getApprovedAttendee();

                if(attendee.getStatus() == AttendeeStatus.APPROVED) {
                        approvedAttendee--;
                        timeSlot.setStatus(TimeSlotStatus.OPENED);
                        timeSlot.setApprovedAttendee(approvedAttendee);

                }

                //decline
                attendee.setStatus(AttendeeStatus.REJECTED);
                attendee.setDeclineUid("");
                attendee.setApprovalUid("");

                attendeeR.save(attendee);

                emailSender.sendDeclineNotificationToTeacher(timeSlot, attendee);
                emailSender.sendDeclineNotificationToStudent(timeSlot, attendee);
            } else {
                result.add("Appointment/Time slot does not exist for attendee Id: " + attendee.getId());
                return result;
            }

        } else {
            result.add("Appointment is not scheduled or has been canceled/declined");
            return result;


        }

        return result;
    }

    public Result cancelAppointment(String uid) {
        return declineAppointment(uid);
    }
}
