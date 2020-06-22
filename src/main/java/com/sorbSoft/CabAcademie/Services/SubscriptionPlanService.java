package com.sorbSoft.CabAcademie.Services;

import com.sorbSoft.CabAcademie.Entities.Enums.OrganizationType;
import com.sorbSoft.CabAcademie.Entities.Enums.SubscriptionPlanLevel;
import com.sorbSoft.CabAcademie.Entities.Rol;
import com.sorbSoft.CabAcademie.Entities.User;
import com.sorbSoft.CabAcademie.Repository.SubscriptionPlanRepository;
import com.sorbSoft.CabAcademie.Repository.UserRepository;
import com.sorbSoft.CabAcademie.exception.RoleNotAllowedException;
import com.sorbSoft.CabAcademie.exception.UserNotFoundExcepion;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class SubscriptionPlanService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubscriptionPlanRepository planRepository;

    @Autowired
    private GenericValidator validator;

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

    public void checkIfDraftCourseAddAllowed(Long userId) throws UserNotFoundExcepion, RoleNotAllowedException {
        User user = userRepository.findById(userId);
        validator.validateNull(user, "userId", userId);

        Rol role = user.getRole();
        validateAllowedRoles(role);

        if(isProfessor(role) || isInstructor(role) || isAdmin(role)) {
            log.debug("Role is: ",role.getDescription());
        }

        if(isSuperAdmin(role)){
            log.debug("Role is: ",role.getDescription());
        }

    }

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
}
