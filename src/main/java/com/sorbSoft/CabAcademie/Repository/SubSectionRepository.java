package com.sorbSoft.CabAcademie.Repository;

import com.sorbSoft.CabAcademie.Entities.SubSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by Dany on 15/05/2018.
 */
public interface SubSectionRepository extends JpaRepository<SubSection, Long> {
    SubSection findSubSectionByNameAndIdIsNot(String title, Long id);
    SubSection findSubSectionByName(String title);

    @Query("select ss from SubSection ss where ss.section.id = :sectionId")
    List<SubSection> findSubSectionBySectionId(@Param("sectionId") Long sectionId);
}
