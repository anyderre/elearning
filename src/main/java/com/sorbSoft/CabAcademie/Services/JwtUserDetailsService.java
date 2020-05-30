package com.sorbSoft.CabAcademie.Services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.sorbSoft.CabAcademie.Entities.Rol;
import com.sorbSoft.CabAcademie.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;


//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        if ("javainuse".equals(username)) {
//            return new User("javainuse", "$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6",
//                    new ArrayList<>());
//        } else {
//            throw new UsernameNotFoundException("User not found with username: " + username);
//        }
//    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if (username.isEmpty()) {
            throw new UsernameNotFoundException("Username must be provided");
        }

        com.sorbSoft.CabAcademie.Entities.User User = userRepository.findByUsername(username);
        if (User == null) {
            User = userRepository.findByEmail(username);
            if (User == null) {
                throw new UsernameNotFoundException(
                        String.format("User not found with username: %s",
                                username));
            }
        }

        Set<GrantedAuthority> roles = new HashSet<>();
//        for (Rol rol : User.getRole()) {
            roles.add(new SimpleGrantedAuthority(User.getRole().getRole().toString()));
//        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>(roles);

        return new org.springframework.security.core.userdetails.User(User.getUsername(), User.getPassword(), User.getEnable() == 1, true, true, true, grantedAuthorities);
    }
}