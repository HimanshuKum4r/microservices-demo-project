package com.aiservice.service;


import com.aiservice.model.Activity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityConsumer {

    private final ActivityAIservice activityAIservice;

    @KafkaListener(topics = "activity-events" ,groupId = "activity-processor-group")
    public void processActivity(Activity activity){
        log.info("received activity for processing : {}" , activity.getUserId());
        activityAIservice.generateRecommendation(activity);

    }
}
