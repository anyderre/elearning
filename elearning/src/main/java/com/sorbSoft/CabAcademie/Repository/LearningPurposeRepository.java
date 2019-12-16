package com.sorbSoft.CabAcademie.Repository;

import com.sorbSoft.CabAcademie.Entities.LearningPurpose;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Dany on 15/05/2018.
 */
public interface LearningPurposeRepository extends JpaRepository<LearningPurpose, Long> {
    List<LearningPurpose> findAllBySyllabus_Id(Long id);
}
