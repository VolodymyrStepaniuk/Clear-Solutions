package com.clear.solutions.user;

import com.clear.solutions.shared.exception.IllegalDateRangeException;
import com.clear.solutions.testspecific.ServiceLevelUnitTest;
import com.clear.solutions.user.exception.NoSuchUserByIdException;
import com.clear.solutions.user.exception.UserAlreadyExistsByEmailException;
import com.clear.solutions.user.payload.UserCreateRequest;
import com.clear.solutions.user.payload.UserUpdateAllFieldsRequest;
import com.clear.solutions.user.payload.UserUpdateSomeFieldsRequest;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ContextConfiguration;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.AdditionalAnswers.answer;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ServiceLevelUnitTest
@ContextConfiguration(classes = {UserService.class, UserMapperImpl.class})
class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Test
    void shouldReturnUserResponseWhenCreatingUser() {
        // given
        var request = new UserCreateRequest(
                "johndoe@gmail.com", "John", "Doe",
                LocalDate.MAX, "New address","1234567890"
        );

        when(userRepository.save(any())).thenAnswer(answer(getFakeSaveAnswer(1L)));

        // when
        var userResponse = userService.createUser(request);

        assertNotNull(userResponse);
        assertEquals(request.getEmail(), userResponse.getEmail());
        assertEquals(request.getFirstName(), userResponse.getFirstName());
        assertEquals(request.getLastName(), userResponse.getLastName());
        assertEquals(request.getBirthDate(), userResponse.getBirthDate());
        assertEquals(request.getAddress(), userResponse.getAddress());
        assertEquals(request.getPhoneNumber(), userResponse.getPhoneNumber());
        assertTrue(userResponse.hasLinks());
        assertTrue(userResponse.getLinks().hasLink("self"));
        assertTrue(userResponse.getLinks().hasLink("update-some-fields"));
        assertTrue(userResponse.getLinks().hasLink("update-all-fields"));
        assertTrue(userResponse.getLinks().hasLink("delete"));

        verify(userRepository, times(1)).save(any());
    }

    @Test
    void shouldThrowUserAlreadyExistsByEmailExceptionWhenCreatingUserWithExistingEmail() {
        // given
        var request = new UserCreateRequest(
                "johndoe@gmail.com", "John", "Doe",
                LocalDate.MAX, "New address","1234567890"
        );

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        // when && then
        assertThrows(UserAlreadyExistsByEmailException.class,
                () -> userService.createUser(request));
    }

    @Test
    void shouldReturnUserResponseWhenGetByExistingId() {
        // given
        Instant timeOfCreation = Instant.now().plus(Duration.ofHours(10));
        Instant timeOfModification = Instant.now().plus(Duration.ofHours(20));

        User userToFind = new User(1L, "johndoe@gmail.com", "John", "Doe",
                LocalDate.MAX, "New address","1234567890", timeOfCreation, timeOfModification);

        when(userRepository.findById(1L)).thenReturn(Optional.of(userToFind));

        // when
        var userResponse = userService.getUserById(1L);

        // then
        assertNotNull(userResponse);
        assertEquals(userToFind.getEmail(), userResponse.getEmail());
        assertEquals(userToFind.getFirstName(), userResponse.getFirstName());
        assertEquals(userToFind.getLastName(), userResponse.getLastName());
        assertEquals(userToFind.getBirthDate(), userResponse.getBirthDate());
        assertEquals(userToFind.getAddress(), userResponse.getAddress());
        assertEquals(userToFind.getPhoneNumber(), userResponse.getPhoneNumber());
        assertEquals(userToFind.getCreatedAt(), userResponse.getCreatedAt());
        assertEquals(userToFind.getLastModifiedAt(), userResponse.getLastModifiedAt());
        assertTrue(userResponse.hasLinks());
        assertTrue(userResponse.getLinks().hasLink("self"));
        assertTrue(userResponse.getLinks().hasLink("update-some-fields"));
        assertTrue(userResponse.getLinks().hasLink("update-all-fields"));
        assertTrue(userResponse.getLinks().hasLink("delete"));
    }

    @Test
    void shouldThrowNoSuchUserByIdExceptionWhenGetByNonExistingId() {
        // given
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // when && then
        assertThrows(NoSuchUserByIdException.class, () -> userService.getUserById(1L));
    }

    @Test
    void shouldUpdateAndReturnUserResponseWhenChangingFirstName() {
        // given
        var userId = 2L;
        Instant timeOfCreation = Instant.now().plus(Duration.ofHours(10));
        Instant timeOfModification = Instant.now().plus(Duration.ofHours(20));

        User userToUpdate = new User(userId, "johnathandoe@gmail.com", "Johnathan", "Doe",
                LocalDate.MIN, "Address","1234567890", timeOfCreation, timeOfModification);

        UserUpdateSomeFieldsRequest updateRequest = new UserUpdateSomeFieldsRequest(
                "newemail@gmail.com",null, null, null, null, null
        );

        when(userRepository.findById(userId)).thenReturn(Optional.of(userToUpdate));
        when(userRepository.save(any())).thenAnswer(answer(getFakeSaveAnswer(userId)));

        var updatedUser = userService.updateSomeUserFields(userId, updateRequest);

        // then
        assertNotNull(updatedUser);
        assertEquals(updateRequest.getEmail(), updatedUser.getEmail());
        assertEquals(userToUpdate.getFirstName(), updatedUser.getFirstName());
        assertEquals(userToUpdate.getLastName(), updatedUser.getLastName());
        assertEquals(userToUpdate.getBirthDate(), updatedUser.getBirthDate());
        assertEquals(userToUpdate.getAddress(), updatedUser.getAddress());
        assertEquals(userToUpdate.getPhoneNumber(), updatedUser.getPhoneNumber());
        assertEquals(userToUpdate.getCreatedAt(), updatedUser.getCreatedAt());
        assertEquals(userToUpdate.getLastModifiedAt(), updatedUser.getLastModifiedAt());
        assertTrue(updatedUser.hasLinks());
        assertTrue(updatedUser.getLinks().hasLink("self"));
        assertTrue(updatedUser.getLinks().hasLink("update-some-fields"));
        assertTrue(updatedUser.getLinks().hasLink("update-all-fields"));
        assertTrue(updatedUser.getLinks().hasLink("delete"));
    }

    @Test
    void shouldThrowNoSuchUserByIdExceptionWhenUpdatingSomeUserFieldsOfNonExistingUser() {
        UserUpdateSomeFieldsRequest updateRequest = new UserUpdateSomeFieldsRequest(
                "newemail@gmail.com",null, null, null, null, null
        );
        // given
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // when && then
        assertThrows(NoSuchUserByIdException.class, () -> userService.updateSomeUserFields(1L,updateRequest));
    }

    @Test
    void shouldUpdateAndReturnUserResponseWhenChangingAllFields() {
        // given
        var userId = 3L;
        Instant timeOfCreation = Instant.now().plus(Duration.ofHours(10));
        Instant timeOfModification = Instant.now().plus(Duration.ofHours(20));

        User userToUpdate = new User(userId, "johnathandoe@gmail.com", "Johnathan", "Doe",
                LocalDate.MIN, "Address","1234567890", timeOfCreation, timeOfModification);

        UserUpdateAllFieldsRequest updateRequest = new UserUpdateAllFieldsRequest("newemail@gmail.com", "John", "Doe",
                LocalDate.MAX, "New address","1234567890");

        when(userRepository.findById(userId)).thenReturn(Optional.of(userToUpdate));
        when(userRepository.save(any())).thenAnswer(answer(getFakeSaveAnswer(userId)));

        var updatedUser = userService.updateAllUserFields(userId, updateRequest);

        // then
        assertNotNull(updatedUser);
        assertEquals(updateRequest.getEmail(), updatedUser.getEmail());
        assertEquals(updateRequest.getFirstName(), updatedUser.getFirstName());
        assertEquals(updateRequest.getLastName(), updatedUser.getLastName());
        assertEquals(updateRequest.getBirthDate(), updatedUser.getBirthDate());
        assertEquals(updateRequest.getAddress(), updatedUser.getAddress());
        assertEquals(updateRequest.getPhoneNumber(), updatedUser.getPhoneNumber());
        assertEquals(userToUpdate.getCreatedAt(), updatedUser.getCreatedAt());
        assertTrue(updatedUser.hasLinks());
        assertTrue(updatedUser.getLinks().hasLink("self"));
        assertTrue(updatedUser.getLinks().hasLink("update-some-fields"));
        assertTrue(updatedUser.getLinks().hasLink("update-all-fields"));
        assertTrue(updatedUser.getLinks().hasLink("delete"));
    }

    @Test
    void shouldThrowNoSuchUserByIdExceptionWhenUpdatingAllUserFieldsOfNonExistingUser() {
        UserUpdateAllFieldsRequest updateRequest = new UserUpdateAllFieldsRequest("newemail@gmail.com", "John", "Doe",
                LocalDate.MAX, "New address","1234567890");
        // given
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // when && then
        assertThrows(NoSuchUserByIdException.class, () -> userService.updateAllUserFields(1L,updateRequest));
    }

    @Test
    void shouldReturnVoidWhenDeleteUser(){
        //given
        var userId = 3L;
        Instant timeOfCreation = Instant.now().plus(Duration.ofHours(10));
        Instant timeOfModification = Instant.now().plus(Duration.ofHours(20));

        User user = new User(userId, "johnathandoe@gmail.com", "Johnathan", "Doe",
                LocalDate.MIN, "Address","1234567890", timeOfCreation, timeOfModification);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        //when
        userService.deleteUser(1L);
        //then
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void shouldThrowNoSuchUserByIdExceptionWhenDeletingNonExistingUser() {
        // given
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // when && then
        assertThrows(NoSuchUserByIdException.class, () -> userService.deleteUser(1L));
    }

    @Test
    void shouldReturnUserResponseListWhenGettingAllUsersByBirthDateRange() {
        // given
        User firstUser = new User(1L, "johnathandoe@gmail.com", "Johnathan", "Doe",
                LocalDate.of(2004,4,25), "Address","1234567890", Instant.now().plus(Duration.ofHours(10)),
                Instant.now().plus(Duration.ofHours(20)));
        User secondUser = new User(2L, "janedoe@gmail.com", "Jane", "Doe",
                LocalDate.of(2003,5,20), "New Address","1234567899", Instant.now().plus(Duration.ofHours(11)),
                Instant.now().plus(Duration.ofHours(21)));

        var from = LocalDate.of(2003, 1, 1);
        var to = LocalDate.of(2004, 12, 31);

        when(userRepository.findAll(any(Specification.class))).thenReturn(List.of(firstUser, secondUser));

        // when
        var users = userService.getAllUsersByBirthDateRange(from, to);

        // then
        assertNotNull(users);
        assertEquals(2, users.size());

        var actualFirstUser = users.get(0);
        var actualSecondUser = users.get(1);

        assertTrue(actualFirstUser.getBirthDate().isAfter(from));
        assertTrue(actualFirstUser.getBirthDate().isBefore(to));
        assertEquals(firstUser.getEmail(), actualFirstUser.getEmail());
        assertEquals(firstUser.getFirstName(), actualFirstUser.getFirstName());
        assertEquals(firstUser.getLastName(), actualFirstUser.getLastName());
        assertEquals(firstUser.getBirthDate(), actualFirstUser.getBirthDate());
        assertEquals(firstUser.getAddress(), actualFirstUser.getAddress());
        assertEquals(firstUser.getPhoneNumber(), actualFirstUser.getPhoneNumber());
        assertEquals(firstUser.getCreatedAt(), actualFirstUser.getCreatedAt());
        assertEquals(firstUser.getLastModifiedAt(), actualFirstUser.getLastModifiedAt());
        assertTrue(actualFirstUser.hasLinks());
        assertTrue(actualFirstUser.getLinks().hasLink("self"));
        assertTrue(actualFirstUser.getLinks().hasLink("update-some-fields"));
        assertTrue(actualFirstUser.getLinks().hasLink("update-all-fields"));
        assertTrue(actualFirstUser.getLinks().hasLink("delete"));

        assertTrue(actualFirstUser.getBirthDate().isAfter(from));
        assertTrue(actualSecondUser.getBirthDate().isBefore(to));
        assertEquals(secondUser.getEmail(), actualSecondUser.getEmail());
        assertEquals(secondUser.getFirstName(), actualSecondUser.getFirstName());
        assertEquals(secondUser.getLastName(), actualSecondUser.getLastName());
        assertEquals(secondUser.getBirthDate(), actualSecondUser.getBirthDate());
        assertEquals(secondUser.getAddress(), actualSecondUser.getAddress());
        assertEquals(secondUser.getPhoneNumber(), actualSecondUser.getPhoneNumber());
        assertEquals(secondUser.getCreatedAt(), actualSecondUser.getCreatedAt());
        assertEquals(secondUser.getLastModifiedAt(), actualSecondUser.getLastModifiedAt());
        assertTrue(actualSecondUser.hasLinks());
        assertTrue(actualSecondUser.getLinks().hasLink("self"));
        assertTrue(actualSecondUser.getLinks().hasLink("update-some-fields"));
        assertTrue(actualSecondUser.getLinks().hasLink("update-all-fields"));
        assertTrue(actualSecondUser.getLinks().hasLink("delete"));
    }

    @Test
    void shouldThrowIllegalDateRangeExceptionWhenGettingAllUsersByBirthDateRangeWithInvalidRange() {
        // given
        var from = LocalDate.of(2004, 1, 1);
        var to = LocalDate.of(2003, 12, 31);

        // when && then
        assertThrows(IllegalDateRangeException.class, () -> userService.getAllUsersByBirthDateRange(from, to));
    }

    private Answer1<User, User> getFakeSaveAnswer(Long id) {
        return user -> {
            user.setId(id);
            return user;
        };
    }
}
