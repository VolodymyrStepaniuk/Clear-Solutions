package com.clear.solutions.user.payload;

import com.clear.solutions.validation.user.*;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class UserCreateRequest {
    @NotNull
    @Email
    String email;

    @NotNull
    @FirstName
    String firstName;

    @NotNull
    @LastName
    String lastName;

    @NotNull
    @Age
    LocalDate birthDate;

    @Nullable
    String address;

    @Nullable
    @Phone
    String phoneNumber;
}
