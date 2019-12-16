package com.sorbSoft.CabAcademie.Controladores;

import com.sorbSoft.CabAcademie.Entities.LearningPurpose;
import com.sorbSoft.CabAcademie.Services.LearningPurposeService;
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
@RequestMapping("/api/learningPurpose")
public class LearningPurposeController {
    @Autowired
    private LearningPurposeService learningPurposeService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<LearningPurpose> getLearningPurpose(@PathVariable Long id){
        if(id<0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        LearningPurpose learningPurpose= learningPurposeService.fetchLearningPurpose(id);
        if(learningPurpose==null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(learningPurpose, HttpStatus.OK);
    }

    @GetMapping("/syllabus/{id}")
    public ResponseEntity<List<LearningPurpose>> getAllLearnigPurpose(@PathVariable Long id){
        List<LearningPurpose> learningPurposes= learningPurposeService.fetchAllLearningPurposeBySyllabusId(id);
        if(learningPurposes.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(learningPurposes, HttpStatus.OK);
    }

    @PostMapping()
    public  ResponseEntity<LearningPurpose> saveLearningPurpose(@Valid @RequestBody LearningPurpose learningPurpose){
        LearningPurpose currentLearningPurpose= learningPurposeService.saveLearningPurpose(learningPurpose);
        if(currentLearningPurpose==null)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return  new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<LearningPurpose> updateLearningPurpose(@PathVariable Long id, @RequestBody LearningPurpose learningPurpose){
        if(id<0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(learningPurposeService.fetchLearningPurpose(id)==null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        LearningPurpose currentLearningPurpose= learningPurposeService.updateLearningPurpose(learningPurpose);
        if (currentLearningPurpose==null)
            return  new ResponseEntity<>(HttpStatus.NOT_MODIFIED);

        return new ResponseEntity<>(currentLearningPurpose, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteLearningPurpose(@PathVariable Long id ){
        if(id<0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(learningPurposeService.fetchLearningPurpose(id)==null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        learningPurposeService.deleteLearningPurpose(id);
        return new ResponseEntity<>("Learning purpose Deleted!", HttpStatus.OK);
    }
}
