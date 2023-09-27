package com.stepaniuk.clear_solutions.user;

import com.stepaniuk.clear_solutions.shared.InstantMapper;
import com.stepaniuk.clear_solutions.user.payload.response.UserResponse;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = {InstantMapper.class})
public interface UserMapper {
        UserResponse toResponse(User user);
}
