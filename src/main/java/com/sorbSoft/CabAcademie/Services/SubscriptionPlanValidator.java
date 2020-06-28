package com.sorbSoft.CabAcademie.Services;

import com.sorbSoft.CabAcademie.Entities.Course;
import com.sorbSoft.CabAcademie.Entities.Enums.CourseStatus;
import com.sorbSoft.CabAcademie.Entities.Enums.OrganizationType;
import com.sorbSoft.CabAcademie.Entities.Enums.SubscriptionPlanLevel;
import com.sorbSoft.CabAcademie.Entities.Rol;
import com.sorbSoft.CabAcademie.Entities.SubscriptionPlan;
import com.sorbSoft.CabAcademie.Entities.User;
import com.sorbSoft.CabAcademie.Repository.CourseRepository;
import com.sorbSoft.CabAcademie.Repository.CourseSchoolRepository;
import com.sorbSoft.CabAcademie.Repository.SubscriptionPlanRepository;
import com.sorbSoft.CabAcademie.Repository.UserRepository;
import com.sorbSoft.CabAcademie.exception.*;
import lombok.extern.log4j.Log4j2;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Log4j2
public class SubscriptionPlanValidator {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private SubscriptionPlanRepository planRepository;

    @Autowired
    private CourseSchoolRepository courseSchoolRepository;

    @Autowired
    private StripeService stripeService;

    @Autowired
    private GenericValidator validator;


    private boolean isProfessor(Rol role) {
        return role.getId() == 3;
    }

    private boolean isInstructor(Rol role) {
        return role.getId() == 10;
    }

    private boolean isAdmin(Rol role) {
        return role.getId() == 1;
    }

    private boolean isSuperAdmin(Rol role) {
        return role.getId() == 9;
    }

    private void validateAllowedRoles(Rol role) throws RoleNotAllowedException {
        long roleId = role.getId();
        if(roleId == 1
                || roleId == 2
                || roleId == 3
                || roleId == 5
                || roleId == 7
                || roleId == 9
                || roleId == 10) {

        } else {
            throw new RoleNotAllowedException("Role id:"+roleId+" is not allowed for add draft course");
        }
    }

    public void checkIfSubscriptionValid(String adminUsername) throws UserNotFoundExcepion, SchoolNotFoundExcepion, SubscriptionPlanDateExpired, PaymentException {

        boolean isValid = false;

        User schoolAdmin = userRepository.findByUsername(adminUsername);
        validator.validateNull(schoolAdmin, "admin userName", adminUsername);

        List<User> adminSchools = schoolAdmin.getSchools();

        if (adminSchools == null || adminSchools.isEmpty()) {
            throw new SchoolNotFoundExcepion("Admin " + adminUsername + " doesn't belong to any school");
        }

        for(User school : adminSchools) {

            //subscription exist?
            if(school.getSubscriptionId() == null || school.getSubscriptionId().isEmpty()) {
                log.warn("School haven't made subscription yet. School ID: {}", school.getId());
                throw new PaymentException("School haven't made subscription yet. School ID: " + school.getId());
            }

            String status = stripeService.getSubscriptionStatus(school.getSubscriptionId());

            if(status == null || !status.equalsIgnoreCase("active")) {

                log.warn("You subscription plan is not active. School ID {}. Probably you need to update your payment method", school.getId());
                throw new SubscriptionPlanDateExpired("You subscription plan is not active. School ID "+school.getId()+". Probably you need to update your payment method");
            }
            return;
        }

        log.warn("Admin doesn't belong to any school. Admin userName {}", adminUsername);
        throw new SchoolNotFoundExcepion("Admin doesn't belong to any school. Admin userName "+ adminUsername);
    }

    public void validateCourseApprovment(Long courseId, String adminUsername) throws UserNotFoundExcepion, SchoolNotFoundExcepion, CourseNotFoundExcepion, MaxCoursesPerProfessorExceededException, PaymentException, SubscriptionPlanDateExpired {

        Course course2Approve = courseRepository.findOne(courseId);

        User teacher = course2Approve.getUser();

        validateAddCourseByAdmin(teacher.getId(), adminUsername);
    }

    public void validateAddCourseByAdmin(Long teacherId, String adminUsername) throws UserNotFoundExcepion, SchoolNotFoundExcepion, CourseNotFoundExcepion, MaxCoursesPerProfessorExceededException, PaymentException, SubscriptionPlanDateExpired {

        checkIfSubscriptionValid(adminUsername);

        User teacher = userRepository.findById(teacherId);

        User schoolAdmin = userRepository.findByUsername(adminUsername);

        validator.validateNull(teacher, "teacher ID", teacherId);
        validator.validateNull(schoolAdmin, "admin userName", adminUsername);

        //TODO: add verifications
        List<User> adminSchools = schoolAdmin.getSchools();

        if (adminSchools == null || adminSchools.isEmpty()) {
            throw new SchoolNotFoundExcepion("Admin " + adminUsername + " doesn't belong to any school");
        }

        long approvedCoursesAmount = 0;
        long maxCoursesPerProfessor = 0;
        for(User adminSchool : adminSchools) {

            approvedCoursesAmount = courseSchoolRepository.countCoursesBySchoolAndTeacherAndStatus(adminSchool, teacher, CourseStatus.APPROVED);

            SubscriptionPlanLevel subscriptionLevel = adminSchool.getSubscriptionLevel();
            OrganizationType organizationType = adminSchool.getOrganizationType();

            SubscriptionPlan subscriptionPlan = planRepository.findOneByLevelAndTypeAndIsActiveTrue(subscriptionLevel, organizationType);

            if(subscriptionLevel == null || organizationType == null) {
                log.error("Subscription plan is not set for school id:"+adminSchool.getId()+" user name:"+adminSchool.getUsername());
                return;
            }

            if(subscriptionPlan == null) {
                log.error("Subscription plan with level:"+subscriptionLevel+", type:"+organizationType+" is not available at db for school id:"+adminSchool.getId()+" user name:"+adminSchool.getUsername());
                //probably need to throw some exception
                return;
            }
            maxCoursesPerProfessor = subscriptionPlan.getMaxCoursesPerProfessor();

            break;
        }

        if(approvedCoursesAmount < maxCoursesPerProfessor) {
            log.debug("Course approvment for teacher id {} allowed", teacher.getId());
        } else {
            log.warn("Course approvment for teacher id {} Not allowed", teacher.getId());
            throw new MaxCoursesPerProfessorExceededException("Professor/instructor with user name "+teacher.getUsername()+" has maximum amount of approved courses "+maxCoursesPerProfessor);
        }


    }
}
