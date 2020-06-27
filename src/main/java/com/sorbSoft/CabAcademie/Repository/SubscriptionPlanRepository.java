package com.sorbSoft.CabAcademie.Repository;

import com.sorbSoft.CabAcademie.Entities.Enums.OrganizationType;
import com.sorbSoft.CabAcademie.Entities.Enums.SubscriptionPlanLevel;
import com.sorbSoft.CabAcademie.Entities.SubscriptionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, Long> {


    SubscriptionPlan findOneByLevelAndTypeAndIsActiveTrue(SubscriptionPlanLevel level, OrganizationType type);


    SubscriptionPlan findOneByPriceId(String priceId);
}
