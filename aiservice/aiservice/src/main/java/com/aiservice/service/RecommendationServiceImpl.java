package com.aiservice.service;

import com.aiservice.model.Recommendation;
import com.aiservice.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService{
    private final RecommendationRepository recommendationRepository;

    public List<Recommendation> getUserRecommendation(String userId){
        return  recommendationRepository.findByUserId(userId);
    }
    public Recommendation getActivityRecommendation(String activityId){
        return  recommendationRepository.findByActivityId(activityId);
    }
}
