package com.sorbSoft.CabAcademie.Repository;

import com.sorbSoft.CabAcademie.Entities.Enums.AppointmentType;
import com.sorbSoft.CabAcademie.Entities.Enums.TimeSlotStatus;
import com.sorbSoft.CabAcademie.Entities.TimeSlot;
import com.sorbSoft.CabAcademie.Entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Repository
public interface SlotsRepository extends JpaRepository<TimeSlot, Long> {



    List<TimeSlot> findByTeacher(User user);

    @Query("select ap from TimeSlot ap where ap.dateFrom >= :rangeFrom and ap.dateTo <= :rangeTo and ap.teacher = :teacher")
    List<TimeSlot> findSlotsByUserWithinDateRange(@Param("rangeFrom") Date rangeFrom, @Param("rangeTo") Date rangeTo, @Param("teacher") User teacher);

    @Transactional
    @Modifying
    @Query("delete from TimeSlot ap where ap.dateFrom >= :rangeFrom and ap.dateTo <= :rangeTo and ap.teacher = :teacher")
    void removeSlotsByUserWithDateRange(@Param("rangeFrom") Date rangeFrom, @Param("rangeTo") Date rangeTo, @Param("teacher") User teacher);

    @Transactional
    @Modifying
    @Query("delete from TimeSlot ap where ap.teacher = :teacher")
    void removeAllSlotsForUser(@Param("teacher") User teacher);

    @Query("select ap from TimeSlot ap where ap.dateFrom <= :meetingStart and ap.dateTo >= :meetingEnd and ap.teacher = :teacher and ap.status = 'OPENED'")
    TimeSlot getAvailableSlot(@Param("meetingStart") Date meetingStart, @Param("meetingEnd") Date meetingEnd, @Param("teacher") User teacher);

    TimeSlot findById(Long id);

    @Query("select ap from TimeSlot ap where ap.dateFrom > :now and ap.teacher = :teacher and ((ap.type = 'PRIVATE' and ap.status = 'CLOSED') or ap.type = 'GROUP')")
    Page<TimeSlot> findUpcomingSessionByTeacher(@Param("teacher") User teacher, @Param("now") Date now, Pageable pageable);

    Page<TimeSlot> findAllByTeacherSchoolsInAndDateFromGreaterThanAndAttendeesUserSchoolsIn(User school, Date now, User attendeeSchool, Pageable pageable);

    /*@Query("select ap from TimeSlot ap where ap.dateFrom > :now and :bySchool member of ap.teacher.schools and :bySchool member of ap.attendees.user.schools and ((ap.type = 'PRIVATE' and ap.status = 'CLOSED') or ap.type = 'GROUP')")
    List<TimeSlot> findUpcomingSessionsForAdmin(@Param("bySchool") Collection<User> bySchool, @Param("now") Date now);*/

}
