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
                if (role.name().equals(Roles.ROLE_SUPER_ADMIN.name())) {
                    Rol newRole = new Rol();
                    newRole.setName("Super Admin");
                    newRole.setDescription(role.name());
                    rolRepository.save(newRole);
                } else if (role.name().equals(Roles.ROLE_ADMIN.name())) {
                    Rol newRole = new Rol();
                    newRole.setName("Admin");
                    newRole.setDescription(role.name());
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
            admin.setUsername("admin");
            admin.setFirstName("Joseph Marc-Antoine");
            admin.setLastName("Ridore");
            admin.setEmail("marcridore@gmail.com");
            admin.setAgreeWithTerms(true);
            admin.setPassword(bCryptPasswordEncoder.encode("admin")); // Todo: Change password
            userRepository.save(admin);
        }
    }
}
