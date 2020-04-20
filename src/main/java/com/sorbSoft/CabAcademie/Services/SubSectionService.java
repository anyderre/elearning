package com.sorbSoft.CabAcademie.Services;

import com.sorbSoft.CabAcademie.Entities.Category;
import com.sorbSoft.CabAcademie.Entities.Section;
import com.sorbSoft.CabAcademie.Entities.SubSection;
import com.sorbSoft.CabAcademie.Repository.SectionRepository;
import com.sorbSoft.CabAcademie.Repository.SubSectionRepository;
import com.sorbSoft.CabAcademie.Services.Dtos.Factory.SubSectionFactory;
import com.sorbSoft.CabAcademie.Services.Dtos.Info.SubSectionInfo;
import com.sorbSoft.CabAcademie.Services.Dtos.Mapper.SubSectionMapper;
import com.sorbSoft.CabAcademie.Services.Dtos.Validation.Result;
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


    public List<SubSection> fetchAllSubSections(){
        return subSectionRepository.findAll();
    }

    public SubSection fetchSubSection(Long id){
        return subSectionRepository.findOne(id);
    }

    public Pair<String, SubSection> updateSubSection (SubSectionViewModel vm){
        SubSection current = subSectionRepository.findOne(vm.getId());
        if (current == null) {
            return Pair.of("The sub-section you want to update does not exist", null);
        }
        SubSection savedSubSection = subSectionRepository.findSubSectionByNameAndIdIsNot(vm.getName(), vm.getId());

        if (savedSubSection != null) {
            return Pair.of("The subSection name already exist for another definition", null);
        }

        return save(vm, "update");
    }

    public Pair<String, SubSection>  saveSubSection (SubSectionViewModel vm){
        vm = prepareEntity(vm);
        Result result = ValidateModel(vm);
        if (!result.isValid()) {
            return Pair.of(result.lista.get(0).message, null);
        }
        if (vm.getId() > 0L) {
            return updateSubSection(vm);
        } else {
            SubSection savedSubSection = subSectionRepository.findSubSectionByName(vm.getName());

            if ( savedSubSection != null){
                return Pair.of("The subSection you are trying to save already exist", null);
            }

            return save(vm, "save");
        }
    }

    private  Pair<String, SubSection> save (SubSectionViewModel vm, String action) {
        SubSection subSection = null;
        try {
            subSection = subSectionRepository.save(getEntity(vm));
        } catch (Exception ex)  {
            return Pair.of(ex.getMessage(), null);
        }
        if (subSection == null){
            return Pair.of(String.format("Couldn't {0} the sub-category", action), null);
        } else {
            return  Pair.of(String.format("Sub-Section {0}d successfully", action), subSection);
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
        for (SubSection sub : subSections) {
            SubSectionInfo sInfo = mapper.mapEntityToInfo(sub);
            info.add(sInfo);
        }
        return info;
    }

    private SubSection getEntity(SubSectionViewModel vm){
        return mapper.mapToEntity(vm);
//        if (vm.getSection()!= null && vm.getSection().getId() != null && vm.getSection().getId() > 0){
//            resultSubSection.setSection(sectionRepository.findOne(vm.getSection().getId()));
//        }
//        return resultSubSection;
    }

    private Result ValidateModel(SubSectionViewModel vm){
        Result result = new Result();

        if (vm.getName().isEmpty()) {
            result.add("You should the name of the sub-section");
            return result;
        }
        if (vm.getSection().getId() <= 0) {
            result.add("You should specify the section for the sub-section");
            return result;
        }

        Section section = sectionRepository.findOne(vm.getSection().getId());
        if (section == null) {
            result.add("The section you specified does not exist");
            return result;
        }
        return result;
    }

    public SubSectionViewModel prepareEntity(SubSectionViewModel vm) {
        if (vm.getId()== null)  {
            vm.setId(0L);
        }
        if (vm.getName()== null)  {
            vm.setName("");
        }
        if (vm.getDescription() == null)  {
            vm.setDescription("");
        }
        if (vm.getSection() == null) {
            vm.setSection( new Section(){
                @Override
                public Long getId() {
                    return 0L;
                }
            });
        }
        return vm;
    }
}
