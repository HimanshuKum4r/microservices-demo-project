package com.fitness.activityservice.service;

import com.fitness.activityservice.dto.ActivityRequest;
import com.fitness.activityservice.dto.ActivityResponse;
import com.fitness.activityservice.model.Activity;
import com.fitness.activityservice.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ActivityServiceImpl implements ActivityService{
    @Autowired
    ActivityRepository activityRepository;

    private final UserValidationService userValidationService;
    private final KafkaTemplate<String, Activity> kafkaTemplate;

    @Value("${kafka.topic.name}")
    private String topicName;

    public ActivityServiceImpl(UserValidationService userValidationService, KafkaTemplate<String, Activity> kafkaTemplate) {
        this.userValidationService = userValidationService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public ActivityResponse AddActivity(ActivityRequest activityRequest) {

        Boolean isvalidUser = userValidationService.validateUser(activityRequest.getUserId());

        if (!isvalidUser){
            throw new RuntimeException("inavalid user" + activityRequest.getUserId());
        }


        Activity activity = Activity.builder()
                .userId(activityRequest.getUserId())

                .activityType(activityRequest.getActivityType())
                .duration(activityRequest.getDuration())
                .caloriesBurned((activityRequest.getCaloriesBurned()))
                .startTime(activityRequest.getStartTime())
                .additionalmetrics(activityRequest.getAdditionalmetrics())
                .build();

        Activity savedActivity = activityRepository.save(activity);
        try{
            kafkaTemplate.send(topicName,savedActivity.getUserId(),savedActivity);
        }catch (Exception e){
            e.printStackTrace();
        }


        return new ActivityResponse(savedActivity.getId(), savedActivity.getUserId(), savedActivity.getActivityType(), savedActivity.getDuration(),
                savedActivity.getCaloriesBurned(), savedActivity.getStartTime(),savedActivity.getAdditionalmetrics(),savedActivity.getCreatedAt(),savedActivity.getUpdatedAt());


    }
}
