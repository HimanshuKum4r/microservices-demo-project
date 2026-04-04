package com.fitness.activityservice.service;

import com.fitness.activityservice.dto.ActivityRequest;
import com.fitness.activityservice.dto.ActivityResponse;
import com.fitness.activityservice.model.Activity;
import com.fitness.activityservice.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActivityServiceImpl implements ActivityService{
    @Autowired
    ActivityRepository activityRepository;

    private final UserValidationService userValidationService;

    public ActivityServiceImpl(UserValidationService userValidationService) {
        this.userValidationService = userValidationService;
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

        return new ActivityResponse(savedActivity.getId(), savedActivity.getUserId(), savedActivity.getActivityType(), savedActivity.getDuration(),
                savedActivity.getCaloriesBurned(), savedActivity.getStartTime(),savedActivity.getAdditionalmetrics(),savedActivity.getCreatedAt(),savedActivity.getUpdatedAt());


    }
}
