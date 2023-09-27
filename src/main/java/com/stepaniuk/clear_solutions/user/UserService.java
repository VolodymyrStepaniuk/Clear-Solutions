package com.stepaniuk.clear_solutions.user;

import com.stepaniuk.clear_solutions.exceptions.UserNotFoundException;
import com.stepaniuk.clear_solutions.user.payload.request.CreateUserRequest;
import com.stepaniuk.clear_solutions.user.payload.request.UpdateUserRequest;
import com.stepaniuk.clear_solutions.user.payload.response.UserListResponse;
import com.stepaniuk.clear_solutions.user.payload.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final UserMapper mapper;
    public UserResponse create(CreateUserRequest request) {
        User user = new User();

        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setBirthDate(request.getBirthDate());
        user.setAddress(request.getAddress());
        user.setPhoneNumber(request.getPhoneNumber());

        return mapper.toResponse(repository.save(user));
    }

    public void deleteById(Long userId) {
        repository.delete(getById(userId));
    }

    private User getById(Long userId) {
        return repository.findById(userId).orElseThrow(UserNotFoundException::new);
    }

    public UserResponse updateAllFields(Long userId, UpdateUserRequest updatedUser) {
        User user = getById(userId);

        user.setEmail(updatedUser.getEmail());
        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setBirthDate(updatedUser.getBirthDate());
        user.setAddress(updatedUser.getAddress());
        user.setPhoneNumber(updatedUser.getPhoneNumber());

        return mapper.toResponse(repository.save(user));
    }

    public UserResponse updateSomeFields(Long userId, UpdateUserRequest updatedFields) {
        User user = getById(userId);

        if (updatedFields.getEmail() != null) {
            user.setEmail(updatedFields.getEmail());
        }
        if (updatedFields.getFirstName() != null) {
            user.setFirstName(updatedFields.getFirstName());
        }
        if (updatedFields.getLastName() != null) {
            user.setLastName(updatedFields.getLastName());
        }
        if (updatedFields.getBirthDate() != null) {
            user.setBirthDate(updatedFields.getBirthDate());
        }
        if (updatedFields.getAddress() != null) {
            user.setAddress(updatedFields.getAddress());
        }
        if (updatedFields.getPhoneNumber() != null) {
            user.setPhoneNumber(updatedFields.getPhoneNumber());
        }

        return mapper.toResponse(repository.save(user));
    }

    public UserListResponse getListByDateBetween(LocalDate fromDate, LocalDate toDate) {
        var listOfUsers = repository.findAllByBirthDateBetween(fromDate, toDate).stream().map(mapper::toResponse).toList();
        return new UserListResponse(listOfUsers, listOfUsers.size());
    }
}
