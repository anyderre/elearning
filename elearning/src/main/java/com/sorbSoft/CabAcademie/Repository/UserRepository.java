package com.sorbSoft.CabAcademie.Repository;

import com.sorbSoft.CabAcademie.Entities.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by anyderre on 11/08/17.
 */
@Repository

public interface UserRepository extends CrudRepository<User, Long> {
    User save(User User);

    User findByUsername(String username);

    User findById(Long id);

    List<User>findAll(Pageable pageable);

    List<User> findAllByUsername(String username);

    List<User>findAll();

    void deleteById(Long id);
}
