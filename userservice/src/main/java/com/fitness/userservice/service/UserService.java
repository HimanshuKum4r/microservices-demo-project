package com.fitness.userservice.service;


import com.fitness.userservice.dto.RegisterRequest;
import com.fitness.userservice.dto.UserResponse;
import org.springframework.http.ResponseEntity;

public interface UserService {


    UserResponse registerRequest(RegisterRequest request);

    UserResponse getUser(java.lang.String userId);

       Boolean validateUser(String userId);
}
