package com.sorbSoft.CabAcademie.Services;

import com.sorbSoft.CabAcademie.Entities.Rol;
import com.sorbSoft.CabAcademie.Entities.User;
import com.sorbSoft.CabAcademie.Repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
//    public List<Rol> rolesUsuario(User user){
//        return rolRepository.findAllByUsername(user.getUsername());
//    }


}
