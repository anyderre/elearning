package com.sorbSoft.CabAcademie.Controllers;

import com.sorbSoft.CabAcademie.Entities.Enums.OrganizationType;
import com.sorbSoft.CabAcademie.Entities.Enums.SubscriptionPlanLevel;
import com.sorbSoft.CabAcademie.Entities.Error.MessageResponse;
import com.sorbSoft.CabAcademie.Entities.SubscriptionPlan;
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
import java.util.List;


@RestController
@RequestMapping("/api/subscription-plan")
@Log4j2
public class SubscriptionPlanController {

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

    @GetMapping(value = "/get/all")
    @PreAuthorize("hasAuthority('ROLE_SUPER_ADMIN')")
    @ApiOperation(value = "Get all subscription plans, Role:SUPER_ROLE_ADMIN")
    public ResponseEntity<List<SubscriptionPlan>> getSubscriptionPlans() {


        List<SubscriptionPlan> all = planService.fetchAll();

        return new ResponseEntity<>(all, HttpStatus.OK);

    }

    @GetMapping(value = "/get-by-id/{id}")
    @PreAuthorize("hasAuthority('ROLE_SUPER_ADMIN')")
    @ApiOperation(value = "Get all subscription plans, Role:SUPER_ROLE_ADMIN")
    public ResponseEntity<SubscriptionPlan> getSubscriptionPlans(@PathVariable Long id) {

        if (id == null || id == 0)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        SubscriptionPlan plan = planService.fetchById(id);

        if(plan == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(plan, HttpStatus.OK);

    }

    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_SUPER_ADMIN')")
    @ApiOperation(value = "Delete subscription plan by ID, Role:SUPER_ROLE_ADMIN")
    public ResponseEntity<String> delete(@PathVariable Long id) {

        if (id == null || id == 0)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        planService.delete(id);

        return new ResponseEntity<>("Deleted", HttpStatus.OK);

    }


    @GetMapping(value = "/getLevels")
    @PreAuthorize("hasAuthority('ROLE_SUPER_ADMIN')")
    @ApiOperation(value = "Get subscription plan level, like BASIC, GOLD, etc, Role:ROLE_ADMIN")
    public ResponseEntity<SubscriptionPlanLevel[]> getLevels() {

        SubscriptionPlanLevel[] levels = planService.fetchAllSubscriptionLevels();

        return new ResponseEntity<>(levels, HttpStatus.OK);
    }

    @GetMapping(value = "/getOrganizationTypes")
    @PreAuthorize("hasAuthority('ROLE_SUPER_ADMIN')")
    @ApiOperation(value = "Get organization type of subscription plan, like School, Organization, Role:ROLE_ADMIN")
    public ResponseEntity<OrganizationType[]> getOrganizationTypes() {

        OrganizationType[] organizationTypes = planService.fetchAllOrganizationTypes();

        return new ResponseEntity<>(organizationTypes, HttpStatus.OK);
    }


}
