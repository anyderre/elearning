package com.sorbSoft.CabAcademie.Services;


import com.sorbSoft.CabAcademie.Entities.Section;
import com.sorbSoft.CabAcademie.Repository.SectionRepository;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Dany on 18/05/2018.
 */
@Service
@Transactional
public class SectionService {
    @Autowired
    private SectionRepository sectionRepository;

    public List<Section> fetchAllSection(){
        return sectionRepository.findAll();
    }

    public Section fetchSection(Long id){
        return sectionRepository.findOne(id);
    }

    public Pair<String, Section> updateSection(Section section){
        Section savedSection = sectionRepository.findSectionByNameAndIdIsNot(section.getName(), section.getId());
        if (savedSection != null) {
            return new Pair<>("The section name already exist for another definition", null);
        }
        Section currentSection= sectionRepository.findOne(section.getId());
        currentSection.setDescription(section.getDescription());
        currentSection.setName(section.getName());

        Section result = sectionRepository.save(currentSection);
        if (result == null) {
            return new Pair<>("Couldn't update the section", null);
        } else {
           return new Pair<>("Section updated successfully", result);
        }
    }
    public Pair<String, Section> saveSection(Section section){
        if (section.getId() > 0L) {
            return updateSection(section);
        } else {
            Section savedSection = sectionRepository.findSectionByName(section.getName());

            if ( savedSection != null){
                return new Pair<>("The section you are trying to save already exist", null);
            }
            Section result = sectionRepository.save(section);
            if (result == null){
                return new Pair<>("Couldn't save the section", null);
            } else {
                return  new Pair<>("Section saved successfully", result);
            }
        }
    }

    public String deleteSection(Long id){
        if (id <= 0L) {
            return "You should indicate the id of the section";
        }
        Section section = fetchSection(id);
        if (section == null) {
            return "The section you want to delete doesn't exist";
        }
        section.setDeleted(true);
        return "Section successfully deleted";
    }

    public List<Section> getAllFiltered(Long sectionId){
        List<Section> sections = fetchAllSection();
        if (sectionId == null)
            return sections;
        if (sectionId <= 0L)
            return new ArrayList<>();
        return sections.isEmpty() ? new ArrayList<>() :  sections.stream().filter(o -> !o.getId().equals(sectionId)).collect(Collectors.toList());
    }

    public Section getSectionViewModel() {
        return new Section() {
            @Override
            public Long getId() {
                return 0L;
            }

            @Override
            public boolean isDeleted() {
                return false;
            }

            @Override
            public String getName() {
                return "";
            }

            @Override
            public String getDescription() {
                return "";
            }
        };
    }
}
