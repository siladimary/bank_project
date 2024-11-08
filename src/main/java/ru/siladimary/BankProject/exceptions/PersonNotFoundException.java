package ru.siladimary.BankProject.exceptions;

public class PersonNotFoundException extends RuntimeException{
    public PersonNotFoundException(String msg){
        super(msg);
    }
}
