package ru.siladimary.BankProject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.siladimary.BankProject.security.PersonDetails;
import ru.siladimary.BankProject.services.AccountsService;
import ru.siladimary.BankProject.services.PeopleService;

@RestController
@RequestMapping("/account")
public class AccountController {
    private final PeopleService peopleService;
    private final AccountsService accountsService;

    @Autowired
    public AccountController(PeopleService peopleService, AccountsService accountsService) {
        this.peopleService = peopleService;
        this.accountsService = accountsService;
    }

    //TODO метод showHomePage, где будут данные о балансе и т д

    @GetMapping("/home")
    public String helloToUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        return personDetails.getPerson().getAccounts().toString();
    }
}
