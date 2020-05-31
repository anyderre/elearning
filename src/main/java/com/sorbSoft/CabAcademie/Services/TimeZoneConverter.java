package com.sorbSoft.CabAcademie.Services;

import com.sorbSoft.CabAcademie.Entities.AppointmentPrice;
import com.sorbSoft.CabAcademie.Entities.Enums.AppointmentType;
import com.sorbSoft.CabAcademie.Entities.TimeSlot;
import com.sorbSoft.CabAcademie.Entities.User;
import com.sorbSoft.CabAcademie.Repository.SlotsRepository;
import com.sorbSoft.CabAcademie.Repository.UserRepository;
import com.sorbSoft.CabAcademie.Services.Dtos.Validation.Result;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.appointment.*;
import com.sorbSoft.CabAcademie.Utils.DateUtils;
import lombok.extern.log4j.Log4j2;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Log4j2
public class TimeZoneConverter {

    @Autowired
    private UserRepository userR;

    public SlotAddRequestModel convertFromTimeZonedToUtc(SlotAddRequestModel vmTimeZoned) {

        Date from = vmTimeZoned.getDateFrom();
        Date to = vmTimeZoned.getDateTo();

        log.debug("SlotAddRequestModel time zone date from: "+from);
        log.debug("SlotAddRequestModel time zone date to: "+to);

        convertFromTimeZonedToUtc(vmTimeZoned.getTeacherId(), from, to);

        log.debug("UTC time zone of SlotAddRequestModel date from: "+from);
        log.debug("UTC time zone of SlotAddRequestModel date to: "+to);

        return vmTimeZoned;
    }

    public SlotsGetRequestModel convertFromTimeZonedToUtc(SlotsGetRequestModel vmTimeZoned) {
        Date from = vmTimeZoned.getDateFrom();
        Date to = vmTimeZoned.getDateTo();

        log.debug("SlotsGetRequestModel time zone date from: "+from);
        log.debug("SlotsGetRequestModel time zone date to: "+to);

        convertFromTimeZonedToUtc(vmTimeZoned.getTeacherId(), from, to);

        log.debug("UTC time zone of SlotsGetRequestModel date from: "+from);
        log.debug("UTC time zone of SlotsGetRequestModel date to: "+to);

        return vmTimeZoned;
    }

    public void convertFromTimeZonedToUtc(Long userId, Date from, Date to) {
        User user = userR.getOne(userId);
        String userTZ = user.getTimeZone();
        from = DateUtils.fromTimeZonedToUtc(from, userTZ);
        to = DateUtils.fromTimeZonedToUtc(to, userTZ);
    }

    public void convertFromUtcToTimeZoned(Long userId, Date from, Date to) {
        User user = userR.getOne(userId);
        String userTZ = user.getTimeZone();
        convertFromUtcToTimeZoned(userTZ, from, to);

        //from = DateUtils.fromUtcToTimeZoned(from, userTZ);
        //to = DateUtils.fromUtcToTimeZoned(to, userTZ);
    }

    public void convertFromUtcToTimeZoned(String userTZ, Date from, Date to) {
        from = DateUtils.fromUtcToTimeZoned(from, userTZ);
        to = DateUtils.fromUtcToTimeZoned(to, userTZ);
    }

    public void convertFromUtcToTimeZoned(SlotsResponseModel responseModelUtc, Long requesterId) {

        Date from = responseModelUtc.getDateFrom();
        Date to = responseModelUtc.getDateTo();

        log.debug("UTC time zone of SlotsResponseModel date from: "+from);
        log.debug("UTC time zone of SlotsResponseModel date to: "+to);

        convertFromUtcToTimeZoned(requesterId, from, to);

        log.debug("SlotsResponseModel time zone date from: "+from);
        log.debug("SlotsResponseModel time zone date to: "+to);

    }

    public SlotDeleteRequestModel convertFromTimeZonedToUtc(SlotDeleteRequestModel vmTimeZoned) {
        Date from = vmTimeZoned.getDateFrom();
        Date to = vmTimeZoned.getDateTo();

        log.debug("SlotDeleteRequestModel time zone date from: "+from);
        log.debug("SlotDeleteRequestModel time zone date to: "+to);

        convertFromTimeZonedToUtc(vmTimeZoned.getRequesterId(), from, to);

        log.debug("UTC time zone of SlotDeleteRequestModel date from: "+from);
        log.debug("UTC time zone of SlotDeleteRequestModel date to: "+to);

        return vmTimeZoned;
    }

    public OneToOneAppointmentMakeRequestModel convertFromTimeZonedToUtc(OneToOneAppointmentMakeRequestModel vmTimeZoned) {
        Date from = vmTimeZoned.getDateFrom();
        Date to = vmTimeZoned.getDateTo();

        log.debug("OneToOneAppointmentMakeRequestModel time zone date from: "+from);
        log.debug("OneToOneAppointmentMakeRequestModel time zone date to: "+to);

        convertFromTimeZonedToUtc(vmTimeZoned.getStudentId(), from, to);

        log.debug("UTC time zone of OneToOneAppointmentMakeRequestModel date from: "+from);
        log.debug("UTC time zone of OneToOneAppointmentMakeRequestModel date to: "+to);

        return vmTimeZoned;
    }
}
