package com.sorbSoft.CabAcademie.Services;


import com.sorbSoft.CabAcademie.Entities.Requirement;
import com.sorbSoft.CabAcademie.Repository.RequirementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Dany on 18/05/2018.
 */
@Service
public class RequirementService {
    @Autowired
    private RequirementRepository requirementRepository;

    public List<Requirement> fetchAllRequirement(){
        return requirementRepository.findAll();
    }

    public Requirement saveRequirement(Requirement requirement){
        return requirementRepository.save(requirement);
    }
    public void deleteRequirement(Long id){
        requirementRepository.deleteById(id);
    }
}
