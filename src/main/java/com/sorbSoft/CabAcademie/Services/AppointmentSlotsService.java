package com.sorbSoft.CabAcademie.Services;

import com.sorbSoft.CabAcademie.Entities.AppointmentSlot;
import com.sorbSoft.CabAcademie.Entities.Category;
import com.sorbSoft.CabAcademie.Entities.User;
import com.sorbSoft.CabAcademie.Repository.AppointmentSlotsRepository;
import com.sorbSoft.CabAcademie.Repository.UserRepository;
import com.sorbSoft.CabAcademie.Services.Dtos.Validation.Result;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.AppointmentSlotViewModel;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.springframework.beans.factory.annotation.Autowired;
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

    //create
    public void save(List<AppointmentSlotViewModel> appointmentVmSlots) {
        for(AppointmentSlotViewModel vm : appointmentVmSlots) {
            if(vm.getId()<=0) {
                validate(vm);
                AppointmentSlot entity = getEntity(vm);
                slotsRepo.save(entity);
            } else {
                update(vm);
            }
        }
    }

    private void update(AppointmentSlotViewModel vm) {
        //TODO: implement
    }

    //read
    public List<AppointmentSlotViewModel> getSlotsByUserIdWithinDateRange(AppointmentSlotViewModel vm) {
        Date dateFrom = vm.getDateFrom();
        Date dateTo = vm.getDateTo();
        Long userId = vm.getUserId();

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

    //update
    public void update(List<AppointmentSlot> appointmentSlots) {
        slotsRepo.save(appointmentSlots);
    }

    //delete
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

    //delete
    public void deleteMany(List<AppointmentSlotViewModel> vms) {

        List<AppointmentSlot> entities = getEntities(vms);
        slotsRepo.delete(entities);
    }

    private void validate(AppointmentSlotViewModel vm){
        if(vm.getUserId()==null || vm.getUserId()<=0) {
            throw new IllegalArgumentException("User id can't be zero or less!");
        }

        if(vm.getDateFrom() == null) {
            throw new IllegalArgumentException("Date From can't be null");
        }

        if(vm.getDateTo() == null) {
            throw new IllegalArgumentException("Date To can't be null");
        }

        Long userId = vm.getUserId();
        User user = userRepository.findById(userId);

        if(user == null) {
            throw new IllegalArgumentException("User with "+vm.getId()+" doesn't exist");
        }

        DateTime  curFrom = new DateTime(vm.getDateFrom());
        DateTime  curTo = new DateTime(vm.getDateTo());
        DateTime  now = DateTime.now();

        if(curFrom.isAfter(curTo)) {
            throw new IllegalArgumentException("Date 'From' can't be after Date 'To'");
        }
        if(curFrom.isEqual(curTo)) {
            throw new IllegalArgumentException("Date 'From' can't be Equal to Date 'To'");
        }

        if(curFrom.isBefore(now)) {
            throw new IllegalArgumentException("Date 'From' can't be before 'Now'");
        }
        if(curTo.isBefore(now)) {
            throw new IllegalArgumentException("Date 'To' can't be before 'Now'");
        }


        List<AppointmentSlot> dbSlots = slotsRepo.findByUser(user);

        for(AppointmentSlot sl : dbSlots) {
            DateTime dbFrom = new DateTime(sl.getDateFrom());
            DateTime dbTo = new DateTime(sl.getDateTo());

            if(isOverlapped(curFrom, curTo, dbFrom, dbTo)) {
                throw new IllegalArgumentException
                        ("Your dates "+curFrom+"-"+curTo+" overlap with db dates "+dbFrom+"-"+dbTo+" for user "+user.getId());
            }
        }

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
