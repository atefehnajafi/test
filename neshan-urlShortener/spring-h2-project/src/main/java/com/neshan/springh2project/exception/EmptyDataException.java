package com.neshan.springh2project.exception;

public class EmptyDataException extends RuntimeException {
    public EmptyDataException(String str){
        super(str);
    }
}
