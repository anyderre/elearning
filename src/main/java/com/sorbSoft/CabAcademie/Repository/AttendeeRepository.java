package com.sorbSoft.CabAcademie.Repository;

import com.sorbSoft.CabAcademie.Entities.Attendee;
import com.sorbSoft.CabAcademie.Entities.TimeSlot;
import com.sorbSoft.CabAcademie.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
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

    Attendee findByApprovalUid(String approvalUid);

    Attendee findByDeclineUid(String declineUid);



}
