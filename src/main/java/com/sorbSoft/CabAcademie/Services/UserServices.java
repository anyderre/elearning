package com.sorbSoft.CabAcademie.Services;

import com.sorbSoft.CabAcademie.Entities.Enums.Roles;
import com.sorbSoft.CabAcademie.Entities.Error.CustomExceptionHandler;
import com.sorbSoft.CabAcademie.Entities.Rol;
import com.sorbSoft.CabAcademie.Entities.User;
import com.sorbSoft.CabAcademie.Repository.UserRepository;
import com.sorbSoft.CabAcademie.Services.Dtos.Factory.UserFactory;
import com.sorbSoft.CabAcademie.Services.Dtos.Info.UserInfo;
import com.sorbSoft.CabAcademie.Services.Dtos.Mapper.UserMapper;
import com.sorbSoft.CabAcademie.Services.Dtos.Validation.Result;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.UserViewModel;
import org.hibernate.NonUniqueObjectException;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by anyderre on 11/08/17.
 */
@Service
@Transactional
public class UserServices {
    @Autowired
   private UserRepository UserRepository;
    @Autowired
    private RolServices rolServices;
    @Autowired
    private SectionService sectionService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private  CategoryService categoryService;
    @Autowired
    private SubCategoryService subCategoryService;

    @Autowired
    private UserRepository userRepository;

    private UserMapper mapper
            = Mappers.getMapper(UserMapper.class);

    public Pair<String, User> saveUser(UserViewModel vm){
        if (vm.getId() > 0L) {
            return updateUser(vm);
        } else {
            User savedUser = userRepository.findByUsername(vm.getUsername());

            if ( savedUser != null){
                return Pair.of("The user you are trying to save already exist", null);
            }
            User resultUser = mapper.mapToEntity(vm);
            resultUser.setPassword(bCryptPasswordEncoder.encode(resultUser.getPassword()));
            if (resultUser.getRole().getId() == Roles.ROLE_STUDENT.ordinal() || resultUser.getRole().getId() == Roles.ROLE_FREE_STUDENT.ordinal()){
                resultUser.setName(resultUser.getFirstName() + ' ' + resultUser.getLastName());
            }
            User result = userRepository.save(resultUser);
            if (result == null){
                return Pair.of("Couldn't save the user", null);
            } else {
                return  Pair.of("User saved successfully", result);
            }
        }
    }

    public Pair<String, User> updateUser(UserViewModel vm){
        vm = prepareEntity(vm);
        Result result = ValidateModel(vm);
        if (!result.isValid()) {
            return Pair.of(result.lista.get(0).message, null);
        }
        User savedUser = userRepository.findUserByUsernameAndIdIsNot(vm.getUsername(), vm.getId());
        if (savedUser != null) {
            return Pair.of("The user name already exist for another definition", null);
        }
        User currentUser = UserRepository.findById(vm.getId());

        if (currentUser == null){
            return Pair.of("The user you are trying to update does not exist anymore", null);
        }

        User resultUser = mapper.mapToEntity(vm);
        if (resultUser.getRole().getId() == Roles.ROLE_STUDENT.ordinal() || resultUser.getRole().getId() == Roles.ROLE_FREE_STUDENT.ordinal()){
            resultUser.setName(resultUser.getFirstName() + ' ' + resultUser.getLastName());
        }

        User user = userRepository.save(resultUser);
        if (user == null) {
            return Pair.of("Couldn't update the user", null);
        } else {
            return Pair.of("User updated successfully", user);
        }
    }

    public User findUserbyUsername(String username){
        return UserRepository.findByUsername(username);
    }

    public List<User> findAllUser(){
        return UserRepository.findAll();
    }

    public List<User> findAllUsersFilteredFromAdmin(){
        return UserRepository.findAll().stream().filter(user ->
                !user.getRole().getDescription().equals(Roles.ROLE_SUPER_ADMIN.name()) && !user.getRole().getDescription().equals(Roles.ROLE_ADMIN.name())).collect(Collectors.toList());
    }

    public List<User> filterUserByRole(Long roleId){
        return UserRepository.findAll().stream().filter(user ->
                user.getRole().getId().equals(roleId)).collect(Collectors.toList());
    }

    public String deleteUser(Long id){
        if (id <= 0L) {
            return "You should indicate the id of the user";
        }
        User user = userRepository.findById(id);
        if (user == null) {
            return "The user you want to delete doesn't exist";
        }
        user.setDeleted(true);
        return "User successfully deleted";
    }

    public User findAUser(Long id){
        return UserRepository.findById(id);
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


        if (Roles.ROLE_SCHOOL.ordinal() ==  vm.getRole().getId().intValue() || Roles.ROLE_ORGANIZATION.ordinal() ==  vm.getRole().getId().intValue()) {
            if (vm.getName().isEmpty()) {
                result.add("You should the name of the school");
                return result;
            }
//            if (vm.getWorkspaceName().isEmpty()) {
//                result.add("You should the workspace name of the school");
//                return result;
//            }
        }
        if (vm.getRole()== null || vm.getRole().getId() <= 0) {
            result.add("You should specify the role");
            return result;
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

        return result;
    }

    public UserViewModel prepareEntity(UserViewModel vm) {
        if (vm.getRole().getId() == Roles.ROLE_SCHOOL.ordinal()){
            vm.setCategories(new ArrayList<>());
            vm.setSubCategories(new ArrayList<>());
            vm.setSchools(new ArrayList<>());
            vm.setCourses(new ArrayList<>());
            vm.setOrganizations(new ArrayList<>());
        }
         else if (Roles.ROLE_PROFESSOR.ordinal() ==  vm.getRole().getId().intValue()) {
            vm.setName(vm.getFirstName() + ' ' + vm.getLastName());
            //            vm.setWorkspaceName("");
            vm.setOrganizations(new ArrayList<>());
        } else if (Roles.ROLE_STUDENT.ordinal() ==  vm.getRole().getId().intValue()) {
            vm.setName(vm.getFirstName() + ' ' + vm.getLastName());
            //            vm.setWorkspaceName("");
            vm.setCategories(new ArrayList<>());
            vm.setSubCategories(new ArrayList<>());
            vm.setOrganizations(new ArrayList<>());
            vm.setCourses(new ArrayList<>());
        } else if (Roles.ROLE_FREELANCER.ordinal() ==  vm.getRole().getId().intValue()) {
            vm.setName(vm.getFirstName() + ' ' + vm.getLastName());
            //            vm.setWorkspaceName("");
            vm.setSchools(new ArrayList<>());
            vm.setOrganizations(new ArrayList<>());
        } else if (Roles.ROLE_FREE_STUDENT.ordinal() ==  vm.getRole().getId().intValue()) {
            vm.setName(vm.getFirstName() + ' ' + vm.getLastName());
            //            vm.setWorkspaceName("");
            vm.setCategories(new ArrayList<>());
            vm.setSubCategories(new ArrayList<>());
            vm.setSchools(new ArrayList<>());
            vm.setCourses(new ArrayList<>());
            vm.setOrganizations(new ArrayList<>());
        } else if (Roles.ROLE_EMPLOYEE.ordinal() ==  vm.getRole().getId().intValue()) {
            vm.setName(vm.getFirstName() + ' ' + vm.getLastName());
            //            vm.setWorkspaceName("");
            vm.setCategories(new ArrayList<>());
            vm.setSubCategories(new ArrayList<>());
            vm.setSchools(new ArrayList<>());
        } else if (Roles.ROLE_SUPER_ADMIN.ordinal() ==  vm.getRole().getId().intValue()) {
            vm.setName(vm.getFirstName() + ' ' + vm.getLastName());
            //            vm.setWorkspaceName("");
            vm.setCategories(new ArrayList<>());
            vm.setSubCategories(new ArrayList<>());
            vm.setSchools(new ArrayList<>());
            vm.setOrganizations(new ArrayList<>());
        } else if (Roles.ROLE_ORGANIZATION.ordinal() ==  vm.getRole().getId().intValue()) {
            vm.setName(vm.getFirstName() + ' ' + vm.getLastName());
            vm.setCategories(new ArrayList<>());
            vm.setSubCategories(new ArrayList<>());
            vm.setOrganizations(new ArrayList<>());
            vm.setSchools(new ArrayList<>());
            vm.setCourses(new ArrayList<>());
        } else if (Roles.ROLE_INSTRUCTOR.ordinal() ==  vm.getRole().getId().intValue()) {
            vm.setName(vm.getFirstName() + ' ' + vm.getLastName());
//            vm.setWorkspaceName("");
            vm.setCategories(new ArrayList<>());
            vm.setSubCategories(new ArrayList<>());
            vm.setSchools(new ArrayList<>());
        }
         return vm;
    }
}

