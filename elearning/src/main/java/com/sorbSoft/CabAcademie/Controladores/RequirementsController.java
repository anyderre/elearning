package com.sorbSoft.CabAcademie.Controladores;

import com.sorbSoft.CabAcademie.Entities.Requirements;
import com.sorbSoft.CabAcademie.Services.RequirementsService;
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
@RequestMapping("/api/requirements")
public class RequirementsController {
    @Autowired
    private RequirementsService requirementsService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<Requirements> getRequirements(@PathVariable Long id){
        if(id<0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Requirements requirements= requirementsService.fetchRequirements(id);
        if(requirements==null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(requirements, HttpStatus.OK);
    }

    @GetMapping("/syllabus/{id}")
    public ResponseEntity<List<Requirements>> getAllRequirements(@PathVariable Long id){
        List<Requirements> requirements= requirementsService.fetchAllRequirementsBySyllabus(id);
        if(requirements.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(requirements, HttpStatus.OK);
    }

    @PostMapping()
    public  ResponseEntity<Requirements> saveRequirements(@Valid @RequestBody Requirements requirements){
        Requirements currentRequirement= requirementsService.saveRequirements(requirements);
        if(currentRequirement==null)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return  new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Requirements> updateRequirement(@PathVariable Long id, @RequestBody Requirements requirements){
        if(id<0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(requirementsService.fetchRequirements(id)==null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Requirements currentRequirement= requirementsService.updateRequirements(requirements);
        if (currentRequirement==null)
            return  new ResponseEntity<>(HttpStatus.NOT_MODIFIED);

        return new ResponseEntity<>(currentRequirement, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteRequirement(@PathVariable Long id ){
        if(id<0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(requirementsService.fetchRequirements(id)==null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        requirementsService.deleteRequirements(id);
        return new ResponseEntity<>("Requirement with "+ id+" Deleted!", HttpStatus.OK);
    }
}
