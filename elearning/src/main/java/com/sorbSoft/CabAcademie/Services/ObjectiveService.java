package com.sorbSoft.CabAcademie.Services;


import com.sorbSoft.CabAcademie.Entities.Objective;
import com.sorbSoft.CabAcademie.Repository.ObjectiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Dany on 18/05/2018.
 */
@Service
public class ObjectiveService {
    @Autowired
    private ObjectiveRepository objectiveRepository;

    public List<Objective> fetchAllObjective(){
        return objectiveRepository.findAll();
    }

    public Objective saveObjective(Objective objective){
        return objectiveRepository.save(objective);
    }
    public void deleteObjective(Long id){
        objectiveRepository.delete(id);
    }
}
