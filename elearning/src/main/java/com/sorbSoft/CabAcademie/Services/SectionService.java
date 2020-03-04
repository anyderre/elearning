package com.sorbSoft.CabAcademie.Services;


import com.sorbSoft.CabAcademie.Entities.Section;
import com.sorbSoft.CabAcademie.Repository.SectionRepository;
import com.sorbSoft.CabAcademie.Services.Dtos.Factory.SectionFactory;
import com.sorbSoft.CabAcademie.Services.Dtos.Info.SectionInfo;
import com.sorbSoft.CabAcademie.Services.Dtos.Mapper.SectionMapper;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.SectionViewModel;
import javafx.util.Pair;
import org.mapstruct.factory.Mappers;
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

    private SectionMapper mapper
            = Mappers.getMapper(SectionMapper.class);

    @Autowired
    private SectionRepository sectionRepository;

    public List<Section> fetchAllSection(){
        return sectionRepository.findAll();
    }

    public Section fetchSection(Long id){
        return sectionRepository.findOne(id);
    }

    public Pair<String, Section> updateSection(SectionViewModel vm){
        Section savedSection = sectionRepository.findSectionByNameAndIdIsNot(vm.getName(), vm.getId());
        if (savedSection != null) {
            return new Pair<>("The section name already exist for another definition", null);
        }
        Section resultSection = mapper.mapToEntity(vm);
        Section currentSection= sectionRepository.findOne(vm.getId());
        currentSection.setDescription(resultSection.getDescription());
        currentSection.setName(resultSection.getName());

        Section result = sectionRepository.save(currentSection);
        if (result == null) {
            return new Pair<>("Couldn't update the section", null);
        } else {
           return new Pair<>("Section updated successfully", result);
        }
    }
    public Pair<String, Section> saveSection(SectionViewModel vm){
        if (vm.getId() > 0L) {
            return updateSection(vm);
        } else {
            Section savedSection = sectionRepository.findSectionByName(vm.getName());

            if (savedSection!= null){
                return new Pair<>("The section you are trying to save already exist", null);
            }
            Section resultSection = mapper.mapToEntity(vm);
            Section result = sectionRepository.save(resultSection);
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

    public SectionViewModel getSectionViewModel(Long sectionId){
        SectionViewModel vm = SectionFactory.getSectionViewModel();
        if (sectionId != null && sectionId > 0) {
            Section section = this.fetchSection(sectionId);
            if (section == null) {
                return null;
            } else {
                vm = mapper.mapToViewModel(section);
            }
        }
        return vm;
    }

    public List <SectionInfo> getUserInfo(){
        List<Section> categories = this.fetchAllSection();
        if (categories.isEmpty()) {
            return new ArrayList<>();
        }
        List<SectionInfo> info = new ArrayList<>();
        for (Section category : categories) {
            SectionInfo sInfo = new SectionInfo();
            sInfo.setId(category.getId());
            sInfo.setName(category.getName());
            sInfo.setDescription(category.getDescription());
            info.add(sInfo);
        }
        return info;
    }

}
