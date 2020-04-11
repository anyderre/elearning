package com.sorbSoft.CabAcademie.Repository;


import com.sorbSoft.CabAcademie.Entities.Forum;
import com.sorbSoft.CabAcademie.Entities.Section;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Dany on 15/05/2018.
 */
public interface SectionRepository extends JpaRepository<Section, Long> {
    Section findSectionByNameAndIdIsNot(String name, Long id);
    Section findSectionByName(String name);
}
