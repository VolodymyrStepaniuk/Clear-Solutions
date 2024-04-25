package com.clear.solutions.user;

import com.clear.solutions.testspecific.ControllerLevelUnitTest;
import com.clear.solutions.user.exception.NoSuchUserByIdException;
import com.clear.solutions.user.exception.UserAlreadyExistsByEmailException;
import com.clear.solutions.user.payload.UserCreateRequest;
import com.clear.solutions.user.payload.UserResponse;
import com.clear.solutions.user.payload.UserUpdateAllFieldsRequest;
import com.clear.solutions.user.payload.UserUpdateSomeFieldsRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.Link;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import static com.clear.solutions.testspecific.hamcrest.TemporalStringMatchers.instantComparesEqualTo;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

@ControllerLevelUnitTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test

    void shouldReturnUserResponseWhenCreatingUser() throws Exception {
        // given
        var timeOfCreationAndModification = Instant.now();
        var response = new UserResponse(1L, "johndoe@gmail.com", "John", "Doe",
                LocalDate.of(2004,5,20), "New address","1234567890", timeOfCreationAndModification, timeOfCreationAndModification);
        response.add(Link.of("http://localhost/users/1", "self"));
        response.add(Link.of("http://localhost/users/1", "update-all-fields"));
        response.add(Link.of("http://localhost/users/1", "update-some-fields"));
        response.add(Link.of("http://localhost/users/1", "delete"));

        var request = new UserCreateRequest(
                "johndoe@gmail.com", "John", "Doe",
                LocalDate.of(2004,5,20), "New address","1234567890"
        );

        when(userService.createUser(request)).thenReturn(response);

        // when
        mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", is(response.getId()), Long.class))
        .andExpect(jsonPath("$.email", is(response.getEmail())))
        .andExpect(jsonPath("$.firstName", is(response.getFirstName())))
        .andExpect(jsonPath("$.lastName", is(response.getLastName())))
        .andExpect(jsonPath("$.birthDate", is(response.getBirthDate().toString())))
        .andExpect(jsonPath("$.address", is(response.getAddress())))
        .andExpect(jsonPath("$.phoneNumber", is(response.getPhoneNumber())))
        .andExpect(jsonPath("$.createdAt", instantComparesEqualTo(response.getCreatedAt())))
        .andExpect(jsonPath("$.lastModifiedAt", instantComparesEqualTo(response.getLastModifiedAt())))
        .andExpect(jsonPath("$._links.self.href", is("http://localhost/users/1")))
        .andExpect(jsonPath("$._links.update-all-fields.href", is("http://localhost/users/1")))
        .andExpect(jsonPath("$._links.update-some-fields.href", is("http://localhost/users/1")))
        .andExpect(jsonPath("$._links.delete.href", is("http://localhost/users/1")));
    }

    @Test
    void shouldReturnErrorResponseWhenCreatingUserWithExistingEmail() throws Exception {
        // given
        var request = new UserCreateRequest(
                "johndoe@gmail.com", "John", "Doe",
                LocalDate.of(2004,5,20), "New address","1234567890"
        );

        when(userService.createUser(request)).thenThrow(new UserAlreadyExistsByEmailException(request.getEmail()));

        // when
        mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.status", is(409)))
        .andExpect(jsonPath("$.title", is("User already exists")))
        .andExpect(jsonPath("$.detail", is("User with email "+request.getEmail()+" already exists")))
        .andExpect(jsonPath("$.instance", is("/users")));
    }


    @Test
    void shouldReturnUserResponseWhenGetUserById() throws Exception {
        // given
        var timeOfCreationAndModification = Instant.now();
        var response = new UserResponse(1L, "johndoe@gmail.com", "John", "Doe",
                LocalDate.of(2004,5,20), "New address","1234567890", timeOfCreationAndModification, timeOfCreationAndModification);
        response.add(Link.of("http://localhost/users/1", "self"));
        response.add(Link.of("http://localhost/users/1", "update-all-fields"));
        response.add(Link.of("http://localhost/users/1", "update-some-fields"));
        response.add(Link.of("http://localhost/users/1", "delete"));

        when(userService.getUserById(1L)).thenReturn(response);
        mockMvc.perform(get("/users/1")
                        .contentType("application/json")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(response.getId()), Long.class))
                .andExpect(jsonPath("$.email", is(response.getEmail())))
                .andExpect(jsonPath("$.firstName", is(response.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(response.getLastName())))
                .andExpect(jsonPath("$.birthDate", is(response.getBirthDate().toString())))
                .andExpect(jsonPath("$.address", is(response.getAddress())))
                .andExpect(jsonPath("$.phoneNumber", is(response.getPhoneNumber())))
                .andExpect(jsonPath("$.createdAt", instantComparesEqualTo(response.getCreatedAt())))
                .andExpect(jsonPath("$.lastModifiedAt", instantComparesEqualTo(response.getLastModifiedAt())))
                .andExpect(jsonPath("$._links.self.href", is("http://localhost/users/1")))
                .andExpect(jsonPath("$._links.update-all-fields.href", is("http://localhost/users/1")))
                .andExpect(jsonPath("$._links.update-some-fields.href", is("http://localhost/users/1")))
                .andExpect(jsonPath("$._links.delete.href", is("http://localhost/users/1")));
    }

    @Test
    void shouldReturnErrorResponseWhenGetUserByIdAndNoSuchUserByIdException() throws Exception {
        // given
        when(userService.getUserById(1L)).thenThrow(new NoSuchUserByIdException(1L));
        // when
        mockMvc.perform(get("/users/1")
                        .contentType("application/json")
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.title", is("No such user")))
                .andExpect(jsonPath("$.detail", is("No user with id 1 found")))
                .andExpect(jsonPath("$.instance", is("/users/1")));
    }

    @Test
    void shouldReturnUserResponseWhenUpdateSomeUserFields() throws Exception {
        // given
        var timeOfCreationAndModification = Instant.now();
        var response = new UserResponse(1L, "johndoe@gmail.com", "John", "Doe",
                LocalDate.of(2004,5,20), "New address","1234567890", timeOfCreationAndModification, timeOfCreationAndModification);
        response.add(Link.of("http://localhost/users/1", "self"));
        response.add(Link.of("http://localhost/users/1", "update-all-fields"));
        response.add(Link.of("http://localhost/users/1", "update-some-fields"));
        response.add(Link.of("http://localhost/users/1", "delete"));

        var request = new UserUpdateSomeFieldsRequest(null,null,null,null,"New address",null);

        when(userService.updateSomeUserFields(1L, request)).thenReturn(response);

        mockMvc.perform(patch("/users/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(response.getId()), Long.class))
                .andExpect(jsonPath("$.email", is(response.getEmail())))
                .andExpect(jsonPath("$.firstName", is(response.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(response.getLastName())))
                .andExpect(jsonPath("$.birthDate", is(response.getBirthDate().toString())))
                .andExpect(jsonPath("$.address", is(response.getAddress())))
                .andExpect(jsonPath("$.phoneNumber", is(response.getPhoneNumber())))
                .andExpect(jsonPath("$.createdAt", instantComparesEqualTo(response.getCreatedAt())))
                .andExpect(jsonPath("$.lastModifiedAt", instantComparesEqualTo(response.getLastModifiedAt())))
                .andExpect(jsonPath("$._links.self.href", is("http://localhost/users/1")))
                .andExpect(jsonPath("$._links.update-all-fields.href", is("http://localhost/users/1")))
                .andExpect(jsonPath("$._links.update-some-fields.href", is("http://localhost/users/1")))
                .andExpect(jsonPath("$._links.delete.href", is("http://localhost/users/1")));
    }

    @Test
    void shouldReturnErrorResponseWhenUpdateSomeUserFieldsAndNoSuchUserByIdException() throws Exception {
        // given
        var request = new UserUpdateSomeFieldsRequest(null,null,null,null,"New address",null);

        when(userService.updateSomeUserFields(1L, request)).thenThrow(new NoSuchUserByIdException(1L));
        // when
        mockMvc.perform(patch("/users/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.title", is("No such user")))
                .andExpect(jsonPath("$.detail", is("No user with id 1 found")))
                .andExpect(jsonPath("$.instance", is("/users/1")));
    }

    @Test
    void shouldReturnUserResponseWhenUpdateAllUserFields() throws Exception {
        // given
        var timeOfCreationAndModification = Instant.now();
        var response = new UserResponse(1L, "johndoe@gmail.com", "John", "Doe",
                LocalDate.of(2004,5,20), "New address","1234567890", timeOfCreationAndModification, timeOfCreationAndModification);
        response.add(Link.of("http://localhost/users/1", "self"));
        response.add(Link.of("http://localhost/users/1", "update-all-fields"));
        response.add(Link.of("http://localhost/users/1", "update-some-fields"));
        response.add(Link.of("http://localhost/users/1", "delete"));

        var request = new UserUpdateAllFieldsRequest("newemail@gmail.com","New John","New Doe",
                LocalDate.of(2004,5,20),"New address","1234567890");

        when(userService.updateAllUserFields(1L, request)).thenReturn(response);

        mockMvc.perform(put("/users/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(response.getId()), Long.class))
                .andExpect(jsonPath("$.email", is(response.getEmail())))
                .andExpect(jsonPath("$.firstName", is(response.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(response.getLastName())))
                .andExpect(jsonPath("$.birthDate", is(response.getBirthDate().toString())))
                .andExpect(jsonPath("$.address", is(response.getAddress())))
                .andExpect(jsonPath("$.phoneNumber", is(response.getPhoneNumber())))
                .andExpect(jsonPath("$.createdAt", instantComparesEqualTo(response.getCreatedAt())))
                .andExpect(jsonPath("$.lastModifiedAt", instantComparesEqualTo(response.getLastModifiedAt())))
                .andExpect(jsonPath("$._links.self.href", is("http://localhost/users/1")))
                .andExpect(jsonPath("$._links.update-all-fields.href", is("http://localhost/users/1")))
                .andExpect(jsonPath("$._links.update-some-fields.href", is("http://localhost/users/1")))
                .andExpect(jsonPath("$._links.delete.href", is("http://localhost/users/1")));
    }

    @Test
    void shouldReturnErrorResponseWhenUpdateAllUserFieldsAndNoSuchUserByIdException() throws Exception {
        // given
        var request = new UserUpdateAllFieldsRequest("newemail@gmail.com","New John","New Doe",
                LocalDate.of(2004,5,20),"New address","1234567890");

        when(userService.updateAllUserFields(1L, request)).thenThrow(new NoSuchUserByIdException(1L));
        // when
        mockMvc.perform(put("/users/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.title", is("No such user")))
                .andExpect(jsonPath("$.detail", is("No user with id 1 found")))
                .andExpect(jsonPath("$.instance", is("/users/1")));
    }

    @Test
    void shouldReturnNoContentWhenDeleteUser() throws Exception {
        mockMvc.perform(delete("/users/1")
                        .contentType("application/json")
                )
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnNotFoundWhenDeleteNonExistingUser() throws Exception {
        doThrow(new NoSuchUserByIdException(1L)).when(userService).deleteUser(1L);

        mockMvc.perform(delete("/users/1")
                        .contentType("application/json")
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.title", is("No such user")))
                .andExpect(jsonPath("$.detail", is("No user with id 1 found")))
                .andExpect(jsonPath("$.instance", is("/users/1")));
    }

    @Test
    void shouldReturnListOfUsersByBirthDateRange() throws Exception {
        // given
        var timeOfCreationAndModification = Instant.now();
        var response = new UserResponse(1L, "johndoe@gmail.com", "John", "Doe",
                LocalDate.of(2004, 5, 20), "New address", "1234567890", timeOfCreationAndModification, timeOfCreationAndModification);
        response.add(Link.of("http://localhost/users/1", "self"));
        response.add(Link.of("http://localhost/users/1", "update-all-fields"));
        response.add(Link.of("http://localhost/users/1", "update-some-fields"));
        response.add(Link.of("http://localhost/users/1", "delete"));

        when(userService.getAllUsersByBirthDateRange(LocalDate.of(2003, 5, 20), LocalDate.of(2005, 5, 20)))
                .thenReturn(List.of(response));

        mockMvc.perform(get("/users")
                        .contentType("application/json")
                        .param("from", "2003-05-20")
                        .param("to", "2005-05-20")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(response.getId()), Long.class))
                .andExpect(jsonPath("$[0].email", is(response.getEmail())))
                .andExpect(jsonPath("$[0].firstName", is(response.getFirstName())))
                .andExpect(jsonPath("$[0].lastName", is(response.getLastName())))
                .andExpect(jsonPath("$[0].birthDate", is(response.getBirthDate().toString())))
                .andExpect(jsonPath("$[0].address", is(response.getAddress())))
                .andExpect(jsonPath("$[0].phoneNumber", is(response.getPhoneNumber())))
                .andExpect(jsonPath("$[0].createdAt", instantComparesEqualTo(response.getCreatedAt())))
                .andExpect(jsonPath("$[0].lastModifiedAt", instantComparesEqualTo(response.getLastModifiedAt())))
                .andExpect(jsonPath("$[0].links[0].href", is("http://localhost/users/1")))
                .andExpect(jsonPath("$[0].links[1].href", is("http://localhost/users/1")))
                .andExpect(jsonPath("$[0].links[2].href", is("http://localhost/users/1")))
                .andExpect(jsonPath("$[0].links[3].href", is("http://localhost/users/1")));
    }
}
