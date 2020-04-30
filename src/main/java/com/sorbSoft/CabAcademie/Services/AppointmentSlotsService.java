package com.sorbSoft.CabAcademie.Services;

import com.sorbSoft.CabAcademie.Entities.AppointmentSlot;
import com.sorbSoft.CabAcademie.Entities.Category;
import com.sorbSoft.CabAcademie.Entities.Enums.AppointmentStatus;
import com.sorbSoft.CabAcademie.Entities.User;
import com.sorbSoft.CabAcademie.Repository.AppointmentSlotsRepository;
import com.sorbSoft.CabAcademie.Repository.UserRepository;
import com.sorbSoft.CabAcademie.Services.Dtos.Validation.Result;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.AppointmentSlotViewModel;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class AppointmentSlotsService {

    @Autowired
    private AppointmentSlotsRepository slotsRepo;

    @Autowired
    private UserRepository userRepository;

    public Result bookMeeting(AppointmentSlotViewModel vm) {

        //check if time range > 10 min
        //check if time range < 1h

        Result result = new Result();

        User teacher = userRepository.findById(vm.getUserId());
        List<AppointmentSlot> slots = slotsRepo.getAvailableSlots(vm.getDateFrom(), vm.getDateTo(), teacher);

        AppointmentSlot availableSlot = slots.get(0);
        DateTime slotFrom = new DateTime(availableSlot.getDateFrom());
        DateTime slotTo = new DateTime(availableSlot.getDateTo());

        DateTime meetingFrom = new DateTime(vm.getDateFrom());
        DateTime meetingTo = new DateTime(vm.getDateTo());

        //- if suggested meeting time == slot -> book
        if(meetingFrom.isEqual(slotFrom) && meetingTo.isEqual(slotTo)) {
            //book
            availableSlot.setStatus(AppointmentStatus.WAITING_APPROVAL);
            slotsRepo.save(availableSlot);
            result.add("Just Booked");
        } else {

            DateTime s1 = null;
            DateTime e1 = null;

            DateTime s2 = null;
            DateTime e2 = null;

            if(meetingFrom.isAfter(slotFrom) && meetingTo.isBefore(slotTo)) {
                //split slot
                s1 = new DateTime(slotFrom);
                e1 = new DateTime(meetingFrom.minusMinutes(1));

                s2 = new DateTime(meetingTo.plusMinutes(1));
                e2 = new DateTime(slotTo);

                //if suggested meeting time < available slot
            } else if(meetingFrom.isEqual(slotFrom) && meetingTo.isBefore(slotTo)) {
                //split slot
                s1 = new DateTime(meetingFrom.plusMinutes(1));
                e1 = new DateTime(slotTo);

                //if suggested meeting time < available slot
            } else if (meetingFrom.isAfter(slotFrom) && meetingTo.isEqual(slotTo)) {
                //split slot
                s1 = new DateTime(slotFrom);
                e1 = new DateTime(meetingFrom.minusMinutes(1));
            }

            Long userId = availableSlot.getUser().getId();
            User user = userRepository.findById(userId);

            //split by creating 2 smaller time slots
            AppointmentSlot slot1 = new AppointmentSlot();
            slot1.setUser(user);
            slot1.setDateFrom(s1.toDate());
            slot1.setDateTo(e1.toDate());
            slot1.setStatus(AppointmentStatus.AVAILABLE);

            //split by creating 2 smaller time slots
            AppointmentSlot slot2 = new AppointmentSlot();
            slot2.setUser(user);
            slot2.setDateFrom(s2.toDate());
            slot2.setDateTo(e2.toDate());
            slot2.setStatus(AppointmentStatus.AVAILABLE);

            //book time slot
            AppointmentSlot booked = new AppointmentSlot();
            booked.setUser(user);
            booked.setDateFrom(meetingFrom.toDate());
            booked.setDateTo(meetingTo.toDate());
            booked.setStatus(AppointmentStatus.WAITING_APPROVAL);

            slotsRepo.save(slot1);
            slotsRepo.save(slot2);
            slotsRepo.delete(availableSlot);
            slotsRepo.save(booked);
            result.add("Splited and Booked");

            //send email to teacher
            //send email to student to inform him



            //book
            //save splitted slot
        }


        //- if suggested meeting time < available slot -> split time slot
        //- if suggested meeting time > available slot -> check neighbor slots. book, throw exception of check next neighbor
        //get available slots
        //book -> mark busy

        return result;
    }

    //create
    public Result save(List<AppointmentSlotViewModel> appointmentVmSlots) {

        Result result = new Result();

        for(AppointmentSlotViewModel vm : appointmentVmSlots) {
            if(vm.getId()<=0) {
                result = validate(vm);

                if(!result.isValid()) {
                    return result;
                }
                AppointmentSlot entity = getEntity(vm);
                slotsRepo.save(entity);
            } else {
                result.add("No update implemented yet. Please remove and add new entity");
                return result;
            }
        }

        return result;
    }

    public List<AppointmentSlotViewModel> getSlotsByUserIdWithinDateRange(AppointmentSlotViewModel vm) {
        Date dateFrom = vm.getDateFrom();
        Date dateTo = vm.getDateTo();
        Long userId = vm.getUserId();

        //TODO: add validation and message return
        User user = userRepository.findById(userId);

        List<AppointmentSlot> slotsByUserWithinDateRange = slotsRepo.findSlotsByUserWithinDateRange(dateFrom, dateTo, user);

        List<AppointmentSlotViewModel> vms = getSlotsVms(slotsByUserWithinDateRange);

        return vms;

    }

    private List<AppointmentSlotViewModel> getSlotsVms(List<AppointmentSlot> slotsByUserWithinDateRange) {

        List<AppointmentSlotViewModel> list = new ArrayList<>();

        for(AppointmentSlot ap : slotsByUserWithinDateRange) {
            AppointmentSlotViewModel vm = getSlotVm(ap);
            list.add(vm);

        }

        return list;
    }

    private AppointmentSlotViewModel getSlotVm(AppointmentSlot ap) {

        AppointmentSlotViewModel vm = new AppointmentSlotViewModel();

        vm.setId(ap.getId());
        vm.setUserId(ap.getUser().getId());
        vm.setDateFrom(ap.getDateFrom());
        vm.setDateTo(ap.getDateTo());

        return vm;
    }


    public List<AppointmentSlotViewModel> getAllSlotsByUserId(Long userId) {

        User user = userRepository.findById(userId);
        List<AppointmentSlot> timeSlots = slotsRepo.findByUser(user);

        List<AppointmentSlotViewModel> vmList = new ArrayList<>();

        for (AppointmentSlot dbSlot : timeSlots) {

            AppointmentSlotViewModel vm = new AppointmentSlotViewModel();

            vm.setId(dbSlot.getId());
            vm.setUserId(dbSlot.getUser().getId());
            vm.setDateFrom(dbSlot.getDateFrom());
            vm.setDateTo(dbSlot.getDateTo());

            vmList.add(vm);
        }

        return vmList;
    }

    public Result deleteOne(Long slotId) {
        Result result = new Result();
        if (slotId <= 0L) {
            result.add("You should indicate the id of the appointment");
            return result;
        }
        AppointmentSlot slot = slotsRepo.findOne(slotId);
        if (slot == null) {
            result.add("The slot you want to delete doesn't exist");
            return result;
        }

        try {
            slotsRepo.delete(slotId);
        } catch (Exception ex)  {
            result.add(ex.getMessage());
            return result;
        }
        return result;
    }

    public Result deleteSlotsByUserIdWithinDateRange(AppointmentSlotViewModel vm) {
        Result result = new Result();

        if(vm.getUserId()==null
                || vm.getUserId()<=0) {
            result.add("User id can't be null, zero or below zero");
            return result;
        }


        result = validateDate(new DateTime(vm.getDateFrom()), new DateTime(vm.getDateTo()));
        if(!result.isValid()) {
            return result;
        }


       User user = userRepository.findById(vm.getUserId());

        if(user==null) {
            result.add("User with id "+ vm.getUserId() +" does not exist in db");
            return result;
        }

        List<AppointmentSlot> dbSlots = slotsRepo.findSlotsByUserWithinDateRange(vm.getDateFrom(), vm.getDateTo(), user);

        if(dbSlots==null || dbSlots.isEmpty()) {
            result.add("There are no slots to delete with a given date range and user");
            return result;
        }

        try {
            slotsRepo.removeSlotsByUserWithDateRange(vm.getDateFrom(), vm.getDateTo(), user);
        } catch (Exception ex)  {
            result.add(ex.getMessage());
            return result;
        }
        return result;
    }


    public Result deleteAllSlotsForUserId(Long userId) {
        Result result = new Result();
        if(userId==null || userId<=0) {
            result.add("User id can't be null, zero or below zero");
            return result;
        }

        User user = userRepository.findById(userId);

        if(user == null) {
            result.add("User with id "+userId+" does not exist");
            return result;
        }

        slotsRepo.removeAllSlotsForUser(user);

        return result;
    }

    private Result validateDate(DateTime from, DateTime to) {

        Result result = new Result();

        if(from.isAfter(to)) {
            result.add("Date 'From' can't be after Date 'To'");
            return result;
        }
        if(from.isEqual(to)) {
            result.add("Date 'From' can't be Equal to Date 'To'");
            return result;
        }

        return result;
    }

    private Result validate(AppointmentSlotViewModel vm){

        Result result = new Result();

        if(vm.getUserId()==null || vm.getUserId()<=0) {
            result.add("User id can't be zero or less!");
            return result;
        }

        if(vm.getDateFrom() == null) {
            result.add("Date From can't be null");
            return result;
        }

        if(vm.getDateTo() == null) {
            result.add("Date To can't be null");
            return result;
        }

        Long userId = vm.getUserId();
        User user = userRepository.findById(userId);

        if(user == null) {
            result.add("User with "+vm.getId()+" doesn't exist");
            return result;
        }

        DateTime  curFrom = new DateTime(vm.getDateFrom());
        DateTime  curTo = new DateTime(vm.getDateTo());
        DateTime  now = DateTime.now();

        result = validateDate(curFrom, curTo);

        if(!result.isValid()) {
            return result;
        }


        if(curFrom.isBefore(now)) {
            result.add("Date 'From' can't be before 'Now'");
            return result;
        }
        if(curTo.isBefore(now)) {
            result.add("Date 'To' can't be before 'Now'");
            return result;
        }


        List<AppointmentSlot> dbSlots = slotsRepo.findByUser(user);

        for(AppointmentSlot sl : dbSlots) {
            DateTime dbFrom = new DateTime(sl.getDateFrom());
            DateTime dbTo = new DateTime(sl.getDateTo());

            if(isOverlapped(curFrom, curTo, dbFrom, dbTo)) {
                result.add("Your dates "+curFrom+"-"+curTo+" overlap with db dates "+dbFrom+"-"+dbTo+" for user "+user.getId());
                return result;
            }
        }

        return result;

    }

    List<AppointmentSlot> getEntities(List<AppointmentSlotViewModel> vms) {

        List<AppointmentSlot> list = new ArrayList<>();

        for(AppointmentSlotViewModel vm : vms) {
            AppointmentSlot entity = getEntity(vm);
            list.add(entity);
        }

        return list;
    }

    private AppointmentSlot getEntity(AppointmentSlotViewModel vm){

        AppointmentSlot slot = new AppointmentSlot();
        slot.setId(vm.getId());

        User user = userRepository.findById(vm.getUserId());

        slot.setUser(user);
        slot.setDateFrom(vm.getDateFrom());
        slot.setDateTo(vm.getDateTo());

        return slot;
    }

    private boolean isOverlapped(DateTime start1, DateTime end1, DateTime start2, DateTime end2) {
        Interval interval = new Interval( start1, end1 );
        Interval interval2 = new Interval( start2, end2 );
        return interval.overlaps( interval2 );
    }


}
