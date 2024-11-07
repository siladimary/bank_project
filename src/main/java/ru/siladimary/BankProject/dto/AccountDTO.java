package ru.siladimary.BankProject.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AccountDTO {
    @NotNull(message = "Номер счета не может быть пустым")
    private Integer accountNumber;

    @NotNull(message = "Баланс не может быть null")
    private BigDecimal balance;

    @NotNull(message = "Логин пользователя должен здесь быть")
    @JsonBackReference
    private PersonResponse username;

    @JsonIgnore
    private List<TransactionDTO> transactions;

    public AccountDTO(Integer accountNumber, PersonResponse username) {
        this.accountNumber = accountNumber;
        this.balance = BigDecimal.ZERO;
        this.username = username;
        this.transactions = new ArrayList<>();
    }
}
