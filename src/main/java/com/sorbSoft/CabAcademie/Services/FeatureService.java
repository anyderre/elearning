package com.sorbSoft.CabAcademie.Services;


import com.sorbSoft.CabAcademie.Entities.Feature;
import com.sorbSoft.CabAcademie.Repository.FeatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Dany on 18/05/2018.
 */
@Service
public class FeatureService {
    @Autowired
    private FeatureRepository featureRepository;

    public List<Feature> fetchAllFeature(){
        return featureRepository.findAll();
    }

    public Feature saveFeature(Feature feature){
        return featureRepository.save(feature);
    }
    public void deleteFeature(Long id){
        featureRepository.deleteById(id);
    }
}
