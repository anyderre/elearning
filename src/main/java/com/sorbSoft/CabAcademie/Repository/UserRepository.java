package com.sorbSoft.CabAcademie.Repository;

import com.sorbSoft.CabAcademie.Entities.Enums.Roles;
import com.sorbSoft.CabAcademie.Entities.Rol;
import com.sorbSoft.CabAcademie.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by anyderre on 11/08/17.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User save(User User);

    User findByUsername(String username);

    User findByEmail(String email);

    User findUserByUsernameAndIdIsNot(String username, Long id);

    User findById(Long id);

    List<User>findAll();

    List<User>findAllByRoleName(String roleName);

    User findByRoleRoleAndId(Roles role, Long professorId);

    List<User> findByRoleRole(Roles role);

    Boolean existsByUsernameAndIdIsNot(String username, Long id);

    Boolean existsByEmailAndIdIsNot(String email, Long id);

    User findUserByEmailConfirmationUID(String emailConfirmationUid);

    List<User> findUsersByWorkspaceName(String workspaceName);

    long countUsersByRoleAndSchoolsIn(Rol role, User school);

    long countUsersByRoleAndSchoolsIsNull(Rol role);

    User findOneByPasswordResetToken(String token);
}
