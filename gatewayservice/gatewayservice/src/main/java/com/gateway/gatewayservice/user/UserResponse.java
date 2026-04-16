package com.gateway.gatewayservice.user;

import lombok.*;

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
