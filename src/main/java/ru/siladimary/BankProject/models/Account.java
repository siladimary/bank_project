package ru.siladimary.BankProject.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.siladimary.BankProject.services.AccountsService;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Account")
public class Account implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Номер счета не может быть пустым")
    @Column(name = "account_number")
    private Integer accountNumber;

    @NotNull(message = "Баланс не может быть null")
    @Column(name = "balance")
    private BigDecimal balance;

    @ManyToOne
    @JoinColumn(name = "username", referencedColumnName = "username")
    @NotNull(message = "Логин пользователя должен здесь быть")
    private Person username;

    public Account(Person username) {
        this.accountNumber = AccountsService.generateAccountNumber();
        this.balance = BigDecimal.ZERO;
        this.username = username;
    }

    //Возможно понадобится @Transient со списком транзакций
}
