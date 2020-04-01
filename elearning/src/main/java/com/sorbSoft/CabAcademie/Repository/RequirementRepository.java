package com.sorbSoft.CabAcademie.Repository;

import com.sorbSoft.CabAcademie.Entities.Objective;
import com.sorbSoft.CabAcademie.Entities.Requirement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Dany on 15/05/2018.
 */
public interface RequirementRepository extends JpaRepository<Requirement, Long> {
    List<Requirement> findAllByDescription(String description);
}
