package com.sorbSoft.CabAcademie.Repository;

import com.sorbSoft.CabAcademie.Entities.User;
import com.sorbSoft.CabAcademie.Entities.AppointmentSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface AppointmentSlotsRepository extends JpaRepository<AppointmentSlot, Long> {

    List<AppointmentSlot> findByUser(User user);

    @Query("select ap from AppointmentSlot ap where ap.dateFrom >= :rangeFrom and ap.dateTo <= :rangeTo and ap.user = :user")
    List<AppointmentSlot> findSlotsByUserWithinDateRange(@Param("rangeFrom") Date rangeFrom, @Param("rangeTo") Date rangeTo, @Param("user") User user);

    @Transactional
    @Modifying
    @Query("delete from AppointmentSlot ap where ap.dateFrom >= :rangeFrom and ap.dateTo <= :rangeTo and ap.user = :user")
    void removeSlotsByUserWithDateRange(@Param("rangeFrom") Date rangeFrom, @Param("rangeTo") Date rangeTo, @Param("user") User user);

    @Transactional
    @Modifying
    @Query("delete from AppointmentSlot ap where ap.user = :user")
    void removeAllSlotsForUser(@Param("user") User user);

    @Query("select ap from AppointmentSlot ap where ap.dateFrom <= :meetingStart and ap.dateTo >= :meetingEnd and ap.user = :teacher")
    List<AppointmentSlot> getAvailableSlots(@Param("meetingStart") Date meetingStart, @Param("meetingEnd") Date meetingEnd, @Param("teacher") User teacher);
}
