package com.sorbSoft.CabAcademie.Controladores;

import com.sorbSoft.CabAcademie.Entities.Section;
import com.sorbSoft.CabAcademie.Services.SectionService;
import javafx.application.Application;
import javafx.util.Pair;
import jdk.nashorn.internal.ir.RuntimeNode;
import org.omg.CORBA.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by Dany on 20/05/2018.
 */
@RestController
@RequestMapping(value = "/api/section")
public class SectionController {
    @Autowired
    private SectionService sectionService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<Section> getSection(@PathVariable Long id){
        if(id<0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Section section= sectionService.fetchSection(id);
        if(section==null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(section, HttpStatus.OK);
    }

    @GetMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Section> getSectionViewModel(){
        Section section= new Section();
        section.setName("");
        section.setDescription("");
        section.setId(0L);
        return new ResponseEntity<>(section, HttpStatus.OK);
    }

    @GetMapping(value = "/all" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Section>> getAllSection(){
        List<Section> sections= sectionService.fetchAllSection();
        if(sections.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(sections, HttpStatus.OK);
    }

    @PostMapping("/save")
    public  ResponseEntity<String> saveSection(@Valid @RequestBody Section section){
        Pair<String, Section> result = sectionService.saveSection(section);
        if(result.getValue() == null)
            return new ResponseEntity<>(result.getKey(), HttpStatus.CONFLICT);
        return  new ResponseEntity<>(result.getKey(), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<String> deleteSection(@PathVariable Long id ){
        if(id<0)
            return ResponseEntity.badRequest().build();

        if(sectionService.fetchSection(id)==null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(sectionService.deleteSection(id));
    }
}
