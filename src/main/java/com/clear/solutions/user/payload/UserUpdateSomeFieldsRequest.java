package com.clear.solutions.user.payload;

import com.clear.solutions.validation.user.*;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class UserUpdateSomeFieldsRequest {
    @Email
    @Nullable
    String email;

    @FirstName
    @Nullable
    String firstName;

    @LastName
    @Nullable
    String lastName;

    @Age
    @Nullable
    LocalDate birthDate;

    @Nullable
    String address;

    @Nullable
    @Phone
    String phoneNumber;
}
