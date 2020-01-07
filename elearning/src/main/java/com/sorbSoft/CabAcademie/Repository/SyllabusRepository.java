package com.sorbSoft.CabAcademie.Repository;

import com.sorbSoft.CabAcademie.Entities.Syllabus;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Dany on 15/05/2018.
 */
public interface SyllabusRepository extends JpaRepository<Syllabus, Long> {
    Syllabus findSyllabusByTitle(String title);
    Syllabus findSyllabusById(Long id);
}
