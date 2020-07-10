package com.sorbSoft.CabAcademie.Repository;

import com.sorbSoft.CabAcademie.Entities.Attendee;
import com.sorbSoft.CabAcademie.Entities.Enums.AttendeeStatus;
import com.sorbSoft.CabAcademie.Entities.TimeSlot;
import com.sorbSoft.CabAcademie.Entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface AttendeeRepository extends JpaRepository<Attendee, Long> {

    List<Attendee> findByUserAndTimeSlot(User user, TimeSlot timeSlot);

    @Query("select at.timeSlot from Attendee at where at.timeSlot.dateFrom > :now and at.user = :student and at.status <> 'REJECTED' and at.status <> 'CANCELED'")
    Page<TimeSlot> findStudentUpcomingSessions(@Param("student") User student, @Param("now") Date now, Pageable pageable);

    @Query("select count(at) from Attendee at where at.timeSlot.dateFrom > :now and at.timeSlot.teacher = :teacher and at.status = 'PENDING_APPROVAL'")
    long findPendingStudentByTeacher(@Param("teacher") User teacher, @Param("now") Date now);

    Attendee findByApprovalUid(String approvalUid);

    Attendee findByDeclineUid(String declineUid);



}
