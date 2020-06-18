package com.sorbSoft.CabAcademie.Services;

import com.sorbSoft.CabAcademie.Entities.Enums.OrganizationType;
import com.sorbSoft.CabAcademie.Entities.Enums.SubscriptionPlanLevel;
import com.sorbSoft.CabAcademie.Repository.SubscriptionPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionPlanService {

    @Autowired
    private SubscriptionPlanRepository planRepository;

    public void save() {

    }

    public void fetchAll() {

    }

    public OrganizationType[] feetchAllOrganizationTypes(){
        return OrganizationType.values();

    }

    public SubscriptionPlanLevel[] feetchAllSubscriptionLevels(){
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

    }
}
