package ru.siladimary.BankProject.dto;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.siladimary.BankProject.models.Person;
import ru.siladimary.BankProject.services.AccountsService;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class AccountDTO {
    @NotNull(message = "Номер счета не может быть пустым")
    private Integer accountNumber;

    @NotNull(message = "Баланс не может быть null")
    private BigDecimal balance;

    @NotNull(message = "Логин пользователя должен здесь быть")
    private Person username;

    public AccountDTO(Person username) {
        this.accountNumber = AccountsService.generateAccountNumber();
        this.balance = BigDecimal.ZERO;
        this.username = username;
    }
}
