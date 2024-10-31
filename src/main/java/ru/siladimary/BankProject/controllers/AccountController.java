package ru.siladimary.BankProject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.siladimary.BankProject.models.Account;
import ru.siladimary.BankProject.security.PersonDetails;
import ru.siladimary.BankProject.services.AccountsService;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/account")
public class AccountController {
    private final AccountsService accountsService;

    @Autowired
    public AccountController(AccountsService accountsService) {
        this.accountsService = accountsService;
    }

    //TODO метод showHomePage, где будут данные о балансе и т д

    @GetMapping("/home")    //для тестов
    public String helloToUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        return personDetails.getPerson().getAccounts().toString();
    }

    @PostMapping("/{accountNumber}/deposit")
    private ResponseEntity<String> deposit(@PathVariable Integer accountNumber,
                                           @RequestParam BigDecimal amount) {
        try {
            Optional<Account> account = accountsService.findByAccountNumber(accountNumber);
            if (account.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Счета с таким номером не существует");
            }
            accountsService.depositAccount(account.get(), amount);
            return ResponseEntity.ok("Счет успешно пополнен");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Произошла ошибка при пополнении счета");
        }
    }

}
