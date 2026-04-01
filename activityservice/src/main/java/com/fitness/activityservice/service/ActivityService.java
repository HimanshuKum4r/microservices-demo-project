package com.fitness.activityservice.service;

import com.fitness.activityservice.dto.ActivityRequest;
import com.fitness.activityservice.dto.ActivityResponse;
import org.jspecify.annotations.Nullable;

public interface ActivityService {

     ActivityResponse AddActivity(ActivityRequest activityRequest);
}
