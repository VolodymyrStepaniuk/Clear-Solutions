package com.clear.solutions.user;

import com.clear.solutions.shared.exception.IllegalDateRangeException;
import com.clear.solutions.user.exception.NoSuchUserByIdException;
import com.clear.solutions.user.exception.UserAlreadyExistsByEmailException;
import com.clear.solutions.user.payload.UserCreateRequest;
import com.clear.solutions.user.payload.UserResponse;
import com.clear.solutions.user.payload.UserUpdateAllFieldsRequest;
import com.clear.solutions.user.payload.UserUpdateSomeFieldsRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public UserResponse createUser(UserCreateRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsByEmailException(request.getEmail());
        }

        User user = new User();

        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setBirthDate(request.getBirthDate());
        user.setAddress(request.getAddress());
        user.setPhoneNumber(request.getPhoneNumber());

        var savedUser = userRepository.save(user);

        return userMapper.toResponse(savedUser);
    }

    public UserResponse getUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toResponse)
                .orElseThrow(() -> new NoSuchUserByIdException(id));
    }

    @Transactional
    public UserResponse updateSomeUserFields(Long userId, UserUpdateSomeFieldsRequest updateRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchUserByIdException(userId));

        if (updateRequest.getEmail() != null) {
            user.setEmail(updateRequest.getEmail());
        }
        if (updateRequest.getFirstName() != null) {
            user.setFirstName(updateRequest.getFirstName());
        }
        if (updateRequest.getLastName() != null) {
            user.setLastName(updateRequest.getLastName());
        }
        if (updateRequest.getBirthDate() != null) {
            user.setBirthDate(updateRequest.getBirthDate());
        }
        if (updateRequest.getAddress() != null) {
            user.setAddress(updateRequest.getAddress());
        }
        if (updateRequest.getPhoneNumber() != null) {
            user.setPhoneNumber(updateRequest.getPhoneNumber());
        }

        var updatedUser = userRepository.save(user);

        return userMapper.toResponse(updatedUser);
    }

    @Transactional
    public UserResponse updateAllUserFields(Long userId, UserUpdateAllFieldsRequest updateRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchUserByIdException(userId));

        user.setEmail(updateRequest.getEmail());
        user.setFirstName(updateRequest.getFirstName());
        user.setLastName(updateRequest.getLastName());
        user.setBirthDate(updateRequest.getBirthDate());

        if (updateRequest.getAddress() != null)
            user.setAddress(updateRequest.getAddress());
        if (updateRequest.getPhoneNumber() != null)
            user.setPhoneNumber(updateRequest.getPhoneNumber());

        var updatedUser = userRepository.save(user);

        return userMapper.toResponse(updatedUser);
    }

    @Transactional
    public void deleteUser(Long userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchUserByIdException(userId));

        userRepository.delete(user);
    }

    public List<UserResponse> getAllUsersByBirthDateRange(LocalDate from, LocalDate to) {
        if (from.isAfter(to)) {
            throw new IllegalDateRangeException(from, to);
        }

        Specification<User> birthDateRangeSpecification = (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("birthDate"), from, to);

        var pageOfUsers = userRepository.findAll(birthDateRangeSpecification);

        return pageOfUsers.stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }
}
