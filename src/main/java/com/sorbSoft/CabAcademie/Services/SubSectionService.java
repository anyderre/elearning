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
        return subSectionRepository.getOne(id);
    }

    public Result updateSubSection (SubSectionViewModel vm){
        Result result = new Result();

        SubSection current = subSectionRepository.getOne(vm.getId());
        if (current == null) {
            result.add("The sub-section you want to update does not exist");
            return result;
        }
        SubSection savedSubSection = subSectionRepository.findSubSectionByNameAndIdIsNot(vm.getName(), vm.getId());

        if (savedSubSection != null) {
            result.add("The subSection name already exist for another definition");
            return result;
        }

        return save(vm);
    }

    public Result saveSubSection (SubSectionViewModel vm){
        vm = prepareEntity(vm);
        Result result = ValidateModel(vm);
        if (!result.isValid()) {
            result.add(result.lista.get(0).getMessage());
            return result;
        }
        if (vm.getId() > 0L) {
            return updateSubSection(vm);
        } else {
            SubSection savedSubSection = subSectionRepository.findSubSectionByName(vm.getName());

            if ( savedSubSection != null){
                result.add("The subSection you are trying to save already exist");
                return result;
            }

            return save(vm);
        }
    }

    private Result save (SubSectionViewModel vm) {
        Result result = new Result();
        try {
            subSectionRepository.save(getEntity(vm));
        } catch (Exception ex)  {
            result.add(ex.getMessage());
            return result;
        }
        return result;
    }

    public Result deleteSubSection(Long id){
        Result result = new Result();
        if (id <= 0L) {
            return result.add("You should indicate the id of the subSection");
        }
        SubSection subSection = subSectionRepository.getOne(id);
        if (subSection == null) {
            return result.add("The subSection you want to delete doesn't exist");
        }

        subSection.setDeleted(true);
        try {
            subSectionRepository.save(subSection);
        } catch (Exception ex)  {
            result.add(ex.getMessage());
            return result;
        }
        return result;
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

        Section section = sectionRepository.getOne(vm.getSection().getId());
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

    public List<SubSection> fetchBySectionId(Long sectionId) {
        return subSectionRepository.findSubSectionBySectionId(sectionId);
    }
}
