package com.sorbSoft.CabAcademie.Controladores;


import com.sorbSoft.CabAcademie.Entities.HistoryOfLearning;
import com.sorbSoft.CabAcademie.Services.HistoryOfLearningService;
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
@RequestMapping("/api/historyOfLearning")
public class HistoryOfLearnings {
    @Autowired
    private HistoryOfLearningService historyOfLearningService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<HistoryOfLearning> getHistoryOfLearning(@PathVariable Long id){
        if(id<0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        HistoryOfLearning historyOfLearning= historyOfLearningService.fetchHistoryOfLearning(id);
        if(historyOfLearning==null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(historyOfLearning, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<HistoryOfLearning>> getAllHistoryOfLearnings(){
        List<HistoryOfLearning> historyOfLearnings= historyOfLearningService.fetchAllHistoryOfLearning();
        if(historyOfLearnings.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(historyOfLearnings, HttpStatus.OK);
    }

    @PostMapping()
    public  ResponseEntity<HistoryOfLearning> saveHistoryOfLEarning(@Valid @RequestBody HistoryOfLearning historyOfLearning){
        HistoryOfLearning currentHistoryOfLearning = historyOfLearningService.saveHitoryOfLearnng(historyOfLearning);
        if(currentHistoryOfLearning==null)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return  new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<HistoryOfLearning> updateHistoryOfLearning(@PathVariable Long id, @RequestBody HistoryOfLearning historyOfLearning){
        if(id<0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(historyOfLearningService.fetchHistoryOfLearning(id)==null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        HistoryOfLearning currentHistoryOfLearning= historyOfLearningService.updateHistoryOfLearning(historyOfLearning);
        if (currentHistoryOfLearning==null)
            return  new ResponseEntity<>(HttpStatus.NOT_MODIFIED);

        return new ResponseEntity<>(currentHistoryOfLearning, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteCourse(@PathVariable Long id ){
        if(id<0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(historyOfLearningService.fetchHistoryOfLearning(id)==null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        historyOfLearningService.deleteHistoryOfLearning(id);
        return new ResponseEntity<>("Course Deleted!", HttpStatus.OK);
    }
}
