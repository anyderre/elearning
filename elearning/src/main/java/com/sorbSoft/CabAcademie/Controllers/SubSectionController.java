package com.sorbSoft.CabAcademie.Controllers;

import com.sorbSoft.CabAcademie.Entities.SubSection;
import com.sorbSoft.CabAcademie.Services.Dtos.Info.SubSectionInfo;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.SubSectionViewModel;
import com.sorbSoft.CabAcademie.Services.SubSectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by Dany on 20/05/2018.
 */
@RestController
@RequestMapping(value = "/api/subSection")
public class SubSectionController {
    @Autowired
    private SubSectionService subSectionService;

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPER_ADMIN')")
    public ResponseEntity<SubSectionViewModel> getSubSection(@PathVariable Long id){
        if(id<0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        SubSectionViewModel vm = subSectionService.getSubSectionViewModel(id);
        if(vm == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(vm, HttpStatus.OK);
    }

    @GetMapping(value = "")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPER_ADMIN')")
    public ResponseEntity<SubSectionViewModel> getSubSectionViewModel(){
        SubSectionViewModel vm = subSectionService.getSubSectionViewModel(null);
        if(vm == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(vm, HttpStatus.OK);
    }

    @GetMapping(value = "/info")
    public ResponseEntity<List<SubSectionInfo>> getAllSubSectionsInfo(){
        List<SubSectionInfo> subSections= subSectionService.getSubSectionInfo();
        if(subSections.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(subSections, HttpStatus.OK);
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<SubSection>> getAllSubSections(){
        List<SubSection> subSections= subSectionService.fetchAllSubSections();
        if(subSections.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(subSections, HttpStatus.OK);
    }

    @GetMapping(value = "/all/filtered")
    public ResponseEntity<List<SubSection>> getAllSubSectionsFiltered(@RequestParam(value = "subSectionId",  required = false) Long subSectionId){
        List <SubSection> subSections = subSectionService.getAllFiltered(subSectionId);
        if(subSections == null)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(subSections, HttpStatus.OK);
    }

    @PostMapping(value = "/save")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPER_ADMIN')")
    public  ResponseEntity<String> saveSubSection(@Valid @RequestBody SubSectionViewModel vm){
        Pair<String, SubSection> result = subSectionService.saveSubSection(vm);
        if(result.getSecond() == null)
            return new ResponseEntity<>(result.getFirst(), HttpStatus.CONFLICT);
        return  new ResponseEntity<>(result.getFirst(), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPER_ADMIN')")
    public ResponseEntity<String> deleteSubSection(@PathVariable Long id ){
        if(id<0)
            return ResponseEntity.badRequest().build();
        if(subSectionService.fetchSubSection(id)==null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(subSectionService.deleteSubSection(id));
    }
}
