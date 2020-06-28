package com.sorbSoft.CabAcademie.Services;

import com.sorbSoft.CabAcademie.Entities.Enums.OrganizationType;
import com.sorbSoft.CabAcademie.Entities.Enums.SubscriptionPlanLevel;
import com.sorbSoft.CabAcademie.Entities.SubscriptionPlan;
import com.sorbSoft.CabAcademie.Repository.SubscriptionPlanRepository;
import com.sorbSoft.CabAcademie.Repository.UserRepository;
import com.sorbSoft.CabAcademie.Services.Dtos.Mapper.SubscriptionPlanMapper;
import com.sorbSoft.CabAcademie.payload.SubscriptionPlanVm;
import lombok.extern.log4j.Log4j2;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class SubscriptionPlanService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubscriptionPlanRepository planRepository;

    @Autowired
    private GenericValidator validator;

    private SubscriptionPlanMapper mapper
            = Mappers.getMapper(SubscriptionPlanMapper.class);



    public void save(SubscriptionPlanVm addRequest) {
        SubscriptionPlan subscriptionPlan = mapper.mapToEntity(addRequest);
        planRepository.save(subscriptionPlan);

        //TODO: add validation
    }

    public List<SubscriptionPlan> fetchAll() {
        return planRepository.findAll();
    }

    public OrganizationType[] fetchAllOrganizationTypes(){
        return OrganizationType.values();

    }

    public SubscriptionPlanLevel[] fetchAllSubscriptionLevels(){
        return SubscriptionPlanLevel.values();
    }

    public void fetchActiveByLevelAndType() {
        //null is all
    }

    public void fetchNonActiveByLevelAndType() {
        //null is all
    }

    public void fetchLastAdded(int amount) {

    }

    public void delete(long subscriptionPlanId) {
        planRepository.delete(subscriptionPlanId);
    }

    public SubscriptionPlan fetchById(Long id) {
        return planRepository.findOne(id);
    }
}
