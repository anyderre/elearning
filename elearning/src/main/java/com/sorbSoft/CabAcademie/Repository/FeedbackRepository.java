package com.sorbSoft.CabAcademie.Repository;

import com.sorbSoft.CabAcademie.Entities.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Dany on 15/05/2018.
 */
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findAllByCourse_Id(Long id);
    List<Feedback> findAllByEnrollement_Id(Long id);
}
