package com.sorbSoft.CabAcademie.Services;

import com.sorbSoft.CabAcademie.Entities.AppointmentSlot;
import com.sorbSoft.CabAcademie.Entities.User;
import com.sorbSoft.CabAcademie.Repository.AppointmentSlotsRepository;
import com.sorbSoft.CabAcademie.Repository.UserRepository;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.AppointmentSlotViewModel;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            slotsRepo.save(getEntity(vm));
        }
    }

    //read
    /*public List<AppointmentSlot> getSlotsByUserId(Long userId) {
        User user = userRepository.getOne(userId);
        List<AppointmentSlot> timeSlots = slotsRepo.findByUser(user);

        return timeSlots;
    }*/

    //update
    public void update(List<AppointmentSlot> appointmentSlots) {
        slotsRepo.save(appointmentSlots);
    }

    //delete
    public void deleteOne(Long slotId) {
        slotsRepo.delete(slotId);
    }

    //delete
    public void deleteMany(List<AppointmentSlot> appointmentSlots) {
        slotsRepo.delete(appointmentSlots);
    }

    private AppointmentSlot getEntity(AppointmentSlotViewModel vm){

        AppointmentSlot slot = new AppointmentSlot();
        slot.setId(vm.getId());

        if(vm.getUserId()==null || vm.getUserId()<=0) {
            throw new IllegalArgumentException("User id can't be zero or less!");
        }

        if(vm.getFrom() == null) {
            throw new IllegalArgumentException("Date From can't be null");
        }

        if(vm.getTo() == null) {
            throw new IllegalArgumentException("Date To can't be null");
        }

        Long userId = vm.getUserId();
        User user = userRepository.findById(userId);

        if(user == null) {
            throw new IllegalArgumentException("User with "+vm.getId()+" doesn't exist");
        }

        //if date from < date to
        //if date from > now()
        //if date to > now()

        //if dates in collection are overlapped
        //if dates from collection are overlapped with dates from db

        DateTime now = DateTime.now();

        slot.setUser(user);
        slot.setDateFrom(vm.getFrom());
        slot.setDateTo(vm.getTo());

        return slot;
    }

    private boolean isOverlapped(Interval i1, Interval i2) {
        return i1.getEnd().isBefore(i2.getStart()) || i1.getStart().isAfter(i2.getEnd());
    }
}
