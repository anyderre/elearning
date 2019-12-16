package com.sorbSoft.CabAcademie.Services;


import com.sorbSoft.CabAcademie.Entities.Enrollement;
import com.sorbSoft.CabAcademie.Repository.EnrollementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Dany on 18/05/2018.
 */
@Service
@Transactional
public class EnrollementService {
    @Autowired
    private EnrollementRepository enrollementRepository;

    public List<Enrollement> fetchAllEnrollements(){
        return enrollementRepository.findAll();
    }

    public Enrollement fetchEnrollement(Long id){
        return enrollementRepository.findOne(id);
    }

    public Enrollement updateEnrollement (Enrollement enrollement){
        Enrollement currentEnrollement= enrollementRepository.findOne(enrollement.getId());
        currentEnrollement.setAmount(enrollement.getAmount());
        currentEnrollement.setCourse(enrollement.getCourse());
        currentEnrollement.setUser(enrollement.getUser());

        return enrollementRepository.save(currentEnrollement);
    }
    public Enrollement saveEnrollement(Enrollement enrollement){
        return enrollementRepository.save(enrollement);
    }
    public void deleteEnrollement(Long id){
        enrollementRepository.delete(id);
    }
    //other delete methods
    //other fetching methods
}
