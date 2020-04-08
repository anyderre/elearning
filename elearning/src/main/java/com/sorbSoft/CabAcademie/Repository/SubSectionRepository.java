package com.sorbSoft.CabAcademie.Repository;

import com.sorbSoft.CabAcademie.Entities.SubSection;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Dany on 15/05/2018.
 */
public interface SubSectionRepository extends JpaRepository<SubSection, Long> {
    SubSection findSubSectionByNameAndIdIsNot(String title, Long id);
    SubSection findSubSectionByName(String title);
}
