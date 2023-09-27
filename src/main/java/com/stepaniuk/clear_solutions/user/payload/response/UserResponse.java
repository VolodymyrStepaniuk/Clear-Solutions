package com.stepaniuk.clear_solutions.user.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
@AllArgsConstructor
@Getter
public class UserResponse {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDateTime birthDate;
    private String address;
    private String phoneNumber;
}
