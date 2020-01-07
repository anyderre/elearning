package com.sorbSoft.CabAcademie.Services;

import com.sorbSoft.CabAcademie.Entities.Syllabus;
import com.sorbSoft.CabAcademie.Repository.SyllabusRepository;
import javafx.util.Pair;
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

    public Pair <String, Syllabus> updateSyllabus(Syllabus syllabus){
        Syllabus savedSyllabus = syllabusRepository.findSyllabusById(syllabus.getId());

        if (savedSyllabus != null) {
            return new Pair<>("The syllabus name already exist for another definition", null);
        }

        Syllabus currentSyllabus= syllabusRepository.findOne(syllabus.getId());

        if (currentSyllabus == null) {
            return new Pair<>("The syllabus you want to update doesn't exist", null);
        }
        currentSyllabus.setDescription(syllabus.getDescription());
        currentSyllabus.setTitle(syllabus.getTitle());
        currentSyllabus.setDescription(syllabus.getDescription());

        Syllabus result = syllabusRepository.save(syllabus);
        if (result == null) {
            return new Pair<>("Couldn't update the syllabus", null);
        } else {
            return new Pair<>("Syllabus updated successfully", result);
        }
    }
    
    
    public Syllabus getSyllabusViewModel(){
        return new Syllabus(){
            @Override
            public Long getId() {
                return 0L;
            }

            @Override
            public String getTitle() {
                return "";
            }

            @Override
            public String getDescription() {
                return "";
            }
        };
    }
    
    public Pair<String, Syllabus> saveSyllabus(Syllabus syllabus){
        if (syllabus.getId() > 0L) {
            return updateSyllabus(syllabus);
        } else {
            Syllabus savedSyllabus = syllabusRepository.findSyllabusByTitle(syllabus.getTitle());

            if ( savedSyllabus != null){
                return new Pair<>("The syllabus you are trying to save already exist", null);
            }
            Syllabus result = syllabusRepository.save(syllabus);
            if (result == null){
                return new Pair<>("Couldn't save the syllabus", null);
            } else {
                return  new Pair<>("Syllabus saved successfully", result);
            }
        }
    }
    public String deleteSyllabus(Long id){
        if (id <= 0L) {
            return "You should indicate the id of the syllabus";
        }
        Syllabus syllabus = syllabusRepository.findOne(id);
        if (syllabus == null) {
            return "The syllabus you want to delete doesn't exist";
        }

        syllabus.setDeleted(true);
        syllabusRepository.save(syllabus);
        return "Section successfully deleted";
        
    }
}
