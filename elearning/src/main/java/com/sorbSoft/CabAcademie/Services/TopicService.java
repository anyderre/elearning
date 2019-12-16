package com.sorbSoft.CabAcademie.Services;


import com.sorbSoft.CabAcademie.Entities.Category;
import com.sorbSoft.CabAcademie.Entities.Topic;
import com.sorbSoft.CabAcademie.Repository.CategoryRepository;
import com.sorbSoft.CabAcademie.Repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Dany on 18/05/2018.
 */
@Service
public class TopicService {
    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private  CategoryService categoryService;

    public List<Topic> fetchAllTopic(){
        return topicRepository.findAll();
    }

    public Topic fetchTopic(Long id){
        return topicRepository.findOne(id);
    }

    public Topic updateTopic(Topic topic){
        Topic currentTopic= topicRepository.findOne(topic.getId());
        currentTopic.setCategory(topic.getCategory());
        currentTopic.setName(topic.getName());
        return topicRepository.save(currentTopic);
    }
    public Topic saveTopic(Topic topic){
        return topicRepository.save(topic);
    }


    public boolean saveTopics (List<Topic> topics){

        if(topics.size()==0)
            return  false;

        if(topics.get(0).getCategory().getId()!=0){
            Category category = categoryRepository.findOne(topics.get(0).getCategory().getId());
            if(category!=null){
                List<Topic> savedTopics = topicRepository.findAllByCategoryId(topics.get(0).getCategory().getId());
                for (Topic sTopic: savedTopics){
                    boolean existed = false;
                    for(Topic nTopics: topics){
                        System.out.println("s-> "+ sTopic.getId()+" n-> "+nTopics.getId());

                        if(nTopics.getId()!=null && sTopic.getId() !=0){
                            if(nTopics.getId().equals(sTopic.getId())) {
                                existed = true;
                                break;
                            }
                            existed =false;
                        }
                    }
                    if(!existed)
                        deleteTopic(sTopic.getId());
                }
               checkTopics(category,topics );

            }else{
                //peu probable
                Category currentCategory = categoryRepository.save(topics.get(0).getCategory());
                checkTopics(currentCategory, topics);
            }
        }
        else{

            Category currentCategory = categoryRepository.save(topics.get(0).getCategory());
            for(Topic topic: topics){
                topic.setCategory(currentCategory);
                topicRepository.save(topic);
            }
        }

       return true;
    }

    private void checkTopics(Category category, List<Topic> topics){

        for(Topic topic: topics){
            if(topic.getId()== null){
                topic.setCategory(category);
                topicRepository.save(topic);
            }else{
                updateTopic(topic);
                categoryService.updateCategory(topics.get(0).getCategory());
            }
        }
    }
    public List<Topic>fetchAllTopicByCategory(Long id){
       return topicRepository.findAllByCategoryId(id);
    }

    public void deleteTopic(Long id){
        topicRepository.delete(id);
    }

    public void deleteTopicByCategory(Long id){
        List<Topic> topics = topicRepository.findAllByCategoryId(id);
        topics.forEach((Topic topic) -> topicRepository.delete(topic));

    }
    //other delete methods
    //other fetching methods
}
