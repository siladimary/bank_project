package ru.siladimary.BankProject.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.siladimary.BankProject.dto.TransactionDTO;
import ru.siladimary.BankProject.dto.TransferRequest;
import ru.siladimary.BankProject.exceptions.AccountNotFoundException;
import ru.siladimary.BankProject.exceptions.ErrorConstructUtil;
import ru.siladimary.BankProject.exceptions.ErrorResponse;
import ru.siladimary.BankProject.models.Account;
import ru.siladimary.BankProject.models.Person;
import ru.siladimary.BankProject.models.Transaction;
import ru.siladimary.BankProject.security.PersonDetails;
import ru.siladimary.BankProject.services.AccountsService;
import ru.siladimary.BankProject.services.TransactionsService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/account")
public class AccountController {
    private final AccountsService accountsService;
    private final TransactionsService transactionsService;
    private final ModelMapper mapper;

    @Autowired
    public AccountController(AccountsService accountsService, TransactionsService transactionsService, ModelMapper mapper) {
        this.accountsService = accountsService;
        this.transactionsService = transactionsService;
        this.mapper = mapper;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createNewAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        Person person = personDetails.getPerson();

        try {
            accountsService.create(person);
            return ResponseEntity.ok("Новый аккаунт успешно зарегистрирован");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Произошла ошибка при создании нового аккаунта");
        }
    }

    @GetMapping("/{accountNumber}/transactions")
    private ResponseEntity<?> getTransactions(@PathVariable Integer accountNumber,
                                              @RequestParam(defaultValue = "0") int page,
                                              PagedResourcesAssembler<TransactionDTO> assembler) {
        try {
            Pageable pageable = PageRequest.of(page, 20, Sort.by("timestamp").descending());

            Page<Transaction> transactionsPage = transactionsService.findTransactions(accountNumber, pageable);
            if (transactionsPage.isEmpty()) {
                return ResponseEntity.ok("Пока что не было выполнено ни одной транзакции");
            }

            PagedModel<EntityModel<TransactionDTO>> pagedModel = assembler.toModel(transactionsPage.
                    map(transaction -> mapper.map(transaction, TransactionDTO.class)));
            return ResponseEntity.ok(pagedModel);
        } catch (AccountNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); //TODO проверить другие методы, где есть это исключение, чтобы сообщение выводилось пользователю
        } catch (Exception e){
            return ResponseEntity.status(500).body("Произошла ошибка при загрузке транзакций");
        }
    }


    @PostMapping("/{accountNumber}/deposit")
    public ResponseEntity<String> deposit(@PathVariable Integer accountNumber,
                                          @RequestParam BigDecimal amount) {
        try {
            Account account = accountsService.checkAccount(accountNumber);
            accountsService.deposit(account, amount);
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
            Account account = accountsService.checkAccount(accountNumber);
            accountsService.withdraw(account, amount);
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
            Account account = accountsService.checkAccount(accountNumber);
            accountsService.transfer(account, transferRequest);
            return ResponseEntity.ok("Деньги успешно переведены");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Произошла ошибка при переводе средств");
        }
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> accountNotFoundException(AccountNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
