package com.sorbSoft.CabAcademie.Services;




import com.sorbSoft.CabAcademie.Entities.Video;
import com.sorbSoft.CabAcademie.Repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Dany on 18/05/2018.
 */
@Service
@Transactional
public class VideoService {
    @Autowired
    private VideoRepository videoRepository;

    public List<Video> fetchAllVideo(){
        return videoRepository.findAll();
    }

    public Video fetchVideo(Long id){
        return videoRepository.findOne(id);
    }

    public Video updateVideo(Video video){
        Video currentVideo= videoRepository.findOne(video.getId());
        currentVideo.setCourse(video.getCourse());
        currentVideo.setNext(video.getNext());
        currentVideo.setPremium(video.isPremium());
        currentVideo.setPrevious(video.getPrevious());
        currentVideo.setUrl(video.getUrl());

        return videoRepository.save(currentVideo);
    }
    public Video saveVideo(Video video){
        return videoRepository.save(video);
    }
    public void deleteVideo(Long id){
        videoRepository.delete(id);
    }
}
