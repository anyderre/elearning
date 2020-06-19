package com.sorbSoft.CabAcademie.Controllers;

import com.sorbSoft.CabAcademie.Entities.Course;
import com.sorbSoft.CabAcademie.Entities.Enums.CourseStatus;
import com.sorbSoft.CabAcademie.Entities.Enums.OrganizationType;
import com.sorbSoft.CabAcademie.Entities.Enums.Roles;
import com.sorbSoft.CabAcademie.Entities.Enums.SubscriptionPlanLevel;
import com.sorbSoft.CabAcademie.Entities.Error.MessageResponse;
import com.sorbSoft.CabAcademie.Entities.SubscriptionPlan;
import com.sorbSoft.CabAcademie.Services.CourseService;
import com.sorbSoft.CabAcademie.Services.SubscriptionPlanService;
import com.sorbSoft.CabAcademie.Services.UserServices;
import com.sorbSoft.CabAcademie.exception.*;
import com.sorbSoft.CabAcademie.payload.CourseApproveRequest;
import com.sorbSoft.CabAcademie.payload.CourseDeclineRequest;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("/api/admin/dashboard/subscription-plan")
@Log4j2
public class SubscriptionPlanController {

    @Autowired
    private UserServices userService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private SubscriptionPlanService planService;

    @PostMapping(value = "/course/approve")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiOperation(value = "Approve course, Role:ROLE_ADMIN")
    public ResponseEntity<MessageResponse> approveCourse(@Valid @RequestBody CourseApproveRequest approveRq, Principal principal) throws CourseNotFoundExcepion, UserNotFoundExcepion, SchoolNotFoundExcepion {

        if (approveRq.getCourseId() == null || approveRq.getCourseId() <= 0 || principal == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        boolean isApproved = courseService.approveCourse(approveRq.getCourseId(), principal.getName());

        if(isApproved) {
            return new ResponseEntity<>(MessageResponse.of("Course has been Approved"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(MessageResponse.of("Not Approved. Probably Admin's and Course's schools do not match"), HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping(value = "/getLevels")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiOperation(value = "Get subscription plan level, like BASIC, GOLD, etc, Role:ROLE_ADMIN")
    public ResponseEntity<SubscriptionPlanLevel[]> getLevels() {

        SubscriptionPlanLevel[] levels = planService.feetchAllSubscriptionLevels();

        return new ResponseEntity<>(levels, HttpStatus.OK);
    }

    @GetMapping(value = "/getOrganizationTypes")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiOperation(value = "Get organization type of subscription plan, like School, Organization, Role:ROLE_ADMIN")
    public ResponseEntity<OrganizationType[]> getOrganizationTypes() {

        OrganizationType[] organizationTypes = planService.feetchAllOrganizationTypes();

        return new ResponseEntity<>(organizationTypes, HttpStatus.OK);
    }


}
