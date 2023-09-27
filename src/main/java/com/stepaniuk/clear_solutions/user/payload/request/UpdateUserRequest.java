package com.stepaniuk.clear_solutions.user.payload.request;

import com.stepaniuk.clear_solutions.user.validation.Age;
import jakarta.validation.constraints.Pattern;
import lombok.Value;

import java.time.LocalDate;

@Value
public class UpdateUserRequest {
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$")
    String email;
    String firstName;
    String lastName;
    @Age LocalDate birthDate;
    String address;
    @Pattern(regexp = "^\\d+$")
    String phoneNumber;
}
