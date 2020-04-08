package com.sorbSoft.CabAcademie.Services;

import com.sorbSoft.CabAcademie.Entities.SubSection;
import com.sorbSoft.CabAcademie.Repository.SectionRepository;
import com.sorbSoft.CabAcademie.Repository.SubSectionRepository;
import com.sorbSoft.CabAcademie.Services.Dtos.Factory.SubSectionFactory;
import com.sorbSoft.CabAcademie.Services.Dtos.Info.SubSectionInfo;
import com.sorbSoft.CabAcademie.Services.Dtos.Mapper.SubSectionMapper;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.SubSectionViewModel;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Dany on 15/05/2018.
 */
@Service
@Transactional
public class SubSectionService {
    @Autowired
    private SubSectionRepository subSectionRepository;

    @Autowired
    private SectionRepository sectionRepository;

    private SubSectionMapper mapper
            = Mappers.getMapper(SubSectionMapper.class);

    @Autowired
    public List<SubSection> fetchAllSubSections(){
        return subSectionRepository.findAll();
    }

    public SubSection fetchSubSection(Long id){
        return subSectionRepository.findOne(id);
    }

    public Pair<String, SubSection> updateSubSection (SubSectionViewModel vm){
        SubSection savedSubSection = subSectionRepository.findSubSectionByNameAndIdIsNot(vm.getName(), vm.getId());

        if (savedSubSection != null) {
            return Pair.of("The subSection name already exist for another definition", null);
        }

        SubSection currentSubSection= subSectionRepository.findOne(vm.getId());

        if (currentSubSection == null) {
            return Pair.of("The subSection you want to update doesn't exist", null);
        }
        SubSection result = subSectionRepository.save(getEntity(vm));
        if (result == null) {
            return Pair.of("Couldn't update the subSection", null);
        } else {
            return Pair.of("SubSection updated successfully", result);
        }
    }

    public Pair<String, SubSection>  saveSubSection (SubSectionViewModel vm){
        if (vm.getId() > 0L) {
            return updateSubSection(vm);
        } else {
            SubSection savedSubSection = subSectionRepository.findSubSectionByName(vm.getName());

            if ( savedSubSection != null){
                return Pair.of("The subSection you are trying to save already exist", null);
            }

            SubSection result = subSectionRepository.save(getEntity(vm));
            if (result == null){
                return Pair.of("Couldn't save the subSection", null);
            } else {
                return  Pair.of("SubSection saved successfully", result);
            }
        }
    }

    public String deleteSubSection(Long id){
        if (id <= 0L) {
            return "You should indicate the id of the subSection";
        }
        SubSection subSection = subSectionRepository.findOne(id);
        if (subSection == null) {
            return "The subSection you want to delete doesn't exist";
        }

        subSection.setDeleted(true);
        subSectionRepository.save(subSection);
        return "Section successfully deleted";
    }

    public SubSectionViewModel getSubSectionViewModel(Long subSectionId){
        SubSectionViewModel vm = SubSectionFactory.getSubSectionViewModel();
        if (subSectionId != null && subSectionId > 0) {
            SubSection subSection = this.fetchSubSection(subSectionId);
            if (subSection == null) {
                return null;
            } else {
                vm = mapper.mapToViewModel(subSection);
            }
        }
        vm.setAllSections(sectionRepository.findAll());
        return vm;
    }

    public List<SubSection> getAllFiltered(Long subSectionId){
        List<SubSection> subSections = fetchAllSubSections();
        if (subSectionId == null)
            return subSections;
        List<SubSection> subSectionsFiltered = subSections.isEmpty() ? null :
                subSections.stream().filter(o -> !o.getId().equals(subSectionId)).collect(Collectors.toList());
        if (subSectionsFiltered == null) {
            return new ArrayList<>();
        }
        return subSectionsFiltered;
    }

    public List <SubSectionInfo> getSubSectionInfo(){
        List<SubSection> subSections = this.fetchAllSubSections();
        if (subSections.isEmpty()) {
            return new ArrayList<>();
        }
        List<SubSectionInfo> info = new ArrayList<>();
        for (SubSection subSection : subSections) {
            SubSectionInfo cInfo = new SubSectionInfo();
            cInfo.setId(subSection.getId());
            cInfo.setName(subSection.getName());
            cInfo.setDescription(subSection.getDescription());
            cInfo.setSectionName(subSection.getSection().getName());
            info.add(cInfo);
        }
        return info;
    }

    private SubSection getEntity(SubSectionViewModel vm){
        SubSection resultSubSection = mapper.mapToEntity(vm);
        if (vm.getSection()!= null && vm.getSection().getId() != null && vm.getSection().getId() > 0){
            resultSubSection.setSection(sectionRepository.findOne(vm.getSection().getId()));
        }
        return resultSubSection;
    }
}
