package com.sorbSoft.CabAcademie.Controladores;

import com.sorbSoft.CabAcademie.Entities.Forum;
import com.sorbSoft.CabAcademie.Services.ForumService;
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
@RequestMapping(value = "/api/forum")
public class ForumController {
    @Autowired
    private ForumService forumService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<Forum> getForum(@PathVariable Long id){
        if(id<0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Forum forum= forumService.fetchForum(id);
        if(forum==null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(forum, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<Forum>> getAllForum(){
        List<Forum> forums= forumService.fetchAllForum();
        if(forums.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(forums, HttpStatus.OK);
    }

    @PostMapping()
    public  ResponseEntity<Forum> saveForum(@Valid @RequestBody Forum forum){
        Forum currentForum= forumService.saveForum(forum);
        if(currentForum==null)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return  new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Forum> updateForum(@PathVariable Long id, @RequestBody Forum forum){
        if(id<0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(forumService.fetchForum(id)==null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Forum currentForum= forumService.updateForum(forum);
        if (currentForum==null)
            return  new ResponseEntity<>(HttpStatus.NOT_MODIFIED);

        return new ResponseEntity<>(currentForum, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteForum(@PathVariable Long id ){
        if(id<0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(forumService.fetchForum(id)==null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        forumService.deleteForum(id);
        return new ResponseEntity<>("Forum Deleted!", HttpStatus.OK);
    }
}
