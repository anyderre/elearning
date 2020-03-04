package com.sorbSoft.CabAcademie.Controllers;


import com.sorbSoft.CabAcademie.Entities.Enrollement;
import com.sorbSoft.CabAcademie.Services.EnrollementService;
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
@RequestMapping(value = "/api/enrollement")
public class EnrollementController {
    @Autowired
    private EnrollementService enrollementService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<Enrollement> getEnrollement(@PathVariable Long id){
        if(id<0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Enrollement enrollement = enrollementService.fetchEnrollement(id);
        if(enrollement==null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(enrollement, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<Enrollement>> getAllEnrollements(){
        List<Enrollement> enrollements = enrollementService.fetchAllEnrollements();
        if(enrollements.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(enrollements, HttpStatus.OK);
    }

    @PostMapping()
    public  ResponseEntity<Enrollement> saveCourse(@Valid @RequestBody Enrollement enrollement){
        Enrollement currentEnrollement= enrollementService.saveEnrollement(enrollement);
        if(currentEnrollement==null)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return  new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Enrollement> updateEnrollement(@PathVariable Long id, @RequestBody Enrollement enrollement){
        if(id<0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(enrollementService.fetchEnrollement(id)==null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Enrollement currentEnrollement= enrollementService.updateEnrollement(enrollement);
        if (currentEnrollement==null)
            return  new ResponseEntity<>(HttpStatus.NOT_MODIFIED);

        return new ResponseEntity<>(currentEnrollement, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteEnrollement(@PathVariable Long id ){
        if(id<0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(enrollementService.fetchEnrollement(id)==null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        enrollementService.deleteEnrollement(id);
        return new ResponseEntity<>("Course Deleted!", HttpStatus.OK);
    }
}
