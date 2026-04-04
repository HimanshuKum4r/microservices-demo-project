package com.fitness.userservice.service;

import com.fitness.userservice.dto.RegisterRequest;
import com.fitness.userservice.dto.UserResponse;
import com.fitness.userservice.model.User;
import com.fitness.userservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class UserServiceImpl implements UserService{
    @Autowired
    UserRepository userRepository;
    @Override
    public UserResponse registerRequest(RegisterRequest request) {

        if(userRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("user already exists");
        }

        User user = new User();

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());

        userRepository.save(user);


        return  new UserResponse(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getCreatedAt(),user.getUpdatedAt());




    }

    @Override
    public UserResponse getUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new RuntimeException(" user not found"));

        return  new UserResponse(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getCreatedAt(),user.getUpdatedAt());

    }

    @Override
    public Boolean validateUser(String userId) {
        log.info("validating user from database");
        return  userRepository.existsById(userId);
    }
}
