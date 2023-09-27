package com.stepaniuk.clear_solutions.user;

import com.stepaniuk.clear_solutions.user.payload.request.CreateUserRequest;
import com.stepaniuk.clear_solutions.user.payload.request.UpdateUserRequest;
import com.stepaniuk.clear_solutions.user.payload.response.UserListResponse;
import com.stepaniuk.clear_solutions.user.payload.response.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping(value = "api/v1/user",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestBody @Valid CreateUserRequest request) {
        return new ResponseEntity<>(service.create(request), HttpStatus.CREATED);
    }
    @PatchMapping("/{userId}")
    public ResponseEntity<UserResponse> updateSomeUserFields(@PathVariable Long userId, @RequestBody @Valid UpdateUserRequest updatedFields) {
        return ResponseEntity.ok(service.updateSomeFields(userId, updatedFields));
    }
    @PutMapping("/{userId}")
    public ResponseEntity<UserResponse> updateUserAllFields(@PathVariable Long userId, @RequestBody @Valid UpdateUserRequest updatedUser) {
        return ResponseEntity.ok(service.updateAllFields(userId, updatedUser));
    }
    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> delete(@PathVariable Long userId) {
        service.deleteById(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<UserListResponse> getListByDateBetween(@RequestParam("from")
                                                                 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                                 LocalDate fromDate,
                                                             @RequestParam("to")
                                                                 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                                 LocalDate toDate) {
        return ResponseEntity.ok(service.getListByDateBetween(fromDate, toDate));
    }
}
