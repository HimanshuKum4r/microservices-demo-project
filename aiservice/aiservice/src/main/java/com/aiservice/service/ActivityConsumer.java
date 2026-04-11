package com.aiservice.service;


import com.aiservice.model.Activity;
import com.aiservice.model.Recommendation;
import com.aiservice.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityConsumer {

    private final ActivityAIservice activityAIservice;
    private final RecommendationRepository recommendationRepository;

    @KafkaListener(topics = "activity-events" ,groupId = "activity-processor-group")
    public void processActivity(Activity activity){
        log.info("received activity for processing : {}" , activity.getUserId());
        Recommendation recommendation = activityAIservice.generateRecommendation(activity);
        recommendationRepository.save(recommendation);


    }
}
