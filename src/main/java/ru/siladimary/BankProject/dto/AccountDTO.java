package ru.siladimary.BankProject.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import ru.siladimary.BankProject.models.Person;
import ru.siladimary.BankProject.models.Transaction;
import ru.siladimary.BankProject.services.AccountsService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class AccountDTO {
    @NotNull(message = "Номер счета не может быть пустым")
    private Integer accountNumber;

    @NotNull(message = "Баланс не может быть null")
    private BigDecimal balance;

    @NotNull(message = "Логин пользователя должен здесь быть")
    private Person username;

    private List<Transaction> transactions;

    public AccountDTO(Person username) {
        this.accountNumber = AccountsService.generateAccountNumber();
        this.balance = BigDecimal.ZERO;
        this.username = username;
        this.transactions = new ArrayList<>();
    }
}
