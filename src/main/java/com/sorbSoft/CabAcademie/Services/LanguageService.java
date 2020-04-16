package com.sorbSoft.CabAcademie.Services;

import com.sorbSoft.CabAcademie.Entities.LanguageEntity;
import com.sorbSoft.CabAcademie.Repository.LanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class LanguageService {

    @Autowired
    private LanguageRepository repo;

    public void saveAll(List<LanguageEntity> list) {

        for(LanguageEntity ln : list) {
            repo.save(ln);
        }
    }

    public void updateAll(List<LanguageEntity> list) {

        for(LanguageEntity ln : list) {
            LanguageEntity dbEntity = repo.findOne(ln.getId());
            dbEntity.setLocale(ln.getLocale());
            dbEntity.setKey(ln.getKey());
            dbEntity.setContent(ln.getContent());

            repo.save(dbEntity);
        }
    }

    public void deleteAll(List<Long> ids) {
        for(Long id : ids) {
            repo.delete(id);
        }
    }

    public LanguageEntity fetchLangByLocaleAndKey(String locale, String key) {
        return repo.findByKeyAndLocale(key, locale);
    }

    public List<LanguageEntity> fetchAllByLocale(String locale) {
        return repo.findAllByLocale(locale);
    }

    public List<LanguageEntity> fetchAll() {
        return repo.findAll();
    }

    public LanguageEntity save(LanguageEntity lang) {
        return repo.save(lang);
    }

    public boolean exists(Long id) {
        return repo.exists(id);
    }

    public LanguageEntity fetchOne(Long id) {
        return repo.findOne(id);
    }

    public void delete(Long id) {
        repo.delete(id);
    }
}
