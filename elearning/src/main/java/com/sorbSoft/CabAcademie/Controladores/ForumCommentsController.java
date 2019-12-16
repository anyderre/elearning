package com.sorbSoft.CabAcademie.Controladores;


import com.sorbSoft.CabAcademie.Entities.ForumComments;
import com.sorbSoft.CabAcademie.Services.ForumCommentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by Dany on 20/05/2018.
 */
@RestController
@RequestMapping(value = "/api/forumComments")
public class ForumCommentsController {
    @Autowired
    private ForumCommentsService forumCommentsService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<ForumComments> getForumComments(@PathVariable Long id){
        if(id<0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        ForumComments forumComments = forumCommentsService.fetchForumComments(id);
        if (forumComments==null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(forumComments, HttpStatus.OK);
    }

    @GetMapping("/forum/{id}")
    public ResponseEntity<List<ForumComments>> getAllForumComments(@PathVariable Long id){
        List<ForumComments> forumComments= forumCommentsService.fetchAllForumComments(id);
        if(forumComments.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(forumComments, HttpStatus.OK);
    }

    @PostMapping()
    public  ResponseEntity<ForumComments> saveForumComments(@Valid @RequestBody ForumComments forumComments){
        ForumComments currentForumComments= forumCommentsService.saveForumComments(forumComments);
        if(currentForumComments==null)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return  new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ForumComments> updateForumComments(@PathVariable Long id, @RequestBody ForumComments forumComments){
        if(id<0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(forumCommentsService.fetchForumComments(id)==null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        ForumComments currentForumComments= forumCommentsService.updateForumComments(forumComments);
        if (currentForumComments==null)
            return  new ResponseEntity<>(HttpStatus.NOT_MODIFIED);

        return new ResponseEntity<>(currentForumComments, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteForumComments(@PathVariable Long id ){
        if(id<0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(forumCommentsService.fetchForumComments(id)==null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        forumCommentsService.deleteForumComments(id);
        return new ResponseEntity<>("ForumComments Deleted!", HttpStatus.OK);
    }
}
