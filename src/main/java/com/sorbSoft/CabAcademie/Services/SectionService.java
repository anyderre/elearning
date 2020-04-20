package com.sorbSoft.CabAcademie.Services;


import com.sorbSoft.CabAcademie.Entities.Section;
import com.sorbSoft.CabAcademie.Repository.SectionRepository;
import com.sorbSoft.CabAcademie.Services.Dtos.Factory.SectionFactory;
import com.sorbSoft.CabAcademie.Services.Dtos.Info.SectionInfo;
import com.sorbSoft.CabAcademie.Services.Dtos.Mapper.SectionMapper;
import com.sorbSoft.CabAcademie.Services.Dtos.Validation.Result;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.SectionViewModel;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
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
        Section current = sectionRepository.findOne(vm.getId());
        if (current == null) {
            return Pair.of("The section you want to update does not exist", null);
        }
        Section savedSection = sectionRepository.findSectionByNameAndIdIsNot(vm.getName(), vm.getId());
        if (savedSection != null) {
            return Pair.of("The section name already exist for another definition", null);
        }
        return save(vm, "update");
    }

    public Pair<String, Section> saveSection(SectionViewModel vm){
        vm = prepareEntity(vm);
        Result result = ValidateModel(vm);
        if (!result.isValid()) {
            return Pair.of(result.lista.get(0).message, null);
        }
        if (vm.getId() > 0L) {
            return updateSection(vm);
        } else {
            Section savedSection = sectionRepository.findSectionByName(vm.getName());

            if (savedSection!= null){
                return Pair.of("The section you are trying to save already exist", null);
            }
            return save(vm, "save");
        }
    }

    private  Pair<String, Section> save (SectionViewModel vm, String action) {
        Section section = null;
        try {
            section = sectionRepository.save(getEntity(vm));
        } catch (Exception ex)  {
            return Pair.of(ex.getMessage(), null);
        }
        if (section == null){
            return Pair.of(String.format("Couldn't {0} the section", action), null);
        } else {
            return  Pair.of(String.format("Section {0}d successfully", action), section);
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

    public List <SectionInfo> getSectionInfo(){
        List<Section> sections = this.fetchAllSection();
        if (sections.isEmpty()) {
            return new ArrayList<>();
        }
        List<SectionInfo> info = new ArrayList<>();
        for (Section section : sections) {
            SectionInfo sInfo = mapper.mapEntityToInfo(section);
            info.add(sInfo);
        }
        return info;
    }

    private Result ValidateModel(SectionViewModel vm){
        Result result = new Result();

        if (vm.getName().isEmpty()) {
            result.add("You should specify the section name");
            return result;
        }

        return result;
    }

    public SectionViewModel prepareEntity(SectionViewModel vm) {
        if (vm.getId()== null)  {
            vm.setId(0L);
        }
        if (vm.getName()== null)  {
            vm.setName("");
        }
        if (vm.getDescription() == null)  {
            vm.setDescription("");
        }
        return vm;
    }

    private Section getEntity(SectionViewModel vm){
        return mapper.mapToEntity(vm);
    }

}
