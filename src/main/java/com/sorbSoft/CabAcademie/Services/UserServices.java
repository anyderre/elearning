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
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by anyderre on 11/08/17.
 */
@Service
@Transactional
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
    private EmailApiService emailAPI;

    private UserMapper mapper
            = Mappers.getMapper(UserMapper.class);

    public Result saveUser (UserViewModel vm){
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
            User user = getEntity(vm);
            userRepository.save(user);

            //if not social
            if(!vm.isSocialUser()) {
                emailAPI.sendUserRegistrationMail(user);
            }
            result.addValue(user);
        } catch (Exception ex)  {
            result.add(ex.getMessage());
            return result;
        }

        return result;
    }

    public Result saveSocialUser(UserViewModel vm) {

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

    public List<User> findAllUser(){
        return userRepository.findAll();
    }

    public List<User> findAllProfessor(){
        return userRepository.findAllByRoleName(Roles.ROLE_PROFESSOR.name());
    }

    public User findProfessor(Long professorId){
        return userRepository.findByRoleNameAndId(Roles.ROLE_PROFESSOR.name(), professorId);
    }

    public List<User> findAllUsersFilteredFromAdmin(){
        return userRepository.findAll().stream().filter(user ->
                !user.getRole().getDescription().equals(Roles.ROLE_SUPER_ADMIN.name()) && !user.getRole().getDescription().equals(Roles.ROLE_ADMIN.name())).collect(Collectors.toList());
    }

    public List<User> filterUserByRole(Long roleId){
        return userRepository.findAll().stream().filter(user ->
                user.getRole().getId().equals(roleId)).collect(Collectors.toList());
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
        vm.setAllCourses(courseService.fetchAllCourses());
        vm.setAllCategories(categoryService.fetchAllCategories());
        vm.setAllSubCategories(subCategoryService.fetchAllSubCategories());
        vm.setName(vm.getFirstName() + ' ' + vm.getLastName());

        Rol rolSchool = rolServices.findRoleByDescription(Roles.ROLE_SCHOOL.name());
        if (rolSchool != null){
            vm.setAllSchools(filterUserByRole(rolSchool.getId()));
        }
        Rol rolOrganization = rolServices.findRoleByDescription(Roles.ROLE_ORGANIZATION.name());
        if (rolOrganization != null){
            vm.setAllOrganizations(filterUserByRole(rolOrganization.getId()));
        }
        return vm;
    }

    private User getEntity(UserViewModel vm){
        User resultUser = mapper.mapToEntity(vm);
        resultUser.setPassword(bCryptPasswordEncoder.encode(resultUser.getPassword()));

        if (resultUser.getRole().getId() == Roles.ROLE_STUDENT.ordinal() || resultUser.getRole().getId() == Roles.ROLE_FREE_STUDENT.ordinal()){
            resultUser.setName(resultUser.getFirstName() + ' ' + resultUser.getLastName());
        }

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

    private Result ValidateModel(UserViewModel vm){
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
        result.add(validateOrganizations(vm.getOrganizations()));

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
        if (vm.getOrganizations()== null) {
            vm.setOrganizations(new ArrayList<>());
        }

        Rol role = rolRepository.findOne(vm.getRole().getId());
        if (role != null) {
            if (role.getDescription().equals(Roles.ROLE_SCHOOL.name())){
                vm.setCategories(new ArrayList<>());
                vm.setSubCategories(new ArrayList<>());
                vm.setSchools(new ArrayList<>());
                vm.setCourses(new ArrayList<>());
                vm.setOrganizations(new ArrayList<>());
            }
             else if (role.getDescription().equals(Roles.ROLE_PROFESSOR.name())) {
                vm.setName(vm.getFirstName() + ' ' + vm.getLastName());
                vm.setWorkspaceName("");
                vm.setOrganizations(new ArrayList<>());
            } else if (role.getDescription().equals(Roles.ROLE_STUDENT.name())) {
                vm.setName(vm.getFirstName() + ' ' + vm.getLastName());
                vm.setWorkspaceName("");
                vm.setCategories(new ArrayList<>());
                vm.setSubCategories(new ArrayList<>());
                vm.setOrganizations(new ArrayList<>());
                vm.setCourses(new ArrayList<>());
            } else if (role.getDescription().equals(Roles.ROLE_FREELANCER.name())){
                vm.setName(vm.getFirstName() + ' ' + vm.getLastName());
                vm.setWorkspaceName("");
                vm.setSchools(new ArrayList<>());
                vm.setOrganizations(new ArrayList<>());
            } else if (role.getDescription().equals(Roles.ROLE_FREE_STUDENT.name())) {
                vm.setName(vm.getFirstName() + ' ' + vm.getLastName());
                vm.setWorkspaceName("");
                vm.setCategories(new ArrayList<>());
                vm.setSubCategories(new ArrayList<>());
                vm.setSchools(new ArrayList<>());
                vm.setCourses(new ArrayList<>());
                vm.setOrganizations(new ArrayList<>());
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
                vm.setOrganizations(new ArrayList<>());
            } else if (role.getDescription().equals(Roles.ROLE_ORGANIZATION.name())){
                vm.setName(vm.getFirstName() + ' ' + vm.getLastName());
                vm.setCategories(new ArrayList<>());
                vm.setSubCategories(new ArrayList<>());
                vm.setOrganizations(new ArrayList<>());
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
}

