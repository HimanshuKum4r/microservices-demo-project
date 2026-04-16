package com.gateway.gatewayservice.user;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final WebClient userServiceWebClient;

    public Mono<Boolean> validateUser(String userId) {
        log.info("calling user service : " + userId);
//        try {
            return userServiceWebClient.get()
                    .uri("/api/users/{userId}/validate", userId)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .onErrorResume(WebClientResponseException.class, e -> {
                        if (e.getStatusCode() == HttpStatus.NOT_FOUND)
                            return Mono.error(new RuntimeException("user not found : " + userId));
                        else if (e.getStatusCode() == HttpStatus.BAD_REQUEST)
                            return Mono.error(new RuntimeException("Invalid : " + userId));

                        return Mono.error(new RuntimeException("unexpeced error : " + userId));
                    }); //sync
//        } catch (WebClientResponseException e) {
//            e.printStackTrace();
//        }
//        return null;
    }

    public Mono<UserResponse> registerUser(RegisterRequest registerRequest) {
        return userServiceWebClient.post()
                .uri("/api/users/register/users")
                .bodyValue(registerRequest)
                .retrieve()
                .bodyToMono(UserResponse.class)
                .onErrorResume(WebClientResponseException.class, e -> {
                    if (e.getStatusCode() == HttpStatus.BAD_REQUEST)
                        return Mono.error(new RuntimeException("badrequest: " + e.getMessage()));

                    return Mono.error(new RuntimeException("unexpeced error : " + e.getMessage()));
                });

    }
}
