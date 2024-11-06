package ru.siladimary.BankProject.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.siladimary.BankProject.dto.HomePageResponse;
import ru.siladimary.BankProject.dto.TransferRequest;
import ru.siladimary.BankProject.exceptions.ErrorConstructUtil;
import ru.siladimary.BankProject.exceptions.ErrorResponse;
import ru.siladimary.BankProject.exceptions.PersonNotFoundException;
import ru.siladimary.BankProject.models.Account;
import ru.siladimary.BankProject.models.Person;
import ru.siladimary.BankProject.security.PersonDetails;
import ru.siladimary.BankProject.services.AccountsService;
import ru.siladimary.BankProject.services.PeopleService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @GetMapping("/homePage")
    public ResponseEntity<?> showHomePage(){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
            String username = personDetails.getUsername();

            Person person = peopleService.findByUsername(username).
                    orElseThrow(PersonNotFoundException::new);

            Map<Integer, BigDecimal> accountsInfo = new HashMap<>();
            List<Account> accounts = person.getAccounts();
            for(Account account: accounts)
                accountsInfo.put(account.getAccountNumber(), account.getBalance());

            HomePageResponse response = new HomePageResponse(
                    "Здравствуйте, " + person.getFirstName() + " " + person.getLastName(),
                    person.getTotalBalance(),
                    accountsInfo
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Произошла ошибка при загрузке данных");
        }
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
            return ResponseEntity.ok("Счет успешно пополнен");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
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

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> personNotFoundException() {
        ErrorResponse errorResponse = new ErrorResponse("Человека с таким логином не существует");

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
