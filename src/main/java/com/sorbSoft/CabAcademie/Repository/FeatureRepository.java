package com.sorbSoft.CabAcademie.Repository;

import com.sorbSoft.CabAcademie.Entities.Feature;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Dany on 15/05/2018.
 */
public interface FeatureRepository extends JpaRepository<Feature, Long> {
    List<Feature> findAllByTitle(String id);
    List<Feature> findAllByIcon(String id);
}
