package com.stepaniuk.clear_solutions.exceptions;

public class UserLessAgeException extends RuntimeException{
    public UserLessAgeException(){
        super("User must be at least 18 years old");
    }
    public UserLessAgeException(String message){
        super(message);
    }
    public UserLessAgeException(Throwable throwable){
        super(throwable);
    }
}
