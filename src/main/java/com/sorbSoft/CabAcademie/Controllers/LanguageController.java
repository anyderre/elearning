package com.sorbSoft.CabAcademie.Controllers;


import com.sorbSoft.CabAcademie.Entities.Error.MessageResponse;
import com.sorbSoft.CabAcademie.Entities.LanguageEntity;
import com.sorbSoft.CabAcademie.Services.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api/languages")
public class LanguageController {

    @Autowired
    private LanguageService langService;

    @GetMapping(value = "/{locale}")
    public ResponseEntity<List<LanguageEntity>> getLanguagesByLocale(@PathVariable String locale) {

        if(locale.isEmpty())
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<LanguageEntity> messages = langService.fetchAllByLocale(locale);
        if(messages==null || messages.size()==0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @GetMapping(value = "/{locale}/{key}")
    public ResponseEntity<LanguageEntity> getLangsByLocaleAndKey(
            @PathVariable String locale,
            @PathVariable String key) {

        if(locale == null
                ||locale.isEmpty()
                || key == null
                || key.isEmpty())
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        LanguageEntity lang = langService.fetchLangByLocaleAndKey(locale, key);

        if(lang == null || lang.getContent().isEmpty())
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(lang, HttpStatus.OK);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<LanguageEntity>> getAllLanguages(){
        List<LanguageEntity> messages = langService.fetchAll();
        if(messages.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @PostMapping("/saveOne")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPER_ADMIN')")
    public  ResponseEntity saveLanguageEntity(@Valid @RequestBody LanguageEntity lang){

        if(lang == null
                || lang.getKey().isEmpty()
                || lang.getContent().isEmpty())
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        if (langService.exists(lang.getKey(), lang.getLocale())) {
            return new ResponseEntity("Language with "+lang.getKey()+" and "+ lang.getLocale() +" already exist", HttpStatus.BAD_REQUEST);
        }

        LanguageEntity currLang = langService.save(lang);
        if(currLang==null)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity("Language has been saved", HttpStatus.CREATED);
    }

    @PostMapping("/saveMany")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPER_ADMIN')")
    public ResponseEntity saveSetOfLanguageEntity(@Valid @RequestBody List<LanguageEntity> langs){

        if(langs == null || langs.isEmpty())
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        for(LanguageEntity lang : langs) {
            if (langService.exists(lang.getKey(), lang.getLocale())) {
                return new ResponseEntity("Language with "+lang.getKey()+" and "+ lang.getLocale() +" already exist", HttpStatus.BAD_REQUEST);
            }
        }

        langService.saveAll(langs);
        return new ResponseEntity("Languages have been saved", HttpStatus.CREATED);
    }

    @PutMapping("/updateMany")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPER_ADMIN')")
    public ResponseEntity<LanguageEntity> updateSetOfLanguages(
            @RequestBody List<LanguageEntity> langs){

        if(langs == null || langs.isEmpty())
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        langService.updateAll(langs);

        return new ResponseEntity("Languages have been updated", HttpStatus.CREATED);
    }


    @PutMapping(value = "/updateOne")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPER_ADMIN')")
    public ResponseEntity<LanguageEntity> updateLanguageEntity(@RequestBody LanguageEntity langEntity){
        if(langEntity == null
                || langEntity.getId()==null
                || langEntity.getContent().isEmpty()
                || langEntity.getKey().isEmpty())
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Long id = langEntity.getId();
        if(!langService.exists(id))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        LanguageEntity currentLang = langService.fetchOne(id);
        currentLang.setLocale(langEntity.getLocale());
        currentLang.setKey(langEntity.getKey());
        currentLang.setContent(langEntity.getContent());

        LanguageEntity dbEntity = langService.save(currentLang);

        if (dbEntity == null)
            return  new ResponseEntity<>(HttpStatus.NOT_MODIFIED);

        return new ResponseEntity<>(currentLang, HttpStatus.OK);
    }

    @DeleteMapping("/deleteMany")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPER_ADMIN')")
    public ResponseEntity<String> deleteSetOfLangs(@RequestBody List<Long> ids ){
        if(ids==null || ids.isEmpty())
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        langService.deleteAll(ids);
        return new ResponseEntity<>("Language Entities have been Deleted!", HttpStatus.OK);
    }

    @DeleteMapping(value = "deleteOne/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPER_ADMIN')")
    public ResponseEntity<String> deleteLang(@PathVariable Long id ){
        if(id==null)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(!langService.exists(id))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        langService.delete(id);
        return new ResponseEntity<>("Language Entity has been Deleted!", HttpStatus.OK);
    }
}
