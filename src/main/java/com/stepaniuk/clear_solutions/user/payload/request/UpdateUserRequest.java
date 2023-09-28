package com.stepaniuk.clear_solutions.user.payload.request;

import com.stepaniuk.clear_solutions.user.validation.Age;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.time.LocalDate;

@Value
public class UpdateUserRequest {
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$")
    String email;
    @Size(min = 1,max = 50) String firstName;
    @Size(min = 1,max = 50) String lastName;
    @Age LocalDate birthDate;
    String address;
    String phoneNumber;
}
