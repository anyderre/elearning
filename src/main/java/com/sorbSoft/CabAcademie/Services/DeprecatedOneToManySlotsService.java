package com.sorbSoft.CabAcademie.Services;

import com.sorbSoft.CabAcademie.Entities.AppointmentPrice;
import com.sorbSoft.CabAcademie.Entities.Enums.TimeSlotStatus;
import com.sorbSoft.CabAcademie.Entities.Enums.AppointmentType;
import com.sorbSoft.CabAcademie.Entities.TimeSlot;
import com.sorbSoft.CabAcademie.Entities.User;
import com.sorbSoft.CabAcademie.Repository.SlotsRepository;
import com.sorbSoft.CabAcademie.Repository.UserRepository;
import com.sorbSoft.CabAcademie.Services.Dtos.Validation.Result;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.appointment.OneToManySlotsAddRequestModel;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.appointment.SlotsResponseModel;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class DeprecatedOneToManySlotsService {

    @Autowired
    private SlotsRepository slotsRepo;

    @Autowired
    private UserRepository userRepository;

    //create
    public Result save(OneToManySlotsAddRequestModel vm) {

        Result result = new Result();

        if(vm.getId()<=0) {
            result = validateAddNewSlotByTeacher(vm);

            if(!result.isValid()) {
                return result;
            }
            TimeSlot entity = getPublicSlot(vm);
            slotsRepo.save(entity);
        } else {
            result.add("No update implemented yet. Please remove and add new entity");
            return result;
        }


        return result;
    }



    private List<SlotsResponseModel> getSlotsVms(List<TimeSlot> slotsByUserWithinDateRange) {

        List<SlotsResponseModel> list = new ArrayList<>();

        for(TimeSlot ap : slotsByUserWithinDateRange) {
            SlotsResponseModel vm = getSlotVm(ap);
            list.add(vm);

        }

        return list;
    }



    private SlotsResponseModel getSlotVm(TimeSlot slot) {

       /* SlotsResponseModel vm = new SlotsResponseModel();

        vm.setId(slot.getId());
        vm.setTeacherId(slot.getTeacher().getId());
        vm.setDateFrom(slot.getDateFrom());
        vm.setDateTo(slot.getDateTo());


        vm.setStatus(slot.getStatus().toString());
        vm.setType(slot.getType().toString());
        vm.setMinMinutes(slot.getMinMinutes());
        vm.setPrices(slot.getPrices());
        vm.setApprovalBeforeMinutes(slot.getApprovalBeforeMinutes());

        return vm;*/

       return null;
    }



    private Result validateDateFromToOrder(Date from, Date to) {

        Result result = new Result();

        DateTime  curFrom = new DateTime(from);
        DateTime  curTo = new DateTime(to);

        if(curFrom.isAfter(curTo)) {
            result.add("Date 'From' can't be after Date 'To'");
            return result;
        }
        if(curFrom.isEqual(curTo)) {
            result.add("Date 'From' can't be Equal to Date 'To'");
            return result;
        }

        return result;
    }

    private Result validateUserExists(OneToManySlotsAddRequestModel vm){
        Result result = new Result();

        Long userId = vm.getTeacherId();
        User user = userRepository.getOne(userId);

        if(user == null) {
            result.add("User with "+vm.getId()+" doesn't exist");
            return result;
        }

        return result;
    }

    private Result validateNull(OneToManySlotsAddRequestModel vm){

        Result result = new Result();

        if(vm.getTeacherId()==null || vm.getTeacherId()<=0) {
            result.add("User id can't be zero or less!");
            return result;
        }

        if(vm.getPrice()==null || vm.getPrice().getPrice()<=0) {
            result.add("Price can not be zero or null");
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

        return result;
    }

    private Result validateAddNewSlotByTeacher(OneToManySlotsAddRequestModel vm){

        Result result = new Result();

        result = validateNull(vm);

        if(!result.isValid()) {
            return result;
        }


        result = validateUserExists(vm);

        if(!result.isValid()) {
            return result;
        }




        result = validateDateFromToOrder(vm.getDateFrom(), vm.getDateTo());

        if(!result.isValid()) {
            return result;
        }



        result = validateDatesBeforeNow(vm.getDateFrom(), vm.getDateTo());

        if(!result.isValid()) {
            return result;
        }


        result = validateDateOverlappedWithDbSlots(vm);

        if(!result.isValid()) {
            return result;
        }



        return result;

    }

    private Result validateDateOverlappedWithDbSlots(OneToManySlotsAddRequestModel vm) {
        Result result = new Result();

        Date from = removeSeconds(vm.getDateFrom());
        Date to = removeSeconds(vm.getDateTo());

        DateTime  curFrom = new DateTime(from);
        DateTime  curTo = new DateTime(to);

        User user = userRepository.getOne(vm.getTeacherId());

        List<TimeSlot> dbSlots = slotsRepo.findByTeacher(user);

        for(TimeSlot sl : dbSlots) {
            DateTime dbFrom = new DateTime(sl.getDateFrom());
            DateTime dbTo = new DateTime(sl.getDateTo());

            if(isOverlapped(curFrom, curTo, dbFrom, dbTo)) {
                result.add("Your dates "+curFrom+"-"+curTo+" overlap with db dates "+dbFrom+"-"+dbTo+" for user "+user.getId());
                return result;
            }
        }

        return result;
    }

    private Result validateDatesBeforeNow(Date from, Date to) {
        Result result = new Result();

        DateTime  curFrom = new DateTime(from);
        DateTime  curTo = new DateTime(to);
        DateTime  now = DateTime.now();

        if(curFrom.isBefore(now)) {
            result.add("Date 'From' can't be before 'Now'");
            return result;
        }
        if(curTo.isBefore(now)) {
            result.add("Date 'To' can't be before 'Now'");
            return result;
        }

        return result;
    }

    private TimeSlot getPublicSlot(OneToManySlotsAddRequestModel vm){

        /*TimeSlot slot = new TimeSlot();
        slot.setId(vm.getId());

        User user = userRepository.getOne(vm.getTeacherId());

        slot.setTeacher(user);

        Date from = removeSeconds(vm.getDateFrom());
        Date to = removeSeconds(vm.getDateTo());

        slot.setDateFrom(from);
        slot.setDateTo(to);
        slot.setStatus(TimeSlotStatus.AVAILABLE);
        slot.setType(AppointmentType.GROUP);
        slot.setMaxAttendees(vm.getMaxAttendees());

        List<AppointmentPrice> prices = new ArrayList<>();
        prices.add(vm.getPrice());

        slot.setPrices(prices);

        slot.setApprovalBeforeMinutes(vm.getApprovalBeforeMinutes());

        return slot;*/
        return null;
    }

    private Date removeSeconds(Date date) {
        Calendar cal = Calendar.getInstance(); // locale-specific
        cal.setTime(date);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }

    private boolean isOverlapped(DateTime start1, DateTime end1, DateTime start2, DateTime end2) {
        Interval interval = new Interval( start1, end1 );
        Interval interval2 = new Interval( start2, end2 );
        return interval.overlaps( interval2 );
    }


}
