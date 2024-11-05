package ru.siladimary.BankProject.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.siladimary.BankProject.dto.TransferRequest;
import ru.siladimary.BankProject.exceptions.ErrorConstructUtil;
import ru.siladimary.BankProject.models.Account;
import ru.siladimary.BankProject.security.PersonDetails;
import ru.siladimary.BankProject.services.AccountsService;
import ru.siladimary.BankProject.services.PeopleService;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/account")
public class AccountController {
    private final AccountsService accountsService;
    private final PeopleService peopleService;

    @Autowired
    public AccountController(AccountsService accountsService, PeopleService peopleService) {
        this.accountsService = accountsService;
        this.peopleService = peopleService;
    }

    //TODO метод showHomePage, где будут данные о балансе и т д

    @GetMapping("/home")    //для тестов
    public String helloToUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        return personDetails.getPerson().getAccounts().toString();
    }

    @PostMapping("/{accountNumber}/deposit")
    public ResponseEntity<String> deposit(@PathVariable Integer accountNumber,
                                          @RequestParam BigDecimal amount) {
        try {
            Optional<Account> account = accountsService.findByAccountNumber(accountNumber);
            if (account.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Счета с таким номером не существует");
            }
            accountsService.deposit(account.get(), amount);
        //    peopleService.updateTotalBalance(account.get());
            return ResponseEntity.ok("Счет успешно пополнен");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(500).body("Произошла ошибка при пополнении счета");
        }
    }

    @PostMapping("/{accountNumber}/withdraw")
    public ResponseEntity<String> withdraw(@PathVariable Integer accountNumber,
                                           @RequestParam BigDecimal amount) {
        try {
            Optional<Account> account = accountsService.findByAccountNumber(accountNumber);
            if (account.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Счета с таким номером не существует");
            }
            accountsService.withdraw(account.get(), amount);
            return ResponseEntity.ok("Деньги успешно сняты");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Произошла ошибка при выводе средств");
        }
    }

    @PostMapping("/{accountNumber}/transfer")
    public ResponseEntity<String> transfer(@PathVariable Integer accountNumber,
                                           @RequestBody @Valid TransferRequest transferRequest,
                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return ResponseEntity.badRequest().body(ErrorConstructUtil.constructErrorMessageToClient(bindingResult));

        try {
            Optional<Account> account = accountsService.findByAccountNumber(accountNumber);
            if (account.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Счета с таким номером не существует");
            }

            accountsService.transfer(account.get(), transferRequest);

            return ResponseEntity.ok("Деньги успешно переведены");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Произошла ошибка при переводе средств");
        }
    }
}
