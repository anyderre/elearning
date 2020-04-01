package com.sorbSoft.CabAcademie.Repository;

import com.sorbSoft.CabAcademie.Entities.User;
import org.springframework.data.domain.Pageable;
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

    User findUserByUsernameAndIdIsNot(String username, Long id);

    User findById(Long id);

    List<User>findAll();
}
