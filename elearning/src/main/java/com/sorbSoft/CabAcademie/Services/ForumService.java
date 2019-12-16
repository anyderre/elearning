package com.sorbSoft.CabAcademie.Services;


import com.sorbSoft.CabAcademie.Entities.Forum;
import com.sorbSoft.CabAcademie.Repository.ForumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Dany on 18/05/2018.
 */
@Service
@Transactional
public class ForumService {
    @Autowired
    private ForumRepository forumRepository;

    public List<Forum> fetchAllForum(){
        return forumRepository.findAll();
    }

    public Forum fetchForum(Long id){
        return forumRepository.findOne(id);
    }

    public Forum updateForum(Forum forum){
        Forum currentForum= forumRepository.findOne(forum.getId());
        currentForum.setCourse(forum.getCourse());
        currentForum.setField(forum.getField());

        return forumRepository.save(currentForum);
    }
    public Forum saveForum(Forum forum){
        return forumRepository.save(forum);
    }


    public void deleteForum(Long id){
        forumRepository.delete(id);
    }
    //other delete methods
    //other fetching methods
}
