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

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

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
                if (role.name().equals(Roles.ROLE_SUPER_ADMIN.name())) {
                    Rol newRole = new Rol();
                    newRole.setRol(role.name());
                    rolRepository.save(newRole);
                } else if (role.name().equals(Roles.ROLE_ADMIN.name())) {
                    Rol newRole = new Rol();
                    newRole.setRol(role.name());
                    rolRepository.save(newRole);
                } else if (role.name().equals(Roles.ROLE_PROFESSOR.name())) {
                    Rol newRole = new Rol();
                    newRole.setRol(role.name());
                    rolRepository.save(newRole);
                } else if (role.name().equals(Roles.ROLE_USER.name())) {
                    Rol newRole = new Rol();
                    newRole.setRol(role.name());
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
            Rol rolSuperAdmin = rolRepository.findAllByRol(Roles.ROLE_SUPER_ADMIN.name());
            Rol rolAdmin = rolRepository.findAllByRol(Roles.ROLE_ADMIN.name());
            List<Rol> roles = new ArrayList<>();
            roles.add(rolSuperAdmin);
            roles.add(rolAdmin);
            User admin = new User();
            admin.setRoles(roles);
            admin.setUsername("admin");
            admin.setName("Joseph Marc-Antoine Ridore");
            admin.setPassword(bCryptPasswordEncoder.encode("admin")); // Todo: Change password
            userRepository.save(admin);
        }
    }
}
