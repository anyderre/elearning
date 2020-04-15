package com.sorbSoft.CabAcademie.Controllers;

import com.sorbSoft.CabAcademie.Entities.Error.MessageResponse;
import com.sorbSoft.CabAcademie.Entities.Section;
import com.sorbSoft.CabAcademie.Services.Dtos.Info.SectionInfo;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.SectionViewModel;
import com.sorbSoft.CabAcademie.Services.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
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
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPER_ADMIN')")
    public ResponseEntity<SectionViewModel> getSection(@PathVariable Long id){
        if(id < 0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        SectionViewModel vm = sectionService.getSectionViewModel(id);
        if(vm == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(vm, HttpStatus.OK);
    }

    @GetMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPER_ADMIN')")
    public ResponseEntity<SectionViewModel> getSectionViewModel(){
        SectionViewModel vm = sectionService.getSectionViewModel(null);
        if(vm == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(vm, HttpStatus.OK);
    }

    @GetMapping(value = "/all/filtered")
    public ResponseEntity<List<Section>> getAllFiltered(@RequestParam(value = "sectionId", required = false) Long sectionId){
        List <Section> categories = sectionService.getAllFiltered(sectionId);
        if(categories == null)
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping(value = "/all" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Section>> getAllSection(){
        List<Section> sections= sectionService.fetchAllSection();
        if(sections.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(sections, HttpStatus.OK);
    }

    @GetMapping(value = "/info" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SectionInfo>> getAllSectionInfo(){
        List<SectionInfo> sections= sectionService.getSectionInfo();
        if(sections.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(sections, HttpStatus.OK);
    }

    @PostMapping("/save")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPER_ADMIN')")
    public  ResponseEntity<MessageResponse> saveSection(@Valid @RequestBody SectionViewModel vm){
        Pair<String, Section> result = sectionService.saveSection(vm);
        if(result.getSecond() == null)
            return new ResponseEntity<>(MessageResponse.of(result.getFirst()), HttpStatus.CONFLICT);
        return  new ResponseEntity<>(MessageResponse.of(result.getFirst()), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPER_ADMIN')")
    public ResponseEntity<String> deleteSection(@PathVariable Long id ){
        if(id<0)
            return ResponseEntity.badRequest().build();

        if(sectionService.fetchSection(id)==null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(sectionService.deleteSection(id));
    }
}
