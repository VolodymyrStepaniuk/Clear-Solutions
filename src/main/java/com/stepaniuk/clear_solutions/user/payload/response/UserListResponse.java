package com.stepaniuk.clear_solutions.user.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

import java.util.List;


@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserListResponse {
    @JsonProperty("content")
    List<UserResponse> content;
    @JsonProperty("size")
    int size;
}
