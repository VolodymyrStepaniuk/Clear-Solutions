package com.stepaniuk.clear_solutions.user.payload.request;

import com.stepaniuk.clear_solutions.user.validation.Age;
import jakarta.validation.constraints.Pattern;
import lombok.Value;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
@Value
public class CreateUserRequest {
    @NotNull @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$")
    String email;
    @NotNull String firstName;
    @NotNull String lastName;
    @NotNull @Age LocalDate birthDate;
    String address;
    @Pattern(regexp = "^\\d+$") String phoneNumber;
}
