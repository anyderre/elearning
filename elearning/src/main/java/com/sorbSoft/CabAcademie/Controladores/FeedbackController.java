package com.sorbSoft.CabAcademie.Controladores;


import com.sorbSoft.CabAcademie.Entities.Feedback;
import com.sorbSoft.CabAcademie.Services.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by Dany on 20/05/2018.
 */
@RestController
@RequestMapping(value = "/api/feedback")
public class FeedbackController {
    @Autowired
    private FeedbackService feedbackService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<Feedback> getFeedback(@PathVariable Long id){
        if(id<0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Feedback feedback= feedbackService.fetchFeedback(id);
        if(feedback==null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(feedback, HttpStatus.OK);
    }

    @GetMapping("/enrollement/{id}")
    public ResponseEntity<List<Feedback>> getAllFeedbacksByEnrollementId(@PathVariable Long id){
        List<Feedback> feedbacks= feedbackService.fetchAllFeedbackByEnrollementId(id);
        if(feedbacks.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(feedbacks, HttpStatus.OK);
    }
    @GetMapping("/course/{id}")
    public ResponseEntity<List<Feedback>> getAllFeedbacksByCourse(@PathVariable Long id){
        List<Feedback> feedbacks= feedbackService.fetchAllFeedbackByCourseId(id);
        if(feedbacks.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(feedbacks, HttpStatus.OK);
    }
    @PostMapping()
    public  ResponseEntity<Feedback> saveFeedback(@Valid @RequestBody Feedback feedback){
        Feedback currentFeedbck= feedbackService.saveFeedback(feedback);
        if(currentFeedbck==null)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return  new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Feedback> updateFeedback(@PathVariable Long id, @RequestBody Feedback feedback){
        if(id<0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(feedbackService.fetchFeedback(id)==null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Feedback currentFeedback= feedbackService.updateFeedback(feedback);
        if (currentFeedback==null)
            return  new ResponseEntity<>(HttpStatus.NOT_MODIFIED);

        return new ResponseEntity<>(currentFeedback, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteFeedback (@PathVariable Long id ){
        if(id<0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(feedbackService.fetchFeedback(id)==null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        feedbackService.deleteFeedback(id);
        return new ResponseEntity<>("Feedback Deleted!", HttpStatus.OK);
    }
}
