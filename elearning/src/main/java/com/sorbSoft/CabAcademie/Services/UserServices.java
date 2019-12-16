package com.sorbSoft.CabAcademie.Services;

import com.sorbSoft.CabAcademie.Entities.Rol;
import com.sorbSoft.CabAcademie.Entities.User;
import com.sorbSoft.CabAcademie.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
   private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
   private UserServices UserServices;

    public void  deleteUser(Long id){
        UserRepository.deleteById(id);
    }

    public User saveUser(User User){
        return UserRepository.save(User);
    }

    public User findUserbyUsername(String username){
        return UserRepository.findByUsername(username);
    }

    public List<User> findUsers(int page, int itemPerPage){
        Pageable pageable = new PageRequest(page,itemPerPage);
        return UserRepository.findAll(pageable);
    }

    public List<User> findAllUsers(){
        return UserRepository.findAll();
    }

    public List<User> findUserByUsername(String username){
        return UserRepository.findAllByUsername(username);
    }

    public void eliminarUserPorId(Long id){
        UserRepository.deleteById(id);
    }
    public User updateUser(User user){
        User currentUser = UserRepository.findById(user.getId());
        currentUser.setEnable(user.getEnable());
        currentUser.setName(user.getName());
        currentUser.setUsername(user.getUsername());
        return UserRepository.save(currentUser);

    }

    public User findAUser(Long id){
        return UserRepository.findById(id);
    }

    public void createAdmin(){
        List<User> Users = UserRepository.findAllByUsername("admin");
        System.out.println(Users.size() +"++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++=");
        Users.get(0).getRoles().forEach(rol ->  System.out.println(rol.getRol() +"++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++="));

        if(Users.size()<1){
            System.out.println("There+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            User User =  new User();
            User.setName("OMSA");
            User.setUsername("admin");
            User.setPassword(bCryptPasswordEncoder.encode("omsa1234"));
            saveUser(User);
            Rol rol = new Rol();
            rol.setUsername(User.getUsername());
            rol.setRol("ROLE_ADMIN");
            Rol rol2 = new Rol();
            rol2.setUsername(User.getUsername());
            rol2.setRol("ROLE_USER");
            //rolServices.creacionRol(rol);
            List<Rol> rols = new ArrayList<>();
            rols.add(rol);
            rols.add(rol2);
            User.setRoles(rols);
            UserServices.saveUser(User);
        }
    }
}

