package com.sorbSoft.CabAcademie.Services;


import com.sorbSoft.CabAcademie.Entities.HistoryOfLearning;
import com.sorbSoft.CabAcademie.Repository.HistoryOfLearningRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Dany on 18/05/2018.
 */
@Service
@Transactional
public class HistoryOfLearningService {
    @Autowired
    private HistoryOfLearningRepository historyOfLearningRepository;

    public List<HistoryOfLearning> fetchAllHistoryOfLearning(){
        return historyOfLearningRepository.findAll();
    }

    public HistoryOfLearning fetchHistoryOfLearning(Long id){
        return historyOfLearningRepository.findOne(id);
    }

    public HistoryOfLearning updateHistoryOfLearning(HistoryOfLearning historyOfLearning){
        HistoryOfLearning currentHistoryOfLearning= historyOfLearningRepository.findOne(historyOfLearning.getId());
        currentHistoryOfLearning.setEnrollement(historyOfLearning.getEnrollement());
        currentHistoryOfLearning.setTimeStamp(currentHistoryOfLearning.getTimeStamp());
        currentHistoryOfLearning.setVideo(historyOfLearning.getVideo());

        return historyOfLearningRepository.save(currentHistoryOfLearning);
    }

    public HistoryOfLearning saveHitoryOfLearnng (HistoryOfLearning historyOfLearning){
        return historyOfLearningRepository.save(historyOfLearning);
    }

    public void deleteHistoryOfLearning(Long id){
        historyOfLearningRepository.delete(id);
    }
    //other delete methods
    //other fetching methods
}
