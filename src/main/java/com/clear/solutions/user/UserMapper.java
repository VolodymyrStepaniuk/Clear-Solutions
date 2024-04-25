package com.clear.solutions.user;

import com.clear.solutions.user.payload.UserResponse;
import org.mapstruct.*;
import org.springframework.hateoas.Link;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper {

    @BeanMapping(qualifiedByName = "addLinks")
    UserResponse toResponse(User user);

    @AfterMapping
    @Named("addLinks")
    default UserResponse addLinks(User user, @MappingTarget UserResponse response) {
        response.add(Link.of("/users/" + user.getId()).withSelfRel());
        response.add(Link.of("/users/" + user.getId()).withRel("update-all-fields"));
        response.add(Link.of("/users/" + user.getId()).withRel("update-some-fields"));
        response.add(Link.of("/users/" + user.getId()).withRel("delete"));

        return response;
    }
}
