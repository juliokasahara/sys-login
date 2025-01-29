package com.syslogin.exeption;

public class UserAlreadyExistException extends RuntimeException{

    public UserAlreadyExistException(){
        super("Email já cadastrado");
    }
    public UserAlreadyExistException(String message){
        super(message);
    }
}
