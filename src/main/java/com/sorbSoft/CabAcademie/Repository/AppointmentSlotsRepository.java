package com.sorbSoft.CabAcademie.Repository;

import com.sorbSoft.CabAcademie.Entities.User;
import com.sorbSoft.CabAcademie.Entities.AppointmentSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentSlotsRepository extends JpaRepository<AppointmentSlot, Long> {

   // List<AppointmentSlot> findByUser(User user);
}
