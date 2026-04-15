package com.fitness.userservice.dto;

import com.fitness.userservice.model.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserResponse {

    private String id;
//    private String keycloakId;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDateTime createdAt;
    private  LocalDateTime updatedAt;
}
