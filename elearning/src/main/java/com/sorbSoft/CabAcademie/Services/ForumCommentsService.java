package com.sorbSoft.CabAcademie.Services;


import com.sorbSoft.CabAcademie.Entities.ForumComments;
import com.sorbSoft.CabAcademie.Repository.ForumCommentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Dany on 18/05/2018.
 */
@Service
@Transactional
public class ForumCommentsService {
    @Autowired
    private ForumCommentsRepository forumCommentsRepository;

    public List<ForumComments> fetchAllForumComments(Long forumId){
        return forumCommentsRepository.findAllByForumId(forumId);
    }

    public ForumComments fetchForumComments(Long id){
        return forumCommentsRepository.findOne(id);
    }

    public ForumComments updateForumComments (ForumComments forumComments){
        ForumComments currentForumComments= forumCommentsRepository.findOne(forumComments.getId());
        currentForumComments.setForum(forumComments.getForum());
        currentForumComments.setParent_commnent(forumComments.getParent_commnent());
        currentForumComments.setText(forumComments.getText());
        currentForumComments.setUser(forumComments.getUser());
        return forumCommentsRepository.save(currentForumComments);
    }
    public ForumComments saveForumComments (ForumComments forumComments){
        return forumCommentsRepository.save(forumComments);
    }
    public void deleteForumComments(Long id){
        forumCommentsRepository.delete(id);
    }
    //other delete methods
    //other fetching methods
}
