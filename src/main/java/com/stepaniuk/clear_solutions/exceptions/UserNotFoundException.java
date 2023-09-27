package com.stepaniuk.clear_solutions.exceptions;


public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(){
        super("No user found");
    }
    public UserNotFoundException(String message){
        super(message);
    }
    public UserNotFoundException(Throwable throwable){
        super(throwable);
    }
}