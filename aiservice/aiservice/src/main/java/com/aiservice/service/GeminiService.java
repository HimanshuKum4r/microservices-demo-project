package com.aiservice.service;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class GeminiService {
    private final WebClient webClient;
    @Value("${gemini.api.url}")
    private String geminiUrl;
    @Value("${gemini.api.key}")
    private String geminiApiKey;

    public GeminiService(WebClient.Builder webclientbuilder){
        this.webClient = webclientbuilder.build();
    }

    public String getRecommendation(String details){
        Map<String,Object> requestBody = Map.of("contents",new Object[]{
                Map.of("parts",new Object[]{
                        Map.of("text",details)
                })
        });
        String response = webClient.post().uri(geminiUrl)
                .header("Content-Type", "application/json")
                .header("x-goog-api-key",geminiApiKey)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return response;



    }


}
