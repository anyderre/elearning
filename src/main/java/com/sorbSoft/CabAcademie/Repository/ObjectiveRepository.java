package com.sorbSoft.CabAcademie.Repository;

import com.sorbSoft.CabAcademie.Entities.Objective;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Dany on 15/05/2018.
 */
public interface ObjectiveRepository extends JpaRepository<Objective, Long> {
    List<Objective> findAllByDescription(String description);
}
