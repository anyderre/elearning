package com.sorbSoft.CabAcademie.Controllers;

import com.sorbSoft.CabAcademie.Entities.Enums.OrganizationType;
import com.sorbSoft.CabAcademie.Entities.Enums.SubscriptionPlanLevel;
import com.sorbSoft.CabAcademie.Entities.Error.MessageResponse;
import com.sorbSoft.CabAcademie.Services.CourseService;
import com.sorbSoft.CabAcademie.Services.SubscriptionPlanService;
import com.sorbSoft.CabAcademie.Services.UserServices;
import com.sorbSoft.CabAcademie.payload.SubscriptionPlanVm;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/subscription-plan")
@Log4j2
public class SubscriptionPlanController {

    @Autowired
    private UserServices userService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private SubscriptionPlanService planService;

    @PostMapping(value = "/save")
    @PreAuthorize("hasAuthority('ROLE_SUPER_ADMIN')")
    @ApiOperation(value = "Add subscription plan, Role:SUPER_ROLE_ADMIN")
    public ResponseEntity<MessageResponse> addSubscriptionPlan(
            @Valid @RequestBody SubscriptionPlanVm addRequest) {

        if (addRequest == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        planService.save(addRequest);

        return new ResponseEntity<>(MessageResponse.of("Subscription plan added"), HttpStatus.OK);

    }


    @GetMapping(value = "/getLevels")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiOperation(value = "Get subscription plan level, like BASIC, GOLD, etc, Role:ROLE_ADMIN")
    public ResponseEntity<SubscriptionPlanLevel[]> getLevels() {

        SubscriptionPlanLevel[] levels = planService.fetchAllSubscriptionLevels();

        return new ResponseEntity<>(levels, HttpStatus.OK);
    }

    @GetMapping(value = "/getOrganizationTypes")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiOperation(value = "Get organization type of subscription plan, like School, Organization, Role:ROLE_ADMIN")
    public ResponseEntity<OrganizationType[]> getOrganizationTypes() {

        OrganizationType[] organizationTypes = planService.fetchAllOrganizationTypes();

        return new ResponseEntity<>(organizationTypes, HttpStatus.OK);
    }


}
