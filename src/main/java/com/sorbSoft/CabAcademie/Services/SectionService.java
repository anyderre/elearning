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
        return sectionRepository.getOne(id);
    }

    public Result updateSection(SectionViewModel vm){
        Result result = new Result();
        Section current = sectionRepository.getOne(vm.getId());
        if (current == null) {
            result.add("The section you want to update does not exist");
            return result;
        }
        Section savedSection = sectionRepository.findSectionByNameAndIdIsNot(vm.getName(), vm.getId());
        if (savedSection != null) {
            result.add("The section name already exist for another definition");
            return result;
        }
        return save(vm);
    }

    public Result saveSection(SectionViewModel vm){
        vm = prepareEntity(vm);
        Result result = ValidateModel(vm);
        if (!result.isValid()) {
            return result;
        }
        if (vm.getId() > 0L) {
            return updateSection(vm);
        } else {
            Section savedSection = sectionRepository.findSectionByName(vm.getName());

            if (savedSection!= null){
                result.add("The section you are trying to save already exist");
                return result;
            }
            return save(vm);
        }
    }

    private  Result save (SectionViewModel vm) {
        Result result = new Result();
        try {
            sectionRepository.save(getEntity(vm));
        } catch (Exception ex)  {
            return result.add(ex.getMessage());
        }
        return  result;
    }
    public Result deleteSection(Long id){
        Result result = new Result();
        if (id <= 0L) {
            result.add("You should indicate the id of the section");
            return result;
        }
        Section section = fetchSection(id);
        if (section == null) {
            result.add("The section you want to delete doesn't exist");
            return result;
        }
        section.setDeleted(true);
        try {
            sectionRepository.save(section);
        } catch (Exception ex)  {
            return result.add(ex.getMessage());
        }
        return result;
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
