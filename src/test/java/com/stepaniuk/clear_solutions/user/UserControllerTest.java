package com.stepaniuk.clear_solutions.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stepaniuk.clear_solutions.user.payload.request.CreateUserRequest;
import com.stepaniuk.clear_solutions.user.payload.request.UpdateUserRequest;
import com.stepaniuk.clear_solutions.user.payload.response.UserResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {
    private final String rootURL = "/api/v1/user";
    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("Should create user and return 201 status")
    @Test
    void createUserTest() throws Exception {
        UserResponse user = new UserResponse(1L,"volodymyr.stepaniuk.04@gmail.com","Volodymyr","Stepaniuk",
                LocalDate.of(2004,5,20),"Ukraine, Lviv","+380683006791");

        when(userService.create(any(CreateUserRequest.class))).thenReturn(user);

            mockMvc.perform(MockMvcRequestBuilders
                            .post(rootURL)
                            .content(objectMapper.writeValueAsString(user))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated());

        verify(userService, times(1)).create(any(CreateUserRequest.class));
    }

    @DisplayName("Should return bad request when create user with invalid fields")
    @Test
    void testShouldReturnBadRequestWhenCreateUserWithInvalidFields() throws Exception {
        CreateUserRequest request = new CreateUserRequest("volodymyr.stepaniuk.04gmail.com","","",
                LocalDate.of(2022,5,20),"Ukraine, Lviv","+380683006791");

        mockMvc.perform(MockMvcRequestBuilders
                        .post(rootURL)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Should return ok when update birth date field in this case")
    @Test
    void testShouldReturnOkWhenUpdateBirthDate() throws Exception {
        long id = 1L;
        UpdateUserRequest request = new UpdateUserRequest(null,null,null,
                LocalDate.of(2004,5,20),null,null);

        mockMvc.perform(MockMvcRequestBuilders
                        .patch(rootURL + "/"+ id)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService, times(1)).updateSomeFields(anyLong(), any(UpdateUserRequest.class));
    }

    @DisplayName("Should return ok when update all fields")
    @Test
    void testShouldReturnOkWhenUpdateAllFields() throws Exception {
        long id = 1L;
        UpdateUserRequest request = new UpdateUserRequest("volodymyr.stepaniuk.04@gmail.com", "Volodymyr", "Stepaniuk",
                LocalDate.of(2004, 5, 20), "Ukraine, Lviv", "+380683006791");
        mockMvc.perform(MockMvcRequestBuilders
                        .put(rootURL + "/"+ id)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(userService, times(1)).updateAllFields(anyLong(), any(UpdateUserRequest.class));
    }

    @DisplayName("Should return no content when delete user")
    @Test
    void testDeleteUserShouldReturnNothing() throws Exception {
        long id = 1L;
        mockMvc.perform(MockMvcRequestBuilders
                        .delete(rootURL + "/"+ id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        verify(userService, times(1)).deleteById(anyLong());
    }

    @DisplayName("Should return unsupported media type when delete user if content type is not json")
    @Test
    void testDeleteUserShouldReturnUnsupportedMediaType() throws Exception {
        long id = 1L;
        mockMvc.perform(MockMvcRequestBuilders
                        .delete(rootURL + "/"+ id)
                        .contentType(MediaType.APPLICATION_XML))
                .andExpect(status().isUnsupportedMediaType());
        verify(userService, times(0)).deleteById(anyLong());
    }

    @DisplayName("Should return ok when get list by date between")
    @Test
    void testShouldReturnOkWhenGetListByDateBetween() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get(rootURL)
                        .param("from", "2021-01-01")
                        .param("to", "2021-12-31")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(userService, times(1)).getListByDateBetween(any(LocalDate.class), any(LocalDate.class));
    }
}