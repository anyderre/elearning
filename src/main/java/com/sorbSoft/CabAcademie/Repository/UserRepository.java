package com.sorbSoft.CabAcademie.Repository;

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

    User findByRoleNameAndId(String roleName, Long professorId);

    Boolean existsByUsernameAndIdIsNot(String username, Long id);

    Boolean existsByEmailAndIdIsNot(String email, Long id);

    User findUserByEmailConfirmationUID(String emailConfirmationUid);

    User findUserByWorkspaceName(String workspaceName);
}
