package com.sorbSoft.CabAcademie.Services;

import com.sorbSoft.CabAcademie.Entities.Rol;
import com.sorbSoft.CabAcademie.Entities.User;
import com.sorbSoft.CabAcademie.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Dany on 22/04/2019.
 */

@Service
public class SecurityServices implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;



    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    /**
     * Creando el User por defecto y su rol.
     */
    public void crearAdmin() {
        User userAdmin = userRepository.findByUsername("bradmin");
        if (userAdmin == null) {

            Rol rolSuperMegaAdmin = new Rol();
            rolSuperMegaAdmin.setRol("ROLE_SUPER_MEGA_ADMIN");
            List<Rol> roles = new ArrayList<>();
            roles.add(rolSuperMegaAdmin);
            User admin = new User();
            admin.setRoles(roles);
            admin.setUsername("bradmin");
            admin.setName("BR-Administrator");
            admin.setPassword(bCryptPasswordEncoder.encode("brtenant")); // Todo: Change password
//            admin.setCreationDate(new Date());
//            admin.setModificationDate(new Date());
            userRepository.save(admin);

        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if (username.isEmpty()) {
            throw new UsernameNotFoundException("Username must be provided");
        }

        User User = userRepository.findByUsername(username);
        if (User == null) {
            throw new UsernameNotFoundException(
                    String.format("Ce nom d'utilisateur n'existe pas antrepriz=%s",
                            username));
        }

        Set<GrantedAuthority> roles = new HashSet<>();
        for (Rol rol : User.getRoles()) {
            roles.add(new SimpleGrantedAuthority(rol.getRol()));
        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>(roles);

        return new org.springframework.security.core.userdetails.User(User.getUsername(), User.getPassword(), User.getEnable() == 1, true, true, true, grantedAuthorities);
    }
}