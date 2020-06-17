package com.sorbSoft.CabAcademie.Services;

import com.sorbSoft.CabAcademie.Entities.AppointmentPrice;
import com.sorbSoft.CabAcademie.Entities.Enums.AppointmentType;
import com.sorbSoft.CabAcademie.Entities.TimeSlot;
import com.sorbSoft.CabAcademie.Entities.User;
import com.sorbSoft.CabAcademie.Repository.SlotsRepository;
import com.sorbSoft.CabAcademie.Repository.UserRepository;
import com.sorbSoft.CabAcademie.Services.Dtos.Validation.Result;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.appointment.SlotAddRequestModel;
import com.sorbSoft.CabAcademie.Utils.DateUtils;
import lombok.extern.log4j.Log4j2;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Log4j2
public class TimeSlotValidator {

    @Autowired
    private SlotsRepository slotsRepo;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private GenericValidator validator;

    @Autowired
    private UserRepository userR;

    public Result validateAddNewSlotByTeacher(SlotAddRequestModel vm) {

        Result result = new Result();

        result = validateAppointmentType(vm);
        if (!result.isValid()) return result;


        AppointmentType timeSlotType = vm.getTimeSlotType();

        if (timeSlotType == AppointmentType.PRIVATE) {

            result = validate121Null(vm);
            if (!result.isValid()) return result;

            result = validateTeacherAndDates(vm);
            if (!result.isValid()) return result;

        } else if (timeSlotType == AppointmentType.GROUP) {

            result = validate12ManyNull(vm);
            if (!result.isValid()) return result;

            result = validateTeacherAndDates(vm);
            if (!result.isValid()) return result;
        } else {
            result.add("Time slot type is not correct or has been corrupted");
        }


        return result;

    }

    private Result validate12ManyNull(SlotAddRequestModel vm) {
        Result result = new Result();

        result = validateNullGeneral(vm);
        if (!result.isValid()) return result;

        result = validateMaxAttendees(vm);
        if (!result.isValid()) return result;

        return result;
    }

    private Result validateMaxAttendees(SlotAddRequestModel vm) {
        Result result = new Result();

        result = validator.validateNull(vm.getMaxAttendees(), "Max attendees");
        if(!result.isValid()) return result;

        return result;
    }

    private Result validateTeacherAndDates(SlotAddRequestModel vm) {
        Result result = new Result();

        result = validateTeacherExists(vm);
        if (!result.isValid()) return result;

        result = validator.validateDateFromToOrder(vm.getDateFrom(), vm.getDateTo());
        if (!result.isValid()) return result;

        result = validator.validateDateBeforeNow(vm.getDateFrom(), "Date From");
        if (!result.isValid()) return result;

        result = validator.validateDateBeforeNow(vm.getDateTo(), "Date To");
        if (!result.isValid()) return result;


        result = validateDateOverlappedWithDbSlots(vm);
        if (!result.isValid()) return result;

        return result;
    }

    public Result validateTeacherExists(SlotAddRequestModel vm) {
        return userValidator.validateUserExists(vm.getTeacherId());
    }



    public Result validate121Null(SlotAddRequestModel vm) {

        Result result = new Result();

        result = validateNullGeneral(vm);
        if (!result.isValid()) return result;

        result = validateMinMinutes(vm);
        if (!result.isValid()) return result;


        return result;
    }

    private Result validateMinMinutes(SlotAddRequestModel vm) {
        Result result = new Result();

        result = validator.validateNull(vm.getMinMinutes(), "Minimal time slot");
        if(!result.isValid()) return result;

        return result;
    }

    private Result validateNullGeneral(SlotAddRequestModel vm) {
        Result result = new Result();

        result = validator.validateNull(vm.getTeacherId(), "User Id");
        if (!result.isValid()) return result;


        result = validator.validateNull(vm.getDateFrom(), "Date From");
        if (!result.isValid()) return result;


        result = validator.validateNull(vm.getDateTo(), "Date To");
        if (!result.isValid()) return result;

        result = validator.validateNull(vm.getPrices(), "Prices set");
        if (!result.isValid()) return result;


        List<AppointmentPrice> prices = vm.getPrices();
        for (AppointmentPrice price : prices) {
            if (price.getPrice() == null || price.getPrice() <= 0) {
                result.add("Prices set can not be null or zero");
                return result;
            }

            if (price.getCurrency() == null || price.getCurrency().length() < 2) {
                result.add("Currency name can't be less than 2 char");
                return result;
            }
        }


        result = validator.validateNull(vm.getBookBeforeMinutes(), "Book before minutes");
        if (!result.isValid()) return result;


        return result;
    }

    public Result validateAppointmentType(SlotAddRequestModel vm) {

        Result result = new Result();

        AppointmentType type = vm.getTimeSlotType();

        if (type == null) {
            result.add("Time slot type can't be null");
            return result;
        }

        if (type != AppointmentType.GROUP && type != AppointmentType.PRIVATE) {
            result.add("Time slot type should be either " + AppointmentType.GROUP + " or " + AppointmentType.PRIVATE);
            return result;
        }

        return result;

    }

    public Result validateDateOverlappedWithDbSlots(SlotAddRequestModel vm) {
        Result result = new Result();

        Date from = DateUtils.removeSeconds(vm.getDateFrom());
        Date to = DateUtils.removeSeconds(vm.getDateTo());

        User user = userR.findById(vm.getTeacherId());

        DateTime curFrom = new DateTime(from);
        DateTime curTo = new DateTime(to);

        List<TimeSlot> dbSlots = slotsRepo.findByTeacher(user);

        for (TimeSlot sl : dbSlots) {
            DateTime dbFrom = new DateTime(sl.getDateFrom());
            DateTime dbTo = new DateTime(sl.getDateTo());

            if (DateUtils.isOverlapped(curFrom, curTo, dbFrom, dbTo)) {
                result.add("Your dates " + curFrom + "-" + curTo + " overlap with db dates " + dbFrom + "-" + dbTo + " for user " + user.getId());
                log.debug("Your dates " + curFrom + "-" + curTo + " overlap with db dates " + dbFrom + "-" + dbTo + " for user " + user.getId());
                return result;
            }
        }

        return result;
    }

    public Result validateDatesBeforeNow(Date from, Date to) {
        Result result = new Result();

        result = validator.validateDateBeforeNow(from, "Date From");
        if(!result.isValid()) return result;

        result = validator.validateDateBeforeNow(to, "Date To");
        if(!result.isValid()) return result;


        return result;
    }

    public Result validateGetSlotsByUserIdWithinDateRange(Date dateFrom, Date dateTo, Long userId) {

        Result result = new Result();

        result = validator.validateNull(dateFrom, "Date From");
        if(!result.isValid()) return result;

        result = validator.validateNull(dateTo, "Date To");
        if(!result.isValid()) return result;

        result = validator.validateNull(userId, "User Id");
        if(!result.isValid()) return result;


        result = validator.validateDateFromToOrder(dateFrom, dateTo);
        if (!result.isValid()) return result;

        result = userValidator.validateUserExists(userId);
        if (!result.isValid()) return result;

        return result;

    }

    public Result validateDateFromToOrder(Date dateFrom, Date dateTo) {
        return validator.validateDateFromToOrder(dateFrom, dateTo);
    }
}
