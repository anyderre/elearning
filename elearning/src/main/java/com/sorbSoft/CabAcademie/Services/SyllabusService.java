package com.sorbSoft.CabAcademie.Services;

import com.sorbSoft.CabAcademie.Entities.Syllabus;
import com.sorbSoft.CabAcademie.Repository.SyllabusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Dany on 18/05/2018.
 */
@Service
public class SyllabusService {
    @Autowired
    private SyllabusRepository syllabusRepository;

    public List<Syllabus> fetchAllSyllabus(){
        return syllabusRepository.findAll();
    }

    public Syllabus fetchSyllabus(Long id){
        return syllabusRepository.findOne(id);
    }

    public Syllabus updateSyllabus(Syllabus syllabus){
        Syllabus currentSyllabus= syllabusRepository.findOne(syllabus.getId());
        currentSyllabus.setAbout(syllabus.getAbout());

        return syllabusRepository.save(currentSyllabus);
    }
    public Syllabus saveSyllabus(Syllabus syllabus){
        return syllabusRepository.save(syllabus);
    }
    public void deleteSyllabus(Long id){
        syllabusRepository.delete(id);
    }
    //other delete methods
    //other fetching methods
}
