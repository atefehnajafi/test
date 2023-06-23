package com.neshan.springh2project.exception;

public class DuplicateUserNameException extends RuntimeException {
    public DuplicateUserNameException(String str){
        super(str);
    }
}
