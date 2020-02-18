package com.sorbSoft.CabAcademie.Repository;

import com.sorbSoft.CabAcademie.Entities.Rol;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Created by Dany on 07/09/2017.
 */
@Repository
public interface RolRepository  extends CrudRepository<Rol, Long> {

    List<Rol> findAll();

    List<Rol> findAllByUsername(String Username);
    Rol findAllByRol(String role);

    void deleteRolById(Long id);

    void deleteRolByUsername(String username);
}
