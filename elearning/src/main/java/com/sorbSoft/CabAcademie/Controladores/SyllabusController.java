package com.sorbSoft.CabAcademie.Controladores;


import com.sorbSoft.CabAcademie.Entities.Syllabus;
import com.sorbSoft.CabAcademie.Services.SyllabusService;
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

    @GetMapping()
    public ResponseEntity<List<Syllabus>> getAllSyllabus(){
        List<Syllabus> syllabusList= syllabusService.fetchAllSyllabus();
        if(syllabusList.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(syllabusList, HttpStatus.OK);
    }

    @PostMapping()
    public  ResponseEntity<Syllabus> saveSyllabus(@Valid @RequestBody Syllabus syllabus){
        Syllabus currentSyllabus= syllabusService.saveSyllabus(syllabus);
        if(currentSyllabus==null)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return  new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Syllabus> updateSyllabus(@PathVariable Long id, @RequestBody Syllabus syllabus){
        if(id<0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(syllabusService.fetchSyllabus(id)==null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Syllabus currentSyllabus= syllabusService.updateSyllabus(syllabus);
        if (currentSyllabus==null)
            return  new ResponseEntity<>(HttpStatus.NOT_MODIFIED);

        return new ResponseEntity<>(currentSyllabus, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteSyllabus(@PathVariable Long id ){
        if(id<0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(syllabusService.fetchSyllabus(id)==null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        syllabusService.deleteSyllabus(id);
        return new ResponseEntity<>("Syllabus with "+ id+" Deleted!", HttpStatus.OK);
    }
}
