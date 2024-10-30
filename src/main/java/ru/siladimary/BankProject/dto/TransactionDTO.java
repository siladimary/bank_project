package ru.siladimary.BankProject.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.siladimary.BankProject.models.Account;
import ru.siladimary.BankProject.models.TransactionAction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class TransactionDTO {

    @NotNull(message = "Действие не должно быть пустым")
    private TransactionAction transactionAction;

    @NotNull(message = "Укажите сумму")
    private BigDecimal amount;

    @NotNull(message = "Должно быть указано точное время")
    private LocalDateTime timestamp;

    @NotNull(message = "Номер счета должен быть заполнен")
    private Account accountNumber;

    public TransactionDTO(TransactionAction transactionAction, BigDecimal amount, Account accountNumber) {
        this.transactionAction = transactionAction;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
        this.accountNumber = accountNumber;
    }
}
