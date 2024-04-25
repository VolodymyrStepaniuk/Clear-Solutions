package com.clear.solutions.user;

import com.clear.solutions.user.payload.UserCreateRequest;
import com.clear.solutions.user.payload.UserResponse;
import com.clear.solutions.user.payload.UserUpdateAllFieldsRequest;
import com.clear.solutions.user.payload.UserUpdateSomeFieldsRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users", produces = "application/json")
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody @Valid UserCreateRequest request) {
        return new ResponseEntity<>(userService.createUser(request), HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserResponse> updateSomeUserFields(@PathVariable Long userId, @RequestBody @Valid UserUpdateSomeFieldsRequest request) {
        return ResponseEntity.ok(userService.updateSomeUserFields(userId, request));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserResponse> updateAllUserFields(@PathVariable Long userId, @RequestBody @Valid UserUpdateAllFieldsRequest request) {
        return ResponseEntity.ok(userService.updateAllUserFields(userId, request));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getListByDateBetween(@RequestParam("from")
                                                                   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                                   LocalDate fromDate,
                                                                   @RequestParam("to")
                                                                   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                                   LocalDate toDate) {
        return ResponseEntity.ok(userService.getAllUsersByBirthDateRange(fromDate, toDate));
    }
}
