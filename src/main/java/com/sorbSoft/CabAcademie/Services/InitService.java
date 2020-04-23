package com.sorbSoft.CabAcademie.Services;

import com.sorbSoft.CabAcademie.Entities.Enums.Roles;
import com.sorbSoft.CabAcademie.Entities.Rol;
import com.sorbSoft.CabAcademie.Entities.User;
import com.sorbSoft.CabAcademie.Repository.RolRepository;
import com.sorbSoft.CabAcademie.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class InitService {

    private final RolRepository rolRepository;

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public InitService(RolRepository rolRepository, UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.rolRepository = rolRepository;
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void init() {
        createRoles();
        createAdmin();
    }
    /**
     * Create ROLES on Init
     *
     * @return
     */
    private void createRoles() {
        List<Rol> roles = rolRepository.findAll();
        if (roles.size() <= 0) {
            for (Roles role : Roles.values()) {
                if (role.name().equals(Roles.ROLE_ADMIN.name())) {
                    Rol newRole = new Rol();
                    newRole.setName("Admin");
                    newRole.setGenerated(true);
                    newRole.setDescription(role.name());
                    rolRepository.save(newRole);
                } else if (role.name().equals(Roles.ROLE_SCHOOL.name())) {
                    Rol newRole = new Rol();
                    newRole.setName("School");
                    newRole.setDescription(role.name());
                    newRole.setGenerated(true);
                    rolRepository.save(newRole);
                } else if (role.name().equals(Roles.ROLE_PROFESSOR.name())) {
                    Rol newRole = new Rol();
                    newRole.setName("Professor");
                    newRole.setDescription(role.name());
                    newRole.setGenerated(true);
                    rolRepository.save(newRole);
                } else if (role.name().equals(Roles.ROLE_STUDENT.name())) {
                    Rol newRole = new Rol();
                    newRole.setName("Student");
                    newRole.setDescription(role.name());
                    newRole.setGenerated(true);
                    rolRepository.save(newRole);
                }else if (role.name().equals(Roles.ROLE_FREELANCER.name())) {
                    Rol newRole = new Rol();
                    newRole.setName("Freelancer");
                    newRole.setDescription(role.name());
                    newRole.setGenerated(true);
                    rolRepository.save(newRole);
                }else if (role.name().equals(Roles.ROLE_FREE_STUDENT.name())) {
                    Rol newRole = new Rol();
                    newRole.setName("Free Student");
                    newRole.setDescription(role.name());
                    newRole.setGenerated(true);
                    rolRepository.save(newRole);
                }else if (role.name().equals(Roles.ROLE_ORGANIZATION.name())) {
                    Rol newRole = new Rol();
                    newRole.setName("Organization");
                    newRole.setDescription(role.name());
                    newRole.setGenerated(true);
                    rolRepository.save(newRole);
                }else if (role.name().equals(Roles.ROLE_EMPLOYEE.name())) {
                    Rol newRole = new Rol();
                    newRole.setName("Employee");
                    newRole.setGenerated(true);
                    newRole.setDescription(role.name());
                    rolRepository.save(newRole);
                } else if (role.name().equals(Roles.ROLE_SUPER_ADMIN.name())) {
                    Rol newRole = new Rol();
                    newRole.setName("Super Admin");
                    newRole.setDescription(role.name());
                    newRole.setGenerated(true);
                    rolRepository.save(newRole);
                } else if (role.name().equals(Roles.ROLE_INSTRUCTOR.name())) {
                    Rol newRole = new Rol();
                    newRole.setName("Instructor");
                    newRole.setDescription(role.name());
                    newRole.setGenerated(true);
                    rolRepository.save(newRole);
                }
            }
        }
    }

    /**
     * Create user and his role.
     */
    public void createAdmin() {
        System.out.println("Creating user ...");
        User userAdmin = userRepository.findByUsername("admin");
        if (userAdmin == null) {
            Rol roleAdmin = rolRepository.findByName("Admin");
            User admin = new User();
            admin.setRole(roleAdmin);
            admin.setFirstName("Joseph Marc-Antoine");
            admin.setLastName("Ridore");
            admin.setName(admin.getFirstName() + ' ' + admin.getLastName());
            admin.setUsername("admin");
            admin.setEmail("marcridore@gmail.com");
            admin.setPassword(bCryptPasswordEncoder.encode("admin")); // Todo: Change password
            admin.setAgreeWithTerms(true);
            admin.setBio("");
            admin.setEnable(1);
            admin.setPhotoURL("");
            admin.setCountry("Haiti");
            admin.setWorkspaceName("");
            admin.setCategories(new ArrayList<>());
            admin.setSubCategories(new ArrayList<>());
            admin.setCourses(new ArrayList<>());
            admin.setOrganizations(new ArrayList<>());
            admin.setSchools(new ArrayList<>());
            userRepository.save(admin);
        }
    }
}
