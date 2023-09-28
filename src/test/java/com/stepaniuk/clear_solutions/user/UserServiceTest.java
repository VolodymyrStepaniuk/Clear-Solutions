package com.stepaniuk.clear_solutions.user;

import com.stepaniuk.clear_solutions.exceptions.UserNotFoundException;
import com.stepaniuk.clear_solutions.shared.InstantMapperImpl;
import com.stepaniuk.clear_solutions.user.payload.request.CreateUserRequest;
import com.stepaniuk.clear_solutions.user.payload.request.UpdateUserRequest;
import com.stepaniuk.clear_solutions.user.payload.response.UserListResponse;
import com.stepaniuk.clear_solutions.user.payload.response.UserResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.AdditionalAnswers.answer;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = {UserMapperImpl.class, InstantMapperImpl.class,UserService.class})
class UserServiceTest {
    @Autowired
    private UserService userService;
    @MockBean
    private UserRepository userRepository;
    @DisplayName("Should create user")
    @Test
    void testCreateUserShouldReturnUserResponse() {
        CreateUserRequest request = new CreateUserRequest("volodymyr.stepaniuk.04gmail.com","Volodymyr","Stepaniuk",
                LocalDate.of(2004,5,20),"Ukraine, Lviv","+380683006791");


        when(userRepository.save(any())).thenAnswer(answer(getFakeSave(1L)));
        UserResponse response = userService.create(request);

        assertEquals(request.getEmail(), response.getEmail());
        assertEquals(request.getFirstName(), response.getFirstName());
        assertEquals(request.getLastName(), response.getLastName());
        assertEquals(request.getBirthDate(), response.getBirthDate());
        assertEquals(request.getAddress(), response.getAddress());
        assertEquals(request.getPhoneNumber(), response.getPhoneNumber());
    }

    private Answer1<User, User> getFakeSave(long id) {
        return user -> {
            user.setId(id);
            return user;
        };
    }
    @DisplayName("Should delete user by id")
    @Test
    void deleteById() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.deleteById(userId);

        verify(userRepository, times(1)).delete(user);
    }
    @DisplayName("Should update all fields")
    @Test
    void testUpdateAllFields() {
        Long userId = 1L;
        UpdateUserRequest request = new UpdateUserRequest("volodymyr.stepaniuk.04gmail.com","Volodymyr","Stepaniuk",
                LocalDate.of(2004,5,20),"Ukraine, Lviv","+380683006791");

        User user = getNewUserWithAllFields(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenAnswer(answer(getFakeSave(userId)));

        UserResponse response = userService.updateAllFields(userId,request);

        assertEquals(userId, response.getId());
        assertEquals(request.getEmail(), response.getEmail());
        assertEquals(request.getFirstName(), response.getFirstName());
        assertEquals(request.getLastName(), response.getLastName());
        assertEquals(request.getBirthDate(), response.getBirthDate());
        assertEquals(request.getAddress(), response.getAddress());
        assertEquals(request.getPhoneNumber(), response.getPhoneNumber());
    }

    private static User getNewUserWithAllFields(Long id) {
        User user = new User();
        user.setId(id);
        user.setEmail("volodymyr.stepaniuk.04gmail.com");
        user.setFirstName("Volodymyr");
        user.setLastName("Stepaniuk");
        user.setBirthDate(LocalDate.of(2004,5,20));
        user.setAddress("Ukraine, Lviv");
        user.setPhoneNumber("+380683006791");
        return user;
    }
    @DisplayName("Should update email field")
    @Test
    void testUpdateEmailField() {
        Long userId = 1L;
        UpdateUserRequest request = new UpdateUserRequest("volodymyr.stepaniuk.04gmail.com",
                null,null,null,null,null);

        User user = getNewUserWithAllFields(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenAnswer(answer(getFakeSave(userId)));

        UserResponse response = userService.updateSomeFields(userId,request);

        assertEquals(userId, response.getId());
        assertEquals(request.getEmail(), response.getEmail());
    }
    @DisplayName("Should get list by date between")
    @Test
    void testGetListByDateBetween() {
        LocalDate fromDate = LocalDate.of(2004,5,10);
        LocalDate toDate = LocalDate.of(2004,5,30);

        User fakeUser = getNewUserWithAllFields(1L);
        when(userRepository.findAllByBirthDateBetween(fromDate,toDate)).thenReturn(java.util.List.of(fakeUser));

        UserListResponse response = userService.getListByDateBetween(fromDate,toDate);

        assertEquals(1,response.getContent().size());
        assertEquals(fakeUser.getId(),response.getContent().get(0).getId());
    }
}