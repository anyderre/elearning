package com.sorbSoft.CabAcademie.Controllers;


import com.sorbSoft.CabAcademie.Entities.Syllabus;
import com.sorbSoft.CabAcademie.Services.SyllabusService;
import org.springframework.data.util.Pair;
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
@RequestMapping("/api/syllabus")
public class SyllabusController {
    @Autowired
    private SyllabusService syllabusService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<Syllabus> getSyllabus(@PathVariable Long id){
        if(id<0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Syllabus currentSyllabus= syllabusService.fetchSyllabus(id);
        if(currentSyllabus==null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(currentSyllabus, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Syllabus>> getAllSyllabus(){
        List<Syllabus> syllabusList= syllabusService.fetchAllSyllabus();
        if(syllabusList.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(syllabusList, HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<Syllabus> getAllSyllabusViewModel(){
        return new ResponseEntity<>(syllabusService.getSyllabusViewModel(), HttpStatus.OK);
    }

    @PostMapping("/save")
    public  ResponseEntity<String> saveSyllabus(@Valid @RequestBody Syllabus syllabus){
        Pair<String, Syllabus> result = syllabusService.saveSyllabus(syllabus);
        if(result.getValue() == null)
            return new ResponseEntity<>(result.getKey(), HttpStatus.CONFLICT);
        return  new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<String> deleteSyllabus(@PathVariable Long id ){

        if(id<0)
            return ResponseEntity.badRequest().build();
        if(syllabusService.fetchSyllabus(id)==null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(syllabusService.deleteSyllabus(id));
    }
}
