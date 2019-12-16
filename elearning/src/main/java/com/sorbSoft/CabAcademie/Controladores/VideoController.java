package com.sorbSoft.CabAcademie.Controladores;

import com.sorbSoft.CabAcademie.Entities.Video;
import com.sorbSoft.CabAcademie.Services.VideoService;
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
@RequestMapping("/api/video")
public class VideoController {
    @Autowired
    private VideoService videoService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<Video> getVideo(@PathVariable Long id){
        if(id<0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Video video= videoService.fetchVideo(id);
        if(video==null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(video, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<Video>> getAllVideos(){
        List<Video> videos= videoService.fetchAllVideo();
        if(videos.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(videos, HttpStatus.OK);
    }

    @PostMapping()
    public  ResponseEntity<Video> saveVideo(@Valid @RequestBody Video video){
        Video currentVideo= videoService.saveVideo(video);
        if(currentVideo==null)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return  new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Video> updateVideo(@PathVariable Long id, @RequestBody Video video){
        if(id<0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(videoService.fetchVideo(id)==null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Video currentVideo= videoService.updateVideo(video);
        if (currentVideo==null)
            return  new ResponseEntity<>(HttpStatus.NOT_MODIFIED);

        return new ResponseEntity<>(currentVideo, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteVideo(@PathVariable Long id ){
        if(id<0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(videoService.fetchVideo(id)==null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        videoService.deleteVideo(id);
        return new ResponseEntity<>("Video with "+ id+" Deleted!", HttpStatus.OK);
    }
}
