package com.clear.solutions.shared;

import com.clear.solutions.user.exception.NoSuchUserByIdException;
import com.clear.solutions.user.exception.UserAlreadyExistsByEmailException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice
public class GeneralControllerExceptionHandler {

    @ExceptionHandler(value = {NoSuchUserByIdException.class})
    public ProblemDetail handleNoSuchUserByIdException(NoSuchUserByIdException e) {
        var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,
                "No user with id " + e.getId() + " found");
        problemDetail.setTitle("No such user");
        problemDetail.setInstance(URI.create("/users/" + e.getId()));
        return problemDetail;
    }

    @ExceptionHandler(value = {UserAlreadyExistsByEmailException.class})
    public ProblemDetail handleUserAlreadyExistsByEmailException(UserAlreadyExistsByEmailException e) {
        var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT,
                "User with email " + e.getEmail() + " already exists");
        problemDetail.setTitle("User already exists");
        return problemDetail;
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                "Validation failed: " + e.getMessage());
        problemDetail.setTitle("Validation failed");
        return problemDetail;
    }
}
