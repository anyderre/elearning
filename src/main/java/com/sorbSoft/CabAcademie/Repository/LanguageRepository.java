package com.sorbSoft.CabAcademie.Repository;

import com.sorbSoft.CabAcademie.Entities.LanguageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LanguageRepository extends JpaRepository<LanguageEntity, Long> {

    List<LanguageEntity> findAllByLocale(String locale);

    LanguageEntity findByKeyAndLocale(String key, String locale);
}
