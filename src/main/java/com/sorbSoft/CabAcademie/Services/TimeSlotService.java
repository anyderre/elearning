package com.sorbSoft.CabAcademie.Services;

import com.sorbSoft.CabAcademie.Entities.Attendee;
import com.sorbSoft.CabAcademie.Entities.Enums.TimeSlotStatus;
import com.sorbSoft.CabAcademie.Entities.Enums.AppointmentType;
import com.sorbSoft.CabAcademie.Entities.TimeSlot;
import com.sorbSoft.CabAcademie.Entities.User;
import com.sorbSoft.CabAcademie.Repository.SlotsRepository;
import com.sorbSoft.CabAcademie.Repository.UserRepository;
import com.sorbSoft.CabAcademie.Services.Dtos.Validation.Result;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.appointment.*;
import com.sorbSoft.CabAcademie.Utils.DateUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
@Log4j2
public class TimeSlotService {

    @Autowired
    private SlotsRepository slotsRepo;

    @Autowired
    private UserRepository userR;

    @Autowired
    private TimeSlotValidator validator;

    @Autowired
    private TimeZoneConverter tzConverter;


    //create
    public Result save(List<SlotAddRequestModel> appointmentVmSlots) {

        Result result = new Result();


        for(SlotAddRequestModel vmTimeZoned : appointmentVmSlots) {

            SlotAddRequestModel vmUtc = tzConverter.convertFromTimeZonedToUtc(vmTimeZoned);

            if(vmUtc.getId()<=0) {
                result = validator.validateAddNewSlotByTeacher(vmUtc);
                if(!result.isValid()) return result;

                TimeSlot entity = getEntity(vmUtc);
                slotsRepo.save(entity);
            } else {
                result.add("No update implemented yet. Please remove and add new entity");
                return result;
            }
        }

        return result;
    }



    public Result getSlotsByUserIdWithinDateRange(SlotsGetRequestModel vmTimeZoned) {

        Result result = new Result();

        SlotsGetRequestModel vm = tzConverter.convertFromTimeZonedToUtc(vmTimeZoned);

        Date dateFrom = DateUtils.removeSeconds(vm.getDateFrom());
        Date dateTo = DateUtils.removeSeconds(vm.getDateTo());
        Long userId = vm.getTeacherId();

        result = validator.validateGetSlotsByUserIdWithinDateRange(dateFrom, dateTo, userId);
        if(!result.isValid()) return result;


        User user = userR.findById(userId);

        List<TimeSlot> slotsByUserWithinDateRange = slotsRepo.findSlotsByUserWithinDateRange(dateFrom, dateTo, user);

        List<SlotsResponseModel> vms = getSlotsVms(slotsByUserWithinDateRange);

        for(SlotsResponseModel responseModel : vms) {
            //it changes value by object link, no need to copy/clone values
            tzConverter.convertFromUtcToTimeZoned(responseModel, vmTimeZoned.getRequesterId());
        }

        result.addValue(vms);

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

        SlotsResponseModel vm = new SlotsResponseModel();

        vm.setTimeSlotId(slot.getId());
        vm.setAppointmentId(slot.getId());
        vm.setTeacherId(slot.getTeacher().getId());

        List<AttendeeModel> attendees = convertAttendees(slot.getAttendees());
        vm.setAttendees(attendees);
        vm.setDateFrom((Date) slot.getDateFrom().clone());
        vm.setDateTo((Date)slot.getDateTo().clone());

        vm.setPrices(slot.getPrices());
        vm.setMinMinutes(slot.getMinMinutes());
        vm.setMaxAttendees(slot.getMaxAttendees());

        vm.setEnrolledAttendees(slot.getApprovedAttendee());
        vm.setBookBeforeMinutes(slot.getBookBeforeMinutes());
        vm.setStatus(slot.getStatus());

        vm.setType(slot.getType());

        return vm;
    }

    private List<AttendeeModel> convertAttendees(List<Attendee> attendees) {

        List<AttendeeModel> list = new ArrayList<>();

        for(Attendee at : attendees) {
            AttendeeModel model = new AttendeeModel();

            model.setUserId(at.getUser().getId());
            model.setFirstName(at.getUser().getFirstName());
            model.setLastName(at.getUser().getLastName());

            model.setUsername(at.getUser().getUsername());
            model.setPhotoURL(at.getUser().getPhotoURL());
            model.setTimeSlotId(at.getTimeSlot().getId());
            model.setStatus(at.getStatus());

            list.add(model);
        }

        return list;
    }

    public Result deleteOne(Long slotId) {
        Result result = new Result();
        if (slotId <= 0L) {
            result.add("You should indicate the id of the appointment");
            return result;
        }
        TimeSlot slot = slotsRepo.findOne(slotId);
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

    public Result getAllSlotsByUserId(Long userId, Long requesterId) {

        Result result = new Result();

        if(userId==null
                || userId<=0) {
            result.add("User id can't be null, zero or below zero");
            return result;
        }

        User user = userR.findById(userId);
        List<TimeSlot> timeSlots = slotsRepo.findByTeacher(user);

        List<SlotsResponseModel> vms = getSlotsVms(timeSlots);

        for(SlotsResponseModel responseModel : vms) {
            //it changes value by object link, no need to copy/clone values
            tzConverter.convertFromUtcToTimeZoned(responseModel, requesterId);
        }


        result.addValue(vms);

        return result;
    }

    public Result deleteSlotsByUserIdWithinDateRange(SlotDeleteRequestModel vmTimeZoned) {
        Result result = new Result();

        SlotDeleteRequestModel vmUtc = tzConverter.convertFromTimeZonedToUtc(vmTimeZoned);

        if(vmUtc.getTeacherId()==null
                || vmUtc.getTeacherId()<=0) {
            result.add("User id can't be null, zero or below zero");
            return result;
        }


        result = validator.validateDateFromToOrder(vmUtc.getDateFrom(), vmUtc.getDateTo());
        if(!result.isValid()) {
            return result;
        }


       User user = userR.findById(vmUtc.getTeacherId());

        if(user==null) {
            result.add("User with id "+ vmUtc.getTeacherId() +" does not exist in db");
            return result;
        }

        List<TimeSlot> dbSlots = slotsRepo.findSlotsByUserWithinDateRange(vmUtc.getDateFrom(), vmUtc.getDateTo(), user);

        if(dbSlots==null || dbSlots.isEmpty()) {
            result.add("There are no slots to delete with a given date range and user");
            return result;
        }

        try {
            slotsRepo.removeSlotsByUserWithDateRange(vmUtc.getDateFrom(), vmUtc.getDateTo(), user);
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

        User user = userR.findById(userId);

        if(user == null) {
            result.add("User with id "+userId+" does not exist");
            return result;
        }

        slotsRepo.removeAllSlotsForUser(user);

        return result;
    }


    private TimeSlot getEntity(SlotAddRequestModel vm){

        TimeSlot slot = new TimeSlot();
        slot.setId(vm.getId());

        User user = userR.findById(vm.getTeacherId());

        slot.setTeacher(user);

        Date from = DateUtils.removeSeconds(vm.getDateFrom());
        Date to = DateUtils.removeSeconds(vm.getDateTo());

        slot.setDateFrom(from);
        slot.setDateTo(to);

        slot.setStatus(TimeSlotStatus.OPENED);


        slot.setBookBeforeMinutes(vm.getBookBeforeMinutes());

        slot.setMinMinutes(vm.getMinMinutes());
        slot.setType(vm.getTimeSlotType());
        slot.setPrices(vm.getPrices());

        AppointmentType type = vm.getTimeSlotType();
        if(type == AppointmentType.PRIVATE) {
            slot.setMaxAttendees(1L);
        }
        if(type == AppointmentType.GROUP) {
            slot.setMaxAttendees(vm.getMaxAttendees());
        }

        return slot;
    }

}
