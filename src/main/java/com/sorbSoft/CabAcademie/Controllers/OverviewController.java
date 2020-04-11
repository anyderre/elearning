package com.sorbSoft.CabAcademie.Controllers;

import com.sorbSoft.CabAcademie.Entities.Overview;
import com.sorbSoft.CabAcademie.Services.OverviewService;
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
@RequestMapping("/api/overview")
public class OverviewController {
    @Autowired
    private OverviewService overviewService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<Overview> getOverview(@PathVariable Long id){
        if(id<0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Overview overview = overviewService.fetchOverview(id);
        if(overview ==null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(overview, HttpStatus.OK);
    }

//    @GetMapping("/syllabus/{id}")
//    public ResponseEntity<List<Overview>> getAllOverview(@PathVariable Long id){
//        List<Overview> requirements= overviewService.fetchAllOverviewBySyllabus(id);
//        if(requirements.isEmpty())
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        return new ResponseEntity<>(requirements, HttpStatus.OK);
//    }

    @PostMapping()
    public  ResponseEntity<Overview> saveOverview(@Valid @RequestBody Overview overview){
        Overview currentRequirement= overviewService.saveOverview(overview);
        if(currentRequirement==null)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return  new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Overview> updateRequirement(@PathVariable Long id, @RequestBody Overview overview){
        if(id<0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(overviewService.fetchOverview(id)==null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Overview currentRequirement= overviewService.updateOverview(overview);
        if (currentRequirement==null)
            return  new ResponseEntity<>(HttpStatus.NOT_MODIFIED);

        return new ResponseEntity<>(currentRequirement, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteRequirement(@PathVariable Long id ){
        if(id<0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(overviewService.fetchOverview(id)==null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        overviewService.deleteOverview(id);
        return new ResponseEntity<>("Requirement with "+ id+" Deleted!", HttpStatus.OK);
    }
}
