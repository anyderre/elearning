package com.sorbSoft.CabAcademie.Services;


import com.sorbSoft.CabAcademie.Entities.Overview;
import com.sorbSoft.CabAcademie.Repository.OverviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Dany on 18/05/2018.
 */
@Service
@Transactional
public class OverviewService {
    @Autowired
    private OverviewRepository overviewRepository;

//    public List<Overview> fetchAllOverviewBySyllabus(Long syllabusId){
//        return overviewRepository.findAllBySyllabus_Id(syllabusId);
//    }

    public Overview fetchOverview(Long id){
        return overviewRepository.getOne(id);
    }

    public Overview updateOverview(Overview overview){
        Overview currentOverview = overviewRepository.getOne(overview.getId());
//        currentOverview.setSyllabus(overview.getSyllabus());

        return overviewRepository.save(currentOverview);
    }
    public Overview saveOverview(Overview overview){
        return overviewRepository.save(overview);
    }
    public void deleteOverview(Long id){
        overviewRepository.deleteById(id);
    }
    //other delete methods
    //other fetching methods
}
