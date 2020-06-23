package com.sorbSoft.CabAcademie.Services.Dtos.Mapper;

import com.sorbSoft.CabAcademie.Entities.SubscriptionPlan;
import com.sorbSoft.CabAcademie.payload.SubscriptionPlanVm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel="spring")
public interface SubscriptionPlanMapper {

    SubscriptionPlanVm mapToViewModel(SubscriptionPlan subscriptionPlan);

    SubscriptionPlan mapToEntity(SubscriptionPlanVm vm);

    @Mappings({
            @Mapping(target = "id", ignore = true),
    })
    SubscriptionPlan mapEntityToEntity(SubscriptionPlan section);

    //SectionInfo mapEntityToInfo(Section section);
}
