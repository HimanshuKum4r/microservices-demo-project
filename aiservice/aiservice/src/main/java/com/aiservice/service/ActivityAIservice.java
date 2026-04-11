package com.aiservice.service;


import com.aiservice.model.Activity;
import com.aiservice.model.Recommendation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityAIservice {

    private final GeminiService geminiService;

    public Recommendation generateRecommendation(Activity activity){
        String prompt = createPromptForActivity(activity);
        String aiResponse = geminiService.getRecommendation(prompt);
        log.info("Response from Ai {} ",aiResponse);
        return processAIResponse(activity,aiResponse);
    }

    private Recommendation processAIResponse(Activity activity, String aiResponse) {
        try{
            ObjectMapper mapper =new ObjectMapper();
            JsonNode rootNode = mapper.readTree(aiResponse);
            JsonNode textNode =  rootNode.path("candidates")
                    .get(0)
                    .path("content")
                    .get("parts")
                    .get(0)
                    .path("text");
            String jsonContent = textNode.asText()
                    .replaceAll("```json\\n","")
                    .replaceAll("\\n```","")
                    .trim();

            log.info("clean Response from Ai {} ",jsonContent  );

            JsonNode analysisJson  = mapper.readTree((jsonContent));
            JsonNode analysisNode = analysisJson.path("analysis");
            StringBuilder fullanalysis = new StringBuilder();

            addAnalysisSection(fullanalysis,analysisNode,"overall", "Overall :");
            addAnalysisSection(fullanalysis,analysisNode,"pace", " Pace:");
            addAnalysisSection(fullanalysis,analysisNode,"hearRate", "HeartRate:");
            addAnalysisSection(fullanalysis,analysisNode,"caloriesBurned", "CaloriesBurned:");

            List<String> improvements = extractImprovements(analysisJson.path("improvements"));
            List<String> suggestions = extractSuggestions(analysisJson.path("suggestions"));
            List<String> safety = extractsafety(analysisJson.path("safety"));

            return Recommendation.builder()
                    .activityId(activity.getId())
                    .userId(activity.getUserId())
                    .type(activity.getActivityType().toString())
                    .recommendation(fullanalysis.toString().trim())
                    .improvements(improvements)
                    .suggestions(suggestions)
                    .safety(safety)
                    .createdAt(LocalDateTime.now())
                    .build();

        }

        catch (Exception e){
            e.printStackTrace();
            return createDefaultRecommendation(activity);

        }
    }

    private Recommendation createDefaultRecommendation(Activity activity) {
        return  Recommendation.builder()
                .activityId(activity.getId())
                .userId(activity.getUserId())
                .type(activity.getActivityType().toString())
                .recommendation("Unable to generate detialed analysis")
                .improvements(Collections.singletonList("continue with your current routine"))
                .suggestions(Collections.singletonList("consider consulting a fitness consultant"))
                .safety(Arrays.asList("always warmup before exercise"
                ,"stay Hydrated"))
                .createdAt(LocalDateTime.now())
                .build();
    }

    private List<String> extractsafety(JsonNode safetyNode) {
        List<String> safety = new ArrayList<>();
        if(safetyNode.isArray()){
            safetyNode.forEach(item->{
               safety.add(item.asText());
            });
        }
        return safety.isEmpty()?Collections.singletonList("follow general safety"):safety;
    }

    private List<String> extractSuggestions(JsonNode suggestionsNode) {
        List<String> suggestions = new ArrayList<>();
        if(suggestionsNode.isArray()){
            suggestionsNode.forEach(suggestion-> {
                String workout = suggestion.path("workout").asText();
                String description = suggestion.path("description").asText();
                suggestions.add(String.format("%s: %s",workout,description));
            });
        }
        return suggestions.isEmpty()?Collections.singletonList("no suggestions "):suggestions;

    }

    private List<String> extractImprovements(JsonNode improvementsNode) {

        List<String> improvements = new ArrayList<>();
        if(improvementsNode.isArray()){
           improvementsNode.forEach(improvement-> {
               String area = improvement.path("area").asText();
               String detail = improvement.path("recommendation").asText();
               improvements.add(String.format("%s: %s",area,detail));
           });

        }
        return improvements.isEmpty()? Collections.singletonList("no improvment provided"):improvements;

    }

    private void addAnalysisSection(StringBuilder fullanalysis, JsonNode analysisNode, String key, String prefix) {
    if(!analysisNode.path(key).isMissingNode()){
        fullanalysis.append(prefix)
                .append(analysisNode.path(key).asText())
                .append("\n\n");

    }


    }

    private String createPromptForActivity(Activity activity){
        return String.format("""
        Analyze this fitness activity and provide detailed recommendations in the following EXACT JSON format:
        {
          "analysis": {
            "overall": "Overall analysis here",
            "pace": "Pace analysis here",
            "heartRate": "Heart rate analysis here",
            "caloriesBurned": "Calories analysis here"
          },
          "improvements": [
            {
              "area": "Area name",
              "recommendation": "Detailed recommendation"
            }
          ],
          "suggestions": [
            {
              "workout": "Workout name",
              "description": "Detailed workout description"
            }
          ],
          "safety": [
            "Safety point 1",
            "Safety point 2"
          ]
        }

        Analyze this activity:
        Activity Type: %s
        Duration: %d minutes
        Calories Burned: %d
        Additional Metrics: %s
        
        Provide detailed analysis focusing on performance, improvements, next workout suggestions, and safety guidelines.
        Ensure the response follows the EXACT JSON format shown above.
        """,
                activity.getActivityType(),
                activity.getDuration(),
                activity.getCaloriesBurned(),
                activity.getAdditionalmetrics()
        );
    }


}
