package com.sorbSoft.CabAcademie.Services;


import com.sorbSoft.CabAcademie.Entities.Feedback;
import com.sorbSoft.CabAcademie.Repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Dany on 18/05/2018.
 */
@Service
@Transactional
public class FeedbackService {
    @Autowired
    private FeedbackRepository feedbackRepository;

    public List<Feedback> fetchAllFeedbackByEnrollementId(Long enrollementId){
        return feedbackRepository.findAllByEnrollement_Id(enrollementId);
    }
    public List<Feedback> fetchAllFeedbackByCourseId(Long courseId){
        return feedbackRepository.findAll();
    }

    public Feedback fetchFeedback(Long id){
        return feedbackRepository.findOne(id);
    }

    public Feedback updateFeedback (Feedback feedback){
        Feedback currentFeedback= feedbackRepository.findOne(feedback.getId());
        currentFeedback.setComment(feedback.getComment());
        currentFeedback.setCourse(feedback.getCourse());
        currentFeedback.setEnrollement(feedback.getEnrollement());
        currentFeedback.setRating(feedback.getRating());

        return feedbackRepository.save(currentFeedback);
    }
    public Feedback saveFeedback(Feedback feedback){
        return feedbackRepository.save(feedback);
    }

    public void deleteFeedback(Long id){
        feedbackRepository.delete(id);
    }
    //other delete methods
    //other fetching methods
}
