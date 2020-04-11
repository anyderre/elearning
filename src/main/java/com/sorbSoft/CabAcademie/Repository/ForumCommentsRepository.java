package com.sorbSoft.CabAcademie.Repository;


import com.sorbSoft.CabAcademie.Entities.ForumComments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Dany on 15/05/2018.
 */
public interface ForumCommentsRepository extends JpaRepository<ForumComments, Long>{
    List<ForumComments> findAllByForumId(Long id);
}
