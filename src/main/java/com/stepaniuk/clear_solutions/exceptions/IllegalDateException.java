package com.stepaniuk.clear_solutions.exceptions;

public class IllegalDateException extends RuntimeException {
    public IllegalDateException(){
        super("From Date must be before To Date");
    }
    public IllegalDateException(String message){
        super(message);
    }
    public IllegalDateException(Throwable throwable){
        super(throwable);
    }
}
