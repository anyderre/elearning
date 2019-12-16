package com.sorbSoft.CabAcademie.Services;


import com.sorbSoft.CabAcademie.Entities.Requirements;
import com.sorbSoft.CabAcademie.Repository.RequirementsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Dany on 18/05/2018.
 */
@Service
@Transactional
public class RequirementsService {
    @Autowired
    private RequirementsRepository requirementsRepository;

    public List<Requirements> fetchAllRequirementsBySyllabus(Long syllabusId){
        return requirementsRepository.findAllBySyllabus_Id(syllabusId);
    }

    public Requirements fetchRequirements(Long id){
        return requirementsRepository.findOne(id);
    }

    public Requirements updateRequirements(Requirements requirements){
        Requirements currentRequirements= requirementsRepository.findOne(requirements.getId());
        currentRequirements.setSyllabus(requirements.getSyllabus());

        return requirementsRepository.save(currentRequirements);
    }
    public Requirements saveRequirements(Requirements requirements){
        return requirementsRepository.save(requirements);
    }
    public void deleteRequirements(Long id){
        requirementsRepository.delete(id);
    }
    //other delete methods
    //other fetching methods
}
