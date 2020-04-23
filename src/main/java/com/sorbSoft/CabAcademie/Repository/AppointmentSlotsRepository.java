package com.sorbSoft.CabAcademie.Repository;

import com.sorbSoft.CabAcademie.Entities.User;
import com.sorbSoft.CabAcademie.Entities.AppointmentSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AppointmentSlotsRepository extends JpaRepository<AppointmentSlot, Long> {

    List<AppointmentSlot> findByUser(User user);


    /*@Query("select ap from AppointmentSlot ap where ap.dateFrom >= :datePoint and ap.user = user ")
    List<AppointmentSlot> findByUserWhichStartsAfter(@Param("user") User user, @Param("datePoint") Date datePoint);
*/
    @Query("select ap from AppointmentSlot ap where ap.dateFrom >= :rangeFrom and ap.dateTo <= :rangeTo and ap.user = :user")
    List<AppointmentSlot> findSlotsByUserWithinDateRange(@Param("rangeFrom") Date rangeFrom, @Param("rangeTo") Date rangeTo, @Param("user") User user);
}
