package com.sorbSoft.CabAcademie.Services;

import com.sorbSoft.CabAcademie.Entities.*;
import com.sorbSoft.CabAcademie.Entities.Enums.Roles;
import com.sorbSoft.CabAcademie.Repository.*;
import com.sorbSoft.CabAcademie.Services.Dtos.Factory.UserFactory;
import com.sorbSoft.CabAcademie.Services.Dtos.Info.UserInfo;
import com.sorbSoft.CabAcademie.Services.Dtos.Mapper.UserMapper;
import com.sorbSoft.CabAcademie.Services.Dtos.Validation.Result;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.UserViewModel;
import com.sorbSoft.CabAcademie.Services.email.EmailApiService;
import com.sorbSoft.CabAcademie.exception.*;
import com.sorbSoft.CabAcademie.payload.ChangeBatchPasswordRequest;
import com.sorbSoft.CabAcademie.payload.ChangePasswordRequest;
import com.sorbSoft.CabAcademie.payload.SetupNewPasswordRequest;
import lombok.extern.log4j.Log4j2;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by anyderre on 11/08/17.
 */
@Service
@Transactional
@Log4j2
public class UserServices {

    @Autowired
    private RolServices rolServices;
    @Autowired
    private RolRepository rolRepository;
    @Autowired
    private CourseService courseService;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private  CategoryService categoryService;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private SubCategoryService subCategoryService;
    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RolRepository roleRepository;

    @Autowired
    private EmailApiService emailAPI;

    @Autowired
    private GenericValidator validator;

    @Autowired
    private StripeService stripeService;

    private UserMapper mapper
            = Mappers.getMapper(UserMapper.class);

    public Result saveUser (UserViewModel vm) throws WorkspaceNameIsAlreadyTaken, SubscriptionPlanNotSpecified {
        vm = prepareEntity(vm);
        Result result = ValidateModel(vm);
        if (!result.isValid()) {
            return result;
        }
        if (vm.getId() > 0L) {
            return updateUser(vm);
        } else {
            User savedUser = userRepository.findByUsername(vm.getUsername());

            if ( savedUser != null){
                result.add("The user you are trying to save already exist");
                return result;
            }
            return save(vm);
        }
    }

    private  Result save (UserViewModel vm) {
        Result result = new Result();
        try {

           User user = null;

            if(isSchool(vm) || isOrganization(vm)) {

                User school = saveSchool(vm);

                User admin = makeAndSaveSchoolAdmin(vm, school);

                user = admin;
                emailAPI.sendUserRegistrationMail(admin);

            } else {
                user = getEntity(vm);
                userRepository.save(user);
                //if not social
                if(!vm.isSocialUser()) {
                    emailAPI.sendUserRegistrationMail(user);
                }
            }


            result.addValue(user);
        } catch (Exception ex)  {
            result.add(ex.getMessage());
            return result;
        }

        return result;
    }

    private User makeAndSaveSchoolAdmin(UserViewModel vm, User user) {

        User admin = getEntity(vm);
        Rol role = new Rol();
        role.setId(1L);
        role.setRole(Roles.ROLE_ADMIN);
        admin.setRole(role);

        if(isSchool(vm)) {
            admin.setSchools(Arrays.asList(user));
        }

        admin.setWorkspaceName("");

        return userRepository.save(admin);
    }

    private User saveSchool(UserViewModel vm) {
        User user = getEntity(vm);
        user.setEmail("school_"+user.getEmail());
        user.setUsername("school_"+user.getUsername());
        user.setEnable(1);

        /*String stripeId = stripeService.createCustomer(vm.getEmail(), vm.getStripeToken());
        String plan = vm.getSubscriptionLevel()+"-"+vm.getOrganizationType();
        String subscriptionId = stripeService.createSubscription(stripeId, plan, vm.getCoupon());

        user.setStripeId(stripeId);
        user.setSubscriptionId(subscriptionId);*/

        return userRepository.save(user);
    }

    private boolean isSchool(UserViewModel vm) {
        return vm.getRole().getId() == 2; //--> School
    }

    private boolean isOrganization(UserViewModel vm) { //--> Organization
        return vm.getRole().getId() == 7;
    }

    public Result saveSocialUser(UserViewModel vm) throws WorkspaceNameIsAlreadyTaken, SubscriptionPlanNotSpecified {

        Result result = new Result();
        result = saveUser(vm);

        if (!result.isValid()) {
            return result;
        }

        User user = (User)result.getValue();
        UserViewModel view = new UserViewModel();

        view.setId(user.getId());
        view.setRole(user.getRole());
        view.setFirstName(user.getFirstName());
        view.setLastName(user.getLastName());
        view.setUsername(user.getUsername());
        view.setEmail(user.getEmail());
        view.setWorkspaceName(user.getWorkspaceName());
        view.setPhotoURL(user.getPhotoURL());

        view.setFacebookId(user.getFacebookId());
        view.setGoogleId(user.getGoogleId());
        view.setLinkedinId(user.getLinkedinId());
        view.setBio(user.getBio());
        view.setTimeZone(user.getTimeZone());

        result.setValue(view);

        return result;
    }

    public Result updateUser(UserViewModel vm){
        Result result = new Result();
        User current = userRepository.findOne(vm.getId());

        if (current == null) {
            result.add("The user you want to update does not exist");
            return result;
        }

        User savedUser = userRepository.findUserByUsernameAndIdIsNot(vm.getUsername(), vm.getId());
        if (savedUser != null) {
            result.add("The user name already exist for another definition");
            return result;
        }
        User currentUser = userRepository.findById(vm.getId());

        if (currentUser == null){
            result.add("The user you are trying to update does not exist anymore");
            return result;
        }

        return save(vm);
    }

    public User findUserbyUsername(String username){
        return userRepository.findByUsername(username);

    }

    public UserViewModel findUserById(Long userId){
        User user = userRepository.findById(userId);
        UserViewModel userViewModel = mapper.mapToViewModel(user);
        return userViewModel;

    }

    public long countFreeUsersByRole(Roles role) {

        Rol studentRole = rolRepository.findByDescription(role.toString());

        long studentCount = userRepository.countUsersByRoleAndSchoolsIsNull(studentRole);

        return studentCount;

    }

    public long countUsersInSchoolByRole(String adminUsername, Roles role) throws SchoolNotFoundExcepion, UserNotFoundExcepion {

        User admin = userRepository.findByUsername(adminUsername);

        if (admin == null) {
            throw new UserNotFoundExcepion("User " + adminUsername + " doesn't exist in system");
        }
        List<User> schools = admin.getSchools();

        if (schools == null) {
            throw new SchoolNotFoundExcepion("Admin " + adminUsername + " doesn't belong to any school");
        }

        Rol studentRole = rolRepository.findByDescription(role.toString());
        long studentCount = 0;

        for (User school : schools) {
            studentCount = userRepository.countUsersByRoleAndSchoolsIn(studentRole, school);
            return studentCount;
        }

        return studentCount;

    }

    public List<User> findAllUser(){
        return userRepository.findAll();
    }

    public List<User> findSchoolProfessors(String userName) throws EmptyValueException, UserNotFoundExcepion, SchoolNotFoundExcepion {

        validator.validateNull(userName, "User should be logged in as school member. userName");
        User user = userRepository.findByUsername(userName);

        validator.validateNull(user, "userName", userName);

        List<User> schools = user.getSchools();

     /*  if(schools == null || schools.isEmpty()) {
            throw new SchoolNotFoundExcepion("User with username "+userName+" doesn't belong to any schools or organization");
        }*/

        List<User> professors = new ArrayList<>();

        for(User school : schools) {

            List<User> findings = new ArrayList<>();

            if(school.getRole().getRole() == Roles.ROLE_SCHOOL) {
                findings = userRepository.findAllBySchoolsInAndRoleRole(school, Roles.ROLE_PROFESSOR);
            }

            if(school.getRole().getRole() == Roles.ROLE_ORGANIZATION) {
                findings = userRepository.findAllBySchoolsInAndRoleRole(school, Roles.ROLE_INSTRUCTOR);
            }

            professors.addAll(findings);

        }
        return professors;
    }

    public List<User> findPublicProfessors() {
        return userRepository.findAllByRoleRole(Roles.ROLE_FREELANCER);
    }

    public UserViewModel findProfessor(Long professorId){
        User professor = userRepository.findByRoleRoleAndId(Roles.ROLE_PROFESSOR, professorId);

        UserViewModel userViewModel = mapper.mapToViewModel(professor);

        return userViewModel;
    }

    public List<User> findAllUsersFilteredFromAdmin(){
        return userRepository.findAll().stream().filter(user ->
                !user.getRole().getDescription().equals(Roles.ROLE_SUPER_ADMIN.name()) && !user.getRole().getDescription().equals(Roles.ROLE_ADMIN.name())).collect(Collectors.toList());
    }

    public List<User> filterUserByRole(Rol role){
        return userRepository.findAllByRole(role);
        /*return userRepository.findAll().stream().filter(user ->
                user.getRole().getId().equals(roleId)).collect(Collectors.toList());*/
    }

    public Result deleteUser(Long id){
        Result result = new Result();
        if (id <= 0L) {
            return result.add("You should indicate the id of the user");
        }
        User user = userRepository.findById(id);
        if (user == null) {
            return result.add("The user you want to delete doesn't exist");
        }
        user.setDeleted(true);
        try {
            userRepository.save(user);
        } catch (Exception ex)  {
            result.add(ex.getMessage());
            return result;
        }
        return result;
    }

    public User findAUser(Long id){
        return userRepository.findById(id);
    }

    public UserViewModel getUserViewModel(Long userId, String filterRoles){
        UserViewModel vm = UserFactory.getUserViewModel();
        if (userId != null && userId > 0) {
            User user = this.findAUser(userId);
            if (user == null) {
                return null;
            } else {
                vm = mapper.mapToViewModel(user);
            }
        }
        if (filterRoles.equals("1")) {
            vm.setAllRoles(rolServices.findAllRoleFiltered());
        } else {
            vm.setAllRoles(rolServices.fetchAllRole());
        }
        //vm.setAllCourses(courseService.fetchAllCourses());
        vm.setAllCategories(categoryService.fetchAllCategories());
        vm.setAllSubCategories(subCategoryService.fetchAllSubCategories());
        vm.setName(vm.getFirstName() + ' ' + vm.getLastName());

        Rol rolSchool = rolServices.findRoleByDescription(Roles.ROLE_SCHOOL.name());
        if (rolSchool != null){
            vm.setAllSchools(filterUserByRole(rolSchool));
        }
        Rol rolOrganization = rolServices.findRoleByDescription(Roles.ROLE_ORGANIZATION.name());
        if (rolOrganization != null){
            vm.setAllOrganizations(filterUserByRole(rolOrganization));
        }
        return vm;
    }

    private User getEntity(UserViewModel vm){
        User resultUser = mapper.mapToEntity(vm);
        resultUser.setPassword(bCryptPasswordEncoder.encode(resultUser.getPassword()));

        if (resultUser.getRole().getId() == Roles.ROLE_STUDENT.ordinal() || resultUser.getRole().getId() == Roles.ROLE_FREE_STUDENT.ordinal()){
            resultUser.setName(resultUser.getFirstName() + ' ' + resultUser.getLastName());
        }

        //check
        resultUser.setName(resultUser.getFirstName());

        //if user is not social
        if(!resultUser.getSocialUser()) {
            resultUser.setEmailConfirmationUID(generateUid());
        } else {
            //if social user
            resultUser.setEnable(1);
        }
        return resultUser;
    }

    public List <UserInfo> getUserInfo(){
        List<User> users= this.findAllUser();
        if (users.isEmpty()) {
            return new ArrayList<>();
        }
        List<UserInfo> info = new ArrayList<>();
        for (User user : users) {
            UserInfo uInfo = mapper.mapEntityToInfo(user);
            if (user.getRole() != null) {
                uInfo.setRoleName(user.getRole().getName());
            }
            info.add(uInfo);
        }
        return info;
    }

    private Result ValidateModel(UserViewModel vm) throws WorkspaceNameIsAlreadyTaken, SubscriptionPlanNotSpecified {
        Result result = new Result();

        if (vm.getRole().getId() <= 0) {
            result.add("You should specify the role");
            return result;
        }

        Rol role = rolRepository.findOne(vm.getRole().getId());
        if (role == null) {
            result.add("The role you specified does not exist");
            return result;
        }

        if (Roles.ROLE_SCHOOL ==  role.getRole() || Roles.ROLE_ORGANIZATION ==  role.getRole()) {
            if (vm.getName().isEmpty()) {
                result.add("You should specify the name");
                return result;
            }
            if (vm.getWorkspaceName().isEmpty()) {
                result.add("You should the workspace name of the school");
                return result;
            }
            if (vm.getWorkspaceName().trim().length() < 2) {
                result.add("You should specify at least two characters for the workspace name");
                return result;
            }
            List<User> schools = userRepository.findUsersByWorkspaceName(vm.getWorkspaceName());
            if(schools!=null && !schools.isEmpty()) {
                throw new WorkspaceNameIsAlreadyTaken("Workspace name '"+vm.getWorkspaceName()+"' is already taken");
            }

            /*if(vm.getSubscriptionLevel() == null) {
                log.warn("You should specify subscription plan level for school with workspace {}", vm.getWorkspaceName());
                throw new SubscriptionPlanNotSpecified("You should specify subscription plan level");
            }
            if(vm.getOrganizationType() == null) {
                log.warn("You should specify subscription organization type for school with workspace {}", vm.getWorkspaceName());
                throw new SubscriptionPlanNotSpecified("You should specify subscription organization type");
            }*/
        }
        if (vm.getUsername().isEmpty()) {
            result.add("You should specify the username");
            return result;
        }
        if (vm.getEmail().isEmpty()) {
            result.add("You should specify the email");
            return result;
        }
        if (vm.getPassword().isEmpty()) {
            result.add("You should specify the password");
            return result;
        }
        if (!vm.isAgreeWithTerms()) {
            result.add("You should accept terms and conditions");
            return result;
        }

        result.add(validateCategories(vm.getCategories()));
        result.add(validateSubCategories(vm.getSubCategories()));
        result.add(validateCourses(vm.getCourses()));
        result.add(validateSchools(vm.getSchools()));
        //result.add(validateOrganizations(vm.getOrganizations()));

        return result;
    }

    private UserViewModel prepareEntity(UserViewModel vm) {
        if (vm.getName()== null)  {
            vm.setName("");
        }
        if (vm.getId()== null)  {
            vm.setId(0L);
        }
        if (vm.getLastName()== null)  {
            vm.setLastName("");
        }
        if (vm.getEmail()== null)  {
            vm.setEmail("");
        }
        if (vm.getUsername()== null)  {
            vm.setUsername("");
        }

        if(!vm.isSocialUser()) {
            vm.setEnable(0);
        }

        if (vm.getPassword()== null) {
            vm.setPassword("");
        }
        if (vm.getBio()== null) {
            vm.setBio("");
        }
        if (vm.getCountry()== null) {
            vm.setCountry("");
        }
        if (vm.getPhotoURL()== null) {
            vm.setPhotoURL("");
        }
        if (vm.getRole()== null) {
            vm.setRole(new Rol(){
                @Override
                public Long getId() {
                    return 0L;
                }
            });
        }
        if (vm.getSubCategories()== null) {
            vm.setSubCategories(new ArrayList<>());
        }
        if (vm.getCategories()== null) {
            vm.setCategories(new ArrayList<>());
        }
        if (vm.getCourses()== null) {
            vm.setCourses(new ArrayList<>());
        }
        if (vm.getSchools()== null) {
            vm.setSchools(new ArrayList<>());
        }
        /*if (vm.getOrganizations()== null) {
            vm.setOrganizations(new ArrayList<>());
        }*/

        Rol role = rolRepository.findOne(vm.getRole().getId());
        if (role != null) {
            if (role.getDescription().equals(Roles.ROLE_SCHOOL.name())){
                vm.setCategories(new ArrayList<>());
                vm.setSubCategories(new ArrayList<>());
                vm.setSchools(new ArrayList<>());
                vm.setCourses(new ArrayList<>());
                //vm.setOrganizations(new ArrayList<>());
            }
             else if (role.getDescription().equals(Roles.ROLE_PROFESSOR.name())) {
                vm.setName(vm.getFirstName() + ' ' + vm.getLastName());
                vm.setWorkspaceName("");
                //vm.setOrganizations(new ArrayList<>());
            } else if (role.getDescription().equals(Roles.ROLE_STUDENT.name())) {
                vm.setName(vm.getFirstName() + ' ' + vm.getLastName());
                vm.setWorkspaceName("");
                vm.setCategories(new ArrayList<>());
                vm.setSubCategories(new ArrayList<>());
                //vm.setOrganizations(new ArrayList<>());
                vm.setCourses(new ArrayList<>());
            } else if (role.getDescription().equals(Roles.ROLE_FREELANCER.name())){
                vm.setName(vm.getFirstName() + ' ' + vm.getLastName());
                vm.setWorkspaceName("");
                vm.setSchools(new ArrayList<>());
                //vm.setOrganizations(new ArrayList<>());
            } else if (role.getDescription().equals(Roles.ROLE_FREE_STUDENT.name())) {
                vm.setName(vm.getFirstName() + ' ' + vm.getLastName());
                vm.setWorkspaceName("");
                vm.setCategories(new ArrayList<>());
                vm.setSubCategories(new ArrayList<>());
                vm.setSchools(new ArrayList<>());
                vm.setCourses(new ArrayList<>());
                //vm.setOrganizations(new ArrayList<>());
            } else if (role.getDescription().equals(Roles.ROLE_EMPLOYEE.name())) {
                vm.setName(vm.getFirstName() + ' ' + vm.getLastName());
                vm.setWorkspaceName("");
                vm.setCategories(new ArrayList<>());
                vm.setSubCategories(new ArrayList<>());
                vm.setSchools(new ArrayList<>());
            } else if (role.getDescription().equals(Roles.ROLE_SUPER_ADMIN.name())) {
                vm.setName(vm.getFirstName() + ' ' + vm.getLastName());
                vm.setWorkspaceName("");
                vm.setCategories(new ArrayList<>());
                vm.setSubCategories(new ArrayList<>());
                vm.setSchools(new ArrayList<>());
                //vm.setOrganizations(new ArrayList<>());
            } else if (role.getDescription().equals(Roles.ROLE_ADMIN.name())) {
                vm.setName(vm.getFirstName() + ' ' + vm.getLastName());
                vm.setWorkspaceName("");
                vm.setCategories(new ArrayList<>());
                vm.setSubCategories(new ArrayList<>());
                //vm.setSchools(new ArrayList<>());
                //vm.setOrganizations(new ArrayList<>());
            }else if (role.getDescription().equals(Roles.ROLE_ORGANIZATION.name())){
                vm.setName(vm.getFirstName() + ' ' + vm.getLastName());
                vm.setCategories(new ArrayList<>());
                vm.setSubCategories(new ArrayList<>());
                //vm.setOrganizations(new ArrayList<>());
                vm.setSchools(new ArrayList<>());
                vm.setCourses(new ArrayList<>());
            } else if (role.getDescription().equals(Roles.ROLE_INSTRUCTOR.name())) {
                vm.setName(vm.getFirstName() + ' ' + vm.getLastName());
                vm.setWorkspaceName("");
                vm.setCategories(new ArrayList<>());
                vm.setSubCategories(new ArrayList<>());
                vm.setSchools(new ArrayList<>());
            }

        }

         return vm;
    }

    private Result validateCategories(List<Category> categories) {
        Result result = new Result();
        if (categories.size() <= 0) {
            return result;
        }
        for (Category category : categories) {
            Category cat = categoryRepository.findOne(category.getId());
            if (cat == null) {
                result.add(String.format("The category no. {0} in the list of categories does not exist", categories.indexOf(category) + 1));
                return result;
            }
        }
        return  result;
    }

    private Result validateSubCategories(List<SubCategory> subCategories) {
        Result result = new Result();
        if (subCategories.size() <= 0) {
            return result;
        }
        for (SubCategory subCategory: subCategories) {
            SubCategory sub = subCategoryRepository.findOne(subCategory.getId());
            if (sub == null) {
                result.add(String.format("The sub-category no. {0} in the list of sub-categories does not exist", subCategories.indexOf(subCategory) + 1));
                return result;
            }
        }
        return  result;
    }

    private Result validateCourses(List<Course> courses) {
        Result result = new Result();
        if (courses.size() <= 0) {
            return result;
        }
        for (Course course: courses) {
            Course savedCourse = courseRepository.findOne(course.getId());
            if (savedCourse == null) {
                result.add(String.format("The course no. {0} in the list of courses does not exist", courses.indexOf(course) + 1));
                return result;
            }
        }
        return  result;
    }

    public Result validateSchools (List<User> schools) {
        Result result = new Result();
        if (schools.size() <= 0) {
            return result;
        }
        for (User school: schools) {
            User savedSchool = userRepository.findOne(school.getId());
            if (savedSchool == null) {
                result.add(String.format("The school no. {0} in the list of schools does not exist", schools.indexOf(school) + 1));
                return result;
            } else {
                if (!savedSchool.getRole().getDescription().equals(Roles.ROLE_SCHOOL.name())) {
                    result.add(String.format("The element no. {0} specified in the list of schools is not a school", schools.indexOf(school) + 1));
                    return result;
                }
            }
        }
        return  result;
    }

    private Result validateOrganizations (List<User> organizations) {
        Result result = new Result();
        if (organizations.size() <= 0) {
            return result;
        }
        for (User organization: organizations) {
            User savedOrganization = userRepository.findOne(organization.getId());
            if (savedOrganization == null) {
                result.add(String.format("The organization no. {0} in the list of organizations does not exist", organizations.indexOf(organization) + 1));
                return result;
            } else {
                if (!savedOrganization.getRole().getDescription().equals(Roles.ROLE_ORGANIZATION.name())) {
                    result.add(String.format("The element no. {0} specified in the list of organizations is not a organization", organizations.indexOf(organization) + 1));
                    return result;
                }
            }
        }
        return  result;
    }

    private String generateUid() {
        UUID uuid = UUID.randomUUID();
        String id = uuid.toString().replace("-", "");
        Date now = new Date();

        return id + now.getTime();
    }

    public Result confirmUserEmail(String emailConfirmationUid) {

        Result result = new Result();
        User user = userRepository.findUserByEmailConfirmationUID(emailConfirmationUid);

        if(user == null) {
            result.add("User not found");
            return result;
        }

        user.setEnable(1);
        user.setEmailConfirmationUID("");
        userRepository.save(user);

        return result;
    }


    public List<UserViewModel> findUserByWorkspaceName(String workspaceName) {

        List<UserViewModel> vms = new ArrayList<>();

        List<User> userByWorkspaceName = userRepository.findUsersByWorkspaceName(workspaceName);
        if(userByWorkspaceName != null && !userByWorkspaceName.isEmpty()) {
            for (User user : userByWorkspaceName) {
                UserViewModel vm = UserFactory.getUserViewModel();
                vm = mapper.mapToViewModel(user);

                vms.add(vm);
            }

            return vms;
        } else {
            return null;
        }
    }

    public void sendResetPasswordEmail(String email) throws UserNotFoundExcepion, EmptyValueException {
        User user = userRepository.findByEmail(email);
        validator.validateNull(user, email, "email");

        user.setPasswordResetToken(generateUid());
        userRepository.save(user);

        emailAPI.sendResetPasswordEmail(user);
    }

    public void setupNewPassword(SetupNewPasswordRequest resetRq) throws EmptyValueException, PasswordsDoNotMatchException, UserNotFoundExcepion {

        String code = resetRq.getCode();
        String password = resetRq.getPassword();
        String confirmPassword = resetRq.getConfirmPassword();

        validator.validateNull(code, "Reset token");
        validator.validateNull(password, "Password");
        validator.validateNull(confirmPassword, "Confirm Password");

        if(password.equals(confirmPassword)){

            User user = userRepository.findOneByPasswordResetToken(code);
            validator.validateNull(user, "token", "");

            user.setPassword(bCryptPasswordEncoder.encode(password));
            user.setPasswordResetToken("");
            user.setIsDefaultPasswordChanged(true);

            userRepository.save(user);
            emailAPI.sendPasswordChangedEmail(user);
        } else {
            throw new PasswordsDoNotMatchException("Password and confirm password do not match");
        }

    }

    public void changePassword(ChangePasswordRequest resetRq, String userName) throws EmptyValueException, PasswordsDoNotMatchException, UserNotFoundExcepion {

        String oldPassword = resetRq.getOldPassword();
        String newPassword = resetRq.getNewPassword();
        String confirmPassword = resetRq.getConfirmPassword();

        validator.validateNull(oldPassword, "Old password");
        validator.validateNull(newPassword, "New Password");
        validator.validateNull(confirmPassword, "Confirm Password");

        if(newPassword.equals(confirmPassword)){

            User user = userRepository.findByUsername(userName);
            validator.validateNull(user, "userName", userName);

            String currentPasswordEncoded = user.getPassword();

            if(bCryptPasswordEncoder.matches(oldPassword, currentPasswordEncoded)){
                user.setPassword(bCryptPasswordEncoder.encode(newPassword));
                user.setIsDefaultPasswordChanged(true);

                userRepository.save(user);

                emailAPI.sendPasswordChangedEmail(user);
            } else {
                throw new PasswordsDoNotMatchException("Old Password and current user password do not match");
            }
        } else {
            throw new PasswordsDoNotMatchException("New Password and confirm password do not match");
        }

    }

    public void changeBatchPassword(ChangeBatchPasswordRequest resetRq, String userName) throws EmptyValueException, PasswordsDoNotMatchException, UserNotFoundExcepion, UserIsNotBatchException {

        String newPassword = resetRq.getNewPassword();
        String confirmPassword = resetRq.getConfirmPassword();

        validator.validateNull(newPassword, "New Password");
        validator.validateNull(confirmPassword, "Confirm Password");

        if(newPassword.equals(confirmPassword)){

            User user = userRepository.findByUsername(userName);
            validator.validateNull(user, "userName", userName);

            if(user.getIsDefaultPasswordChanged() == false) {
                user.setPassword(bCryptPasswordEncoder.encode(newPassword));
                user.setIsDefaultPasswordChanged(true);

                userRepository.save(user);

                emailAPI.sendPasswordChangedEmail(user);
            } else {
                throw new UserIsNotBatchException("This user can't change password without providing old password");
            }

        } else {
            throw new PasswordsDoNotMatchException("New Password and confirm password do not match");
        }

    }

    public void batchSignupUsers(MultipartFile usersCsvFile, String adminName) throws UserNotFoundExcepion, SchoolNotFoundExcepion, CsvParseException, EmptyValueException, WorkspaceNameIsAlreadyTaken, SaveUserException, RoleNotAllowedException, RoleFormatException, SubscriptionPlanNotSpecified {
        User admin = userRepository.findByUsername(adminName);
        validator.validateNull(admin, adminName, "userName");


        List<UserViewModel> userViewModels = parseUsersFromCSVFile(usersCsvFile);
        setSchoolOrOrg(userViewModels, admin);

        for(UserViewModel vm : userViewModels) {
            Result result = null;

            result = saveUser(vm);

            if(!result.isValid()) {
                String email = vm.getEmail();
                int emailSize = email.length();
                email = email.substring(0,4)+"**"+email.substring(emailSize-5,emailSize);
                throw new SaveUserException(result.lista.get(0).getMessage()+", Email:"+email+" UserName:"+vm.getUsername());
            }

        }
    }

    private void setSchoolOrOrg(List<UserViewModel> userViewModels, User admin) throws SchoolNotFoundExcepion, RoleNotAllowedException {
        List<User> schools = admin.getSchools();

        if(schools != null && !schools.isEmpty()) {
            for(User school : schools){
                setSchool(userViewModels, school);
                break;
            }
        } else {
            log.error("Admin "+admin.getUsername()+" doesn't belong to any school or org");
            throw new SchoolNotFoundExcepion("Admin "+admin.getUsername()+" doesn't belong to any school or org");
        }
    }

    private void setSchool(List<UserViewModel> vms, User school) throws RoleNotAllowedException {
        for(UserViewModel vm : vms) {
            //student || professor
            if(vm.getRole().getId() == 3 || vm.getRole().getId() == 4) {
                vm.setSchools(Arrays.asList(school));
            } else {
                throw new RoleNotAllowedException("Role id:"+vm.getRole().getId()+" of userName:"+vm.getUsername()+" is not allowed for School batch signup");
            }
        }
    }

    /*private void setOrg(List<UserViewModel> vms, User organization) throws RoleNotAllowedException {
        for(UserViewModel vm : vms) {
            //employee or instructor
            if(vm.getRole().getId() == 8 || vm.getRole().getId() == 10) {
                vm.setOrganizations(Arrays.asList(organization));
            } else {
                throw new RoleNotAllowedException("Role id:"+vm.getRole().getId()+" of userName:"+vm.getUsername()+" is not allowed for Organization batch signup");
            }
        }
    }*/

    private List<UserViewModel> parseUsersFromCSVFile(final MultipartFile file) throws CsvParseException, EmptyValueException, RoleNotAllowedException, RoleFormatException {
        final List<UserViewModel> vms = new ArrayList<>();
        int CSV_LENGTH = 8;
        try (final BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            int lineNumber = 1;
            while ((line = br.readLine()) != null) {
                final String[] data = line.split(";");

                if (data.length == CSV_LENGTH) {

                    validator.validateNull(data[0], "First name at line:" + lineNumber);
                    validator.validateNull(data[1], "Last name at line:" + lineNumber);
                    validator.validateNull(data[2], "Username at line:" + lineNumber);
                    validator.validateNull(data[3], "email at line:" + lineNumber);
                    validator.validateNull(data[4], "password at line:" + lineNumber);
                    validator.validateNull(data[5], "country at line:" + lineNumber);
                    validator.validateNull(data[6], "Role id at line:" + lineNumber);
                    validator.validateNull(data[7], "Time zone at line:" + lineNumber);


                    long roleId;
                    try {
                        roleId = Long.parseLong(data[6]);
                    } catch (NumberFormatException e) {
                        throw new RoleFormatException("Can't parse roleId:" + data[6] + " to number at line:" + lineNumber);
                    }

                    validateAllowedRoles(roleId, lineNumber);

                    final UserViewModel user = new UserViewModel();
                    user.setFirstName(data[0]);
                    user.setLastName(data[1]);
                    user.setUsername(data[2]);
                    user.setEmail(data[3]);
                    user.setPassword(data[4]);
                    user.setCountry(data[5]);
                    user.setAgreeWithTerms(true);
                    user.setIsDefaultPasswordChanged(false);


                    Rol rol = new Rol();
                    rol.setId(Long.parseLong(data[6]));
                    user.setRole(rol);

                    user.setTimeZone(data[7]);
                    vms.add(user);

                    lineNumber++;
                } else {
                    throw new CsvParseException("Csv parameters amount at line:" + lineNumber + " is not equal:" + CSV_LENGTH);
                }
            }
            return vms;

        } catch (final IOException e) {
            log.error("Failed to parse CSV file {}", e);
            throw new CsvParseException("Failed to parse CSV file. Reason:" + e.getMessage());
        }
    }

    private void validateAllowedRoles(long roleId, int lineNumber) throws RoleNotAllowedException {
        if(roleId == 3 || roleId == 4 || roleId == 8 || roleId == 10) {

        } else {
            throw new RoleNotAllowedException("Role id:"+roleId+" at line:"+lineNumber+" is not allowed for batch signup");
        }
    }


}

