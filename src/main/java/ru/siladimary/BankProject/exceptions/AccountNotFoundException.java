package ru.siladimary.BankProject.exceptions;

public class AccountNotFoundException extends RuntimeException{
    public AccountNotFoundException(String msg){
        super(msg);
    }
}
