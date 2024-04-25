package com.clear.solutions.user.payload;

import com.clear.solutions.validation.user.*;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
@Relation(collectionRelation = "users", itemRelation = "user")
public class UserResponse extends RepresentationModel<UserResponse> {

    @Id
    @NotNull
    private Long id;

    @Email
    @NotNull
    private String email;

    @FirstName
    @NotNull
    private String firstName;

    @LastName
    @NotNull
    private String lastName;

    @Age
    @NotNull
    private LocalDate birthDate;

    @Nullable
    private String address;

    @Phone
    @Nullable
    private String phoneNumber;

    @NotNull
    private final Instant createdAt;

    @NotNull
    private final Instant lastModifiedAt;
}
