package com.sorbSoft.CabAcademie.Services;

import com.sorbSoft.CabAcademie.Entities.Syllabus;
import com.sorbSoft.CabAcademie.Repository.SyllabusRepository;
import org.springframework.data.util.Pair;
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
        return syllabusRepository.getOne(id);
    }

    public Pair <String, Syllabus> updateSyllabus(Syllabus syllabus){
        Syllabus savedSyllabus = syllabusRepository.findSyllabusById(syllabus.getId());

        if (savedSyllabus != null) {
            return Pair.of("The syllabus name already exist for another definition", null);
        }

        Syllabus currentSyllabus= syllabusRepository.getOne(syllabus.getId());

        if (currentSyllabus == null) {
            return Pair.of("The syllabus you want to update doesn't exist", null);
        }
        currentSyllabus.setChapterTitle(syllabus.getChapterTitle());
        currentSyllabus.setChapterTuts(syllabus.getChapterTuts());

        Syllabus result = syllabusRepository.save(syllabus);
        if (result == null) {
            return Pair.of("Couldn't update the syllabus", null);
        } else {
            return Pair.of("Syllabus updated successfully", result);
        }
    }
    
    public Pair<String, Syllabus> saveSyllabus(Syllabus syllabus){
        if (syllabus.getId() > 0L) {
            return updateSyllabus(syllabus);
        } else {
            Syllabus savedSyllabus = syllabusRepository.findSyllabusByChapterTitle(syllabus.getChapterTitle());

            if ( savedSyllabus != null){
                return Pair.of("The syllabus you are trying to save already exist", null);
            }
            Syllabus result = syllabusRepository.save(syllabus);
            if (result == null){
                return Pair.of("Couldn't save the syllabus", null);
            } else {
                return  Pair.of("Syllabus saved successfully", result);
            }
        }
    }
    public String deleteSyllabus(Long id){
        if (id <= 0L) {
            return "You should indicate the id of the syllabus";
        }
        Syllabus syllabus = syllabusRepository.getOne(id);
        if (syllabus == null) {
            return "The syllabus you want to delete doesn't exist";
        }

        syllabus.setDeleted(true);
        syllabusRepository.save(syllabus);
        return "Section successfully deleted";
        
    }
}
