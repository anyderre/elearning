package com.sorbSoft.CabAcademie.Controllers;


import com.sorbSoft.CabAcademie.Entities.LanguageEntity;
import com.sorbSoft.CabAcademie.Services.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping
    public ResponseEntity<List<LanguageEntity>> getAllLanguages(){
        List<LanguageEntity> messages = langService.fetchAll();
        if(messages.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @PostMapping
    public  ResponseEntity saveLanguageEntity(@Valid @RequestBody LanguageEntity lang){

        if(lang == null
                || lang.getKey().isEmpty()
                || lang.getContent().isEmpty())
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        LanguageEntity currLang = langService.save(lang);
        if(currLang==null)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return  new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping
    public ResponseEntity saveSetOfLanguageEntity(@Valid @RequestBody List<LanguageEntity> langs){

        if(langs == null || langs.isEmpty())
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        langService.saveAll(langs);
        return  new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<LanguageEntity> updateSetOfLanguages(
            @RequestBody List<LanguageEntity> langs){

        if(langs == null || langs.isEmpty())
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        langService.updateAll(langs);
        return  new ResponseEntity<>(HttpStatus.CREATED);
    }


    @PutMapping(value = "/{id}")
    public ResponseEntity<LanguageEntity> updateLanguageEntity(@PathVariable Long id, @RequestBody LanguageEntity langEntity){
        if(id==null
                || langEntity==null
                || langEntity.getContent().isEmpty()
                || langEntity.getKey().isEmpty())
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

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

    @DeleteMapping
    public ResponseEntity<String> deleteSetOfLangs(@PathVariable List<Long> ids ){
        if(ids==null || ids.isEmpty())
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        langService.deleteAll(ids);
        return new ResponseEntity<>("Language Entities have been Deleted!", HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteLang(@PathVariable Long id ){
        if(id==null)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(!langService.exists(id))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        langService.delete(id);
        return new ResponseEntity<>("Language Entity has been Deleted!", HttpStatus.OK);
    }
}
