package com.sorbSoft.CabAcademie.Repository;

import com.sorbSoft.CabAcademie.Entities.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Created by Dany on 07/09/2017.
 */
@Repository
public interface RolRepository  extends JpaRepository<Rol, Long> {

    List<Rol> findAll();

    Rol findByName(String role);

    Rol findByDescription(String description);

    Rol findRolByNameAndIdIsNot(String name, Long id);

    void deleteRolById(Long id);
}
