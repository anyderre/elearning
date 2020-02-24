package com.sorbSoft.CabAcademie.Services;

import com.sorbSoft.CabAcademie.Entities.Enums.Roles;
import com.sorbSoft.CabAcademie.Entities.Rol;
import com.sorbSoft.CabAcademie.Entities.User;
import com.sorbSoft.CabAcademie.Entities.ViewModel.UserViewModel;
import com.sorbSoft.CabAcademie.Repository.RolRepository;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.UserViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dany on 07/09/2017.
 */
@Service
@Transactional
public class RolServices {
    @Autowired
    private RolRepository rolRepository;

    public long cantidadUsuario(){
        return rolRepository.count();
    }

    public Rol creacionRol(Rol rol){
        rolRepository.save(rol);
        return rol;
    }
    public Rol getRole(String rol){
        return rolRepository.findAllByRol(rol);
    }
    public void elimarRolPorId(Long id){
        rolRepository.deleteRolById(id);
    }
    public List<Rol> todosRoles(){
        return rolRepository.findAll();
    }

    public List<Rol> getUserRoles (UserViewModel user) {
    List<Rol> roles = new ArrayList<>();
    if (user.isAdmin()){
        Rol rol = rolRepository.findAllByRol(Roles.ROLE_ADMIN.name());
        if (rol != null) {
            roles.add(rol);
        }
    }
    if (user.isProfessor()){
        Rol rol = rolRepository.findAllByRol(Roles.ROLE_PROFESSOR.name());
        if (rol != null) {
            roles.add(rol);
        }
    }
    return roles;
}

    public Pair<Boolean, Boolean> getUserRoles (User user) {
        if (user.getRoles().isEmpty()){
            return Pair.of(false, false);
        }
        boolean admin = false;
        boolean prof = false;
        for (Rol rol : user.getRoles()) {
            if (rol.getRol().equals(Roles.ROLE_ADMIN.name())){
                admin = true;
            } else if (rol.getRol().equals(Roles.ROLE_PROFESSOR.name())){
                prof = true;
            }
        }
        return Pair.of(admin, prof);
    }


}
