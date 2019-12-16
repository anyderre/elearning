package com.sorbSoft.CabAcademie.Services;


import com.sorbSoft.CabAcademie.Entities.LearningPurpose;
import com.sorbSoft.CabAcademie.Repository.LearningPurposeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Dany on 18/05/2018.
 */
@Service
@Transactional
public class LearningPurposeService {
    @Autowired
    private LearningPurposeRepository learningPurposeRepository;

    public List<LearningPurpose> fetchAllLearningPurposeBySyllabusId(Long syllabusId){
        return learningPurposeRepository.findAllBySyllabus_Id(syllabusId);
    }

    public LearningPurpose fetchLearningPurpose(Long id){
        return learningPurposeRepository.findOne(id);
    }

    public LearningPurpose updateLearningPurpose(LearningPurpose learningPurpose){
        LearningPurpose currentLearningPurpose= learningPurposeRepository.findOne(learningPurpose.getId());
        currentLearningPurpose.setDescription(learningPurpose.getDescription());
        currentLearningPurpose.setSyllabus(learningPurpose.getSyllabus());
        return learningPurposeRepository.save(currentLearningPurpose);
    }
    public LearningPurpose saveLearningPurpose(LearningPurpose learningPurpose){
        return learningPurposeRepository.save(learningPurpose);
    }
    public void deleteLearningPurpose(Long id){
        learningPurposeRepository.delete(id);
    }
    //other delete methods
    //other fetching methods
}
